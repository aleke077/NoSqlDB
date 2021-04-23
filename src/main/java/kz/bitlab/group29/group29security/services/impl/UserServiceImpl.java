package kz.bitlab.group29.group29security.services.impl;

import kz.bitlab.group29.group29security.entities.Roles;
import kz.bitlab.group29.group29security.entities.Users;
import kz.bitlab.group29.group29security.repository.RoleRepository;
import kz.bitlab.group29.group29security.repository.UserRepository;
import kz.bitlab.group29.group29security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Users user = userRepository.findByEmail(s);

        if(user!=null){
            return user;
        }else{
            throw new UsernameNotFoundException("User not found");
        }

    }

    @Override
    public Users registerUser(Users user) {

        Users checkUser = userRepository.findByEmail(user.getEmail());
        if(checkUser==null){

            List<Roles> roles = new ArrayList<>();
            Roles role = roleRepository.findByRole("ROLE_USER");
            roles.add(role);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Users result = userRepository.save(user);

            return result;

        }

        return null;
    }

    @Override
    public boolean updatePassword(Users user, String oldPass, String newPass) {

        if(passwordEncoder.matches(oldPass, user.getPassword())){

            user.setPassword(passwordEncoder.encode(newPass));
            userRepository.save(user);
            return true;

        }

        return false;

    }

    @Override
    public Users updateAva(Users user) {
        Users checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser != null) {
            checkUser.setAvatarURL(user.getAvatarURL());
            return userRepository.save(checkUser);
        }
        return null;
    }
}
