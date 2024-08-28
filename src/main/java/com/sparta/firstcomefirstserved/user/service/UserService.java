package com.sparta.firstcomefirstserved.user.service;

import com.sparta.firstcomefirstserved.user.dto.LoginRequestDto;
import com.sparta.firstcomefirstserved.user.dto.SignupRequestDto;
import com.sparta.firstcomefirstserved.user.dto.UserResponseDto;
import com.sparta.firstcomefirstserved.user.entity.User;
import com.sparta.firstcomefirstserved.user.repository.UserRepository;
import com.sparta.firstcomefirstserved.utils.AESUtil;
import com.sparta.firstcomefirstserved.utils.Encryptor;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    private final Encryptor encryptor;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JavaMailSender mailSender, AESUtil encryptor, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.encryptor = encryptor;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입
     * @param signupRequestDto username, password, email, address, phone
     * @throws Exception aesUtil.encrypt 실패 시
     */
    @Transactional
    public void registerUser(SignupRequestDto signupRequestDto) throws Exception{
        //암호화 종류에 대해 찾아볼 것
        String encodedUsername = encryptor.encrypt(signupRequestDto.getUsername());
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        String encodedEmail = encryptor.encrypt(signupRequestDto.getEmail());
        String encodedPhone = encryptor.encrypt(signupRequestDto.getPhone());
        String encodedAddress = encryptor.encrypt(signupRequestDto.getAddress());

        User encodedUser = new User(encodedUsername, encodedPassword, encodedEmail, encodedPhone, encodedAddress);
        User savedUser = userRepository.save(encodedUser);

        sendVerificationEmail(savedUser);
    }

    /**
     * SignupRequestDto의 email로 확인용 이메일 발송
     * @param user registerUser내에서 생성한 User 객체
     */
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

    /**
     * email의 Verify 버튼을 눌렀을 때 검증
     * @param token 회원가입 시 임의로 만든 난수. 이메일의 verify 버튼 클릭 시 자동으로 들어감
     * @return HttpStatus.OK, "User verified successfully"
     */
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
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok("User verified successfully");
    }

    /**
     * 로그인
     * @param loginRequestDto 사용자의 email, password
     * @return HttpStatus.OK, "Login successful"
     * @throws Exception HttpStatus.UNAUTHORIZED, "Invalid password"
     */
    public ResponseEntity<String> login(LoginRequestDto loginRequestDto) throws Exception{
        String encodedEmail = encryptor.encrypt(loginRequestDto.getEmail());
        User user = userRepository.findByEmail(encodedEmail).orElse(null);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
        }

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            return new ResponseEntity<String>("Invalid password", HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok("Login successful");
    }

    public ResponseEntity<UserResponseDto> getUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserResponseDto userResponseDto = new UserResponseDto(user);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userResponseDto);
    }
}
