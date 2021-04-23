package kz.bitlab.group29.group29security.services;

import kz.bitlab.group29.group29security.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Users registerUser(Users user);
    boolean updatePassword(Users user, String oldPass, String newPass);
    Users updateAva(Users user);

}
