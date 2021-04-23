package kz.bitlab.group29.group29security.controllers;

import kz.bitlab.group29.group29security.entities.Cities;
import kz.bitlab.group29.group29security.entities.Clubs;
import kz.bitlab.group29.group29security.entities.Users;
import kz.bitlab.group29.group29security.services.ClubService;
import kz.bitlab.group29.group29security.services.FileUploadService;
import kz.bitlab.group29.group29security.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private FileUploadService uploadService;

    @Value("${loadURL}")
    private String loadURL;

    @GetMapping(value = "/")
    public String index(Model model){
        model.addAttribute("currentUser", getUser());
        return "index";
    }

    @GetMapping(value = "/loginpage")
    public String loginPage(Model model){
        model.addAttribute("currentUser", getUser());
        return "loginpage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/profile")
    public String profile(Model model){
        model.addAttribute("currentUser", getUser());
        return "profile";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/admin")
    public String admin(Model model,
                        @RequestParam(name = "name", defaultValue = "", required = false) String name,
                        @RequestParam(name = "city_id", required = false, defaultValue = "0") Long cityId,
                        @RequestParam(name = "ulc_from", required = false) Integer uclFrom,
                        @RequestParam(name = "ulc_to", required = false) Integer uclTo,
                        @RequestParam(name = "league_from", required = false) Integer leaguesFrom,
                        @RequestParam(name = "league_to", required = false) Integer leaguesTo){

        model.addAttribute("currentUser", getUser());

        List<Cities> cities = clubService.getAllCities();
        model.addAttribute("cities", cities);

        List<Clubs> clubs = clubService.searchClubs(name, cityId, uclFrom, uclTo, leaguesFrom, leaguesTo);
        model.addAttribute("clubs", clubs);

        return "admin";
    }

    @GetMapping(value = "/403")
    public String accessDeniedPage(Model model){
        model.addAttribute("currentUser", getUser());
        return "403";
    }

    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("currentUser", getUser());
        return "register";
    }

    @PostMapping(value = "/toregister")
    public String toRegister(@RequestParam(name = "user_email") String userEmail,
                             @RequestParam(name = "user_password") String userPassword,
                             @RequestParam(name = "re_user_password") String reUserPassword,
                             @RequestParam(name = "user_full_name") String fullName){

        if(userPassword.equals(reUserPassword)){

            Users user = new Users();
            user.setEmail(userEmail);
            user.setPassword(userPassword);
            user.setFullName(fullName);
            user = userService.registerUser(user);
            if(user!=null && user.getId()!=null){
                return "redirect:/register?success";
            }

        }

        return "redirect:/register?error";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updatepassword")
    public String updatePassword(@RequestParam(name = "old_password") String oldPassword,
                                 @RequestParam(name = "new_password") String newPassword,
                                 @RequestParam(name = "re_new_password") String reNewPassword){

        if(newPassword.equals(reNewPassword)){

            Users currentUser = getUser();
            if(userService.updatePassword(currentUser, oldPassword, newPassword)){

                return "redirect:/profile?success";

            }

        }

        return "redirect:/profile?error";

    }

    @PostMapping(value = "/addclub")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addClub(@RequestParam(name = "name") String name,
                          @RequestParam(name = "city_id") Long cityId,
                          @RequestParam(name = "ucl_titles") int ucl,
                          @RequestParam(name = "league_titles") int leagues){

        Cities city = clubService.getCity(cityId);
        if(city!=null){
            Clubs club = new Clubs();
            club.setName(name);
            club.setCity(city);
            club.setChampionsLeagueTitles(ucl);
            club.setLeagueTitles(leagues);
            clubService.addClub(club);
            return "redirect:/admin?success";
        }

        return "redirect:/admin?error";

    }

    @GetMapping(value = "/details/{idbek}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String details(@PathVariable(name = "idbek") Long id, Model model){

        model.addAttribute("currentUser", getUser());

        List<Cities> cities = clubService.getAllCities();
        model.addAttribute("cities", cities);

        Clubs club = clubService.getClub(id);
        if(club!=null){
            model.addAttribute("club", club);
            return "details";
        }

        return "404";

    }

    @PostMapping(value = "/saveclub")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String saveClub(@RequestParam(name = "id") Long id,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "city_id") Long cityId,
                          @RequestParam(name = "ucl_titles") int ucl,
                          @RequestParam(name = "league_titles") int leagues){

        Cities city = clubService.getCity(cityId);
        if(city!=null){
            Clubs club = clubService.getClub(id);
            if(club!=null) {
                club.setName(name);
                club.setCity(city);
                club.setChampionsLeagueTitles(ucl);
                club.setLeagueTitles(leagues);
                clubService.saveClub(club);
                return "redirect:/details/"+id+"?success";
            }
        }

        return "redirect:/details"+id+"?error";

    }

    @PostMapping(value = "/deleteclub")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteClub(@RequestParam(name = "id") Long id){

        Clubs club = clubService.getClub(id);
        if(club!=null) {
            clubService.deleteClub(club);
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/uploadavatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name = "user_avatar") MultipartFile file){
        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")){

            uploadService.uploadAva(file, getUser());

        }

        return "redirect:/profile";
    }

    @GetMapping(value = "/viewava/{avaURL}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewAva(@PathVariable(name = "avaURL") String avaURL) throws IOException {

        String picURL = loadURL+"default_user.png";
        if(avaURL!=null){
            picURL = loadURL+avaURL+".jpg";
        }
        InputStream in;
        try{
            ClassPathResource resource = new ClassPathResource(picURL);
            in = resource.getInputStream();
        }catch (Exception e){
            picURL = loadURL+"default_user.png";
            ClassPathResource resource = new ClassPathResource(picURL);
            in = resource.getInputStream();
        }

        return IOUtils.toByteArray(in);

    }


    private Users getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user = (Users) authentication.getPrincipal();
            return user;
        }
        return null;
    }

}
