package kz.bitlab.group29.group29security.services.impl;

import kz.bitlab.group29.group29security.entities.Users;
import kz.bitlab.group29.group29security.services.FileUploadService;
import kz.bitlab.group29.group29security.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private UserService userService;

    @Value("${targetURL}")
    private String targetURL;

    @Override
    public boolean uploadAva(MultipartFile file, Users user) {
        try{

            String fileName = user.getId()+"pic_kartina";
            fileName = DigestUtils.sha1Hex(fileName);

            byte []bytes = file.getBytes();
            Path path = Paths.get(targetURL+fileName+".jpg");
            Files.write(path, bytes);

            user.setAvatarURL(fileName);
            userService.updateAva(user);

        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
}
