package kz.bitlab.group29.group29security.services;

import kz.bitlab.group29.group29security.entities.Users;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    boolean uploadAva(MultipartFile file, Users user);

}
