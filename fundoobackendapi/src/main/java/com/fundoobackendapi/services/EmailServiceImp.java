package com.fundoobackendapi.services;
import com.fundoobackendapi.dto.LoginDto;
import com.fundoobackendapi.dto.RabbitMqDto;
import com.fundoobackendapi.exception.ResponseHelper;
import com.fundoobackendapi.exception.UserException;
import com.fundoobackendapi.model.User;
import com.fundoobackendapi.repository.IUserRepository;
import com.fundoobackendapi.responsedto.UserResponseDTO;
import com.fundoobackendapi.serviceint.IEmailService;
import com.fundoobackendapi.utility.JwtTokenUtil;
import com.fundoobackendapi.utility.RabbitMqImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class EmailServiceImp implements IEmailService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RabbitMqDto rabbitMqDto;

    @Autowired
    private RabbitMqImp rabbitMq;

    @Override
    public Long sendTokenOnEmail(String email,String subject) throws UserException {
        return userRepository.findByEmail(email)
                .map(user -> {
                    String token = jwtTokenUtil.createToken(user.getId(), 30 * 60 * 1000);
                    String body = token;
                    rabbitMqDto.setTo(email);
                    rabbitMqDto.setFrom("ubuntushell89@gmail.com");
                    rabbitMqDto.setSubject(subject);
                    rabbitMqDto.setBody(body);
                    rabbitMq.sendMessageToQueue(rabbitMqDto);
                    return user.getId();
                })
                .orElseThrow(() -> new UserException(UserException.exceptionType.INVALID_EMAIL_ID));
    }
    @Override
        public UserResponseDTO verifyEmail(String token) throws UserException{
            long id = jwtTokenUtil.decodeToken(token);
            Optional<User> userDetails = userRepository.findById(id);
            if(userDetails==null) ResponseHelper.statusResponse(400, "Invalid Token","token : "+token);
            userRepository.findById(id)
                    .map(user -> {
                        user.setIsVerified(1);
                        return user;
                    })
                    .map(userRepository::save).get();

            return ResponseHelper.statusResponse(200, "Email Verified Successfully","User ID : "+id);
        }
        @Override
        public void forgotPassword(String email) throws UserException{
            userRepository.findByEmail(email).get().setIsVerified(0);
            sendTokenOnEmail(email,"Reset Password!");
        }
        @Override
        public UserResponseDTO setPassword(LoginDto loginDto,String token) throws UserException{
            UserResponseDTO userResponseDTO = verifyEmail(token);
            if (userResponseDTO.status == 200) {
                Optional<User> userDetails = userRepository.findByEmail(loginDto.email);
                if (userDetails == null)
                    ResponseHelper.statusResponse(400, "Invalid Email", "Email ID : " + loginDto.email);
                userDetails.map(user -> {
                    String password = passwordEncoder.encode(loginDto.password);
                    user.setPassword(password);
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    user.setModDate(localDateTime.format(dateFormat));
                    return user;
                }).map(userRepository::save).get();
                Optional<User> updatedUserDetails = userRepository.findByEmail(loginDto.email);
                return ResponseHelper.statusResponse(200, "Password Reset Successfully", "Login Details : " + updatedUserDetails);
            }
            return userResponseDTO;
        }
    }


