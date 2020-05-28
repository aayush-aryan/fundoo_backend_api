package com.fundoobackendapi.services;
import com.fundoobackendapi.dto.LoginDto;
import com.fundoobackendapi.dto.UserDto;
import com.fundoobackendapi.exception.ResponseHelper;
import com.fundoobackendapi.exception.UserException;
import com.fundoobackendapi.model.User;
import com.fundoobackendapi.repository.IUserRepository;
import com.fundoobackendapi.responsedto.UserResponseDTO;
import com.fundoobackendapi.serviceint.IEmailService;
import com.fundoobackendapi.serviceint.IUserService;
import com.fundoobackendapi.utility.JwtTokenUtil;
import com.fundoobackendapi.utility.RedisTemplateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplateUtility<Object> redis;
    @Qualifier("emailServiceImp")
    @Autowired
    private IEmailService service;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserResponseDTO registerUser(UserDto userDto) throws UserException {
        Optional<User> users = userRepository.findByEmail(userDto.getEmail());
        if (users.isPresent()) return ResponseHelper.statusResponse(400, "This Mail Is Already Registered",userDto);
        User user = new User(userDto);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:a");
        user.setRegDate(localDateTime.format(dateFormat));
        user.setPassword(passwordEncoder.encode(userDto.password));
        userRepository.save(user);
        service.sendTokenOnEmail(userDto.getEmail(),"Complete Registration!");
        return ResponseHelper.statusResponse(200, "Registration Successful, A Token Is Send To Your Registered EmailId",userDto.email);
    }

    @Override
    public UserResponseDTO loginUser(LoginDto loginDto) throws UserException {
        User user = userRepository.findByEmail(loginDto.email)
                .orElseThrow(() -> new UserException(UserException.exceptionType.INVALID_EMAIL_ID));
        if (user.getIsVerified()==0)
            return ResponseHelper.statusResponse(400, "Unverified Users",loginDto);
        if (!passwordEncoder.matches(loginDto.password, user.getPassword()))
            return ResponseHelper.statusResponse(400, "Incorrect Password",loginDto);
        String token = jwtTokenUtil.createToken(user.getId(),3*60*60*1000);
        user.setStatus("active");
        userRepository.save(user);
        return ResponseHelper.statusResponse(200, "Hi "+user.getFirstName()+"! Login Success",token);
    }
    @Override
    public UserResponseDTO logoutUser(String token) throws UserException {
        long userId = jwtTokenUtil.decodeToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserException.exceptionType.INVALID_EMAIL_ID));
        if(user.getStatus().equals("active")) {
            user.setStatus("inactive");
            userRepository.save(user);
            redis.deleteMap("Key",token);
            return ResponseHelper.statusResponse(200, "User LogOut Successfully", "User Id "+userId);
        }
        return ResponseHelper.statusResponse(400, "Login First","UserId "+userId);
    }
    @Override
    public  UserResponseDTO getAll() {
        List<User> users = userRepository.findAll();
        return ResponseHelper.statusResponse(200, "Total "+users.size()+" Registered Users Found",users);
    }
}

