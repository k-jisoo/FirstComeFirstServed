package com.sparta.firstcomefirstserved.user.service;

import com.sparta.firstcomefirstserved.user.dto.LoginRequestDto;
import com.sparta.firstcomefirstserved.user.dto.SignupRequestDto;
import com.sparta.firstcomefirstserved.user.entity.User;
import com.sparta.firstcomefirstserved.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private JavaMailSender mailSender;

    public UserService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public void registerUser(SignupRequestDto signupRequestDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        User user = new User(signupRequestDto);
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);

        sendVerificationEmail(savedUser);
    }

    private void sendVerificationEmail(User user) {
        String toAddress = user.getEmail();
        String subject = "Please verify your registration";
        String content = "Dear " + user.getUsername() + ",<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company.";

        String verifyURL = "http://localhost:8080/api/user/verify?token=" + user.getVerificationToken();

        content = content.replace("[[URL]]", verifyURL);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("boy7442@naver.com");
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public ResponseEntity<String> verifyUser(String token) {
        try {
            User user = userRepository.findByVerificationToken(token);

            log.info(token);
            log.info(user.getId().toString());
            user.setVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok("User verified successfully");
    }

    public ResponseEntity<String> login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            return new ResponseEntity<String>("not a valid password", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok("Login successful");


    }
}
