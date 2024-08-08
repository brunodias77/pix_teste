package com.brunodias.pixTest.services;
import com.brunodias.pixTest.dtos.users.RegisterUserResponse;
import com.brunodias.pixTest.entities.User;
import com.brunodias.pixTest.repositories.UserRepository;
import com.brunodias.pixTest.utils.RandomString;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    public RegisterUserResponse registerUser(User user) throws MessagingException {
        if(userRepository.findByEmail(user.getEmail()) != null)
        {
            throw new RuntimeException("Esse e-mail ja existe");
        }else{
            var encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            var randomCode = RandomString.generateRandomString(64);
            user.setVerificationCode(randomCode);
            user.setEnabled(false);
            User savedUser = this.userRepository.save(user);
            mailService.sendVerificationEmail(user);
            return new RegisterUserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPassword());
        }

    }

    public boolean verify(String verificationCode){

        User user = this.userRepository.findByVerificationCode(verificationCode);

        if(user == null || user.isEnabled()){
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            this.userRepository.save(user);

            return true;
        }
    }
}
