package com.fundoobackendapi.controller;
import com.fundoobackendapi.dto.LoginDto;
import com.fundoobackendapi.dto.Token;
import com.fundoobackendapi.dto.UserDto;
import com.fundoobackendapi.exception.ResponseHelper;
import com.fundoobackendapi.exception.UserException;
import com.fundoobackendapi.model.User;
import com.fundoobackendapi.responsedto.UserResponseDTO;
import com.fundoobackendapi.serviceint.IColaboratorService;
import com.fundoobackendapi.serviceint.IEmailService;
import com.fundoobackendapi.serviceint.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
//import javax.validation.Valid;
//import java.util.List;

@RestController
@RequestMapping(value = "user")
public class UserController {
    //@Qualifier("emailServiceImp")
    @Autowired
    private IEmailService service;
    @Qualifier("userServiceImp")
    @Autowired
    private IUserService userService;

    @Qualifier("colaboratorServiceImpl")
    @Autowired
    private IColaboratorService colaboratorService;

    @PostMapping("/registration")
    public UserResponseDTO registration(@RequestBody  UserDto userDto) throws UserException {
        return userService.registerUser(userDto);
    }
    @PostMapping("/verifyEmail")
    public UserResponseDTO confirmUserAccount(@RequestHeader String token) throws UserException {
        return service.verifyEmail(token);
    }

    @PostMapping("/login")
    public UserResponseDTO login(@RequestBody LoginDto loginDto) {
        try {
            return userService.loginUser(loginDto);
        }catch (UserException e){
            return ResponseHelper.statusResponse(400, "EmailId Doesn't Register",loginDto);
        }

    }
    @PostMapping("/logout")
    public UserResponseDTO logoutUser(@RequestHeader String token) {
        try {
            return userService.logoutUser(token);
        }catch (UserException e){
            return ResponseHelper.statusResponse(400, "Invalid token","token "+token);
        }
    }
    @PutMapping("/forgotPassword/{email}")
    public UserResponseDTO forgotPassword(@RequestParam String email) {
        try{
            service.forgotPassword(email);
        }catch(UserException e){
            return ResponseHelper.statusResponse(400, "EmailId Doesn't Register","Email "+email);
        }
        return ResponseHelper.statusResponse(200, "Token Is Send On This Email Id","Email "+email);
    }
    @PutMapping("/resetPassword")
    public UserResponseDTO resetPassword(@RequestBody LoginDto loginDto,@RequestHeader String token) throws UserException {
        return service.setPassword(loginDto,token);
    }
    @GetMapping("/getAllUsers")
    public UserResponseDTO getAll() {
        return userService.getAll();
    }

    @PostMapping("/addCollobrator")
    public UserResponseDTO addCollobrator(@RequestHeader String token,@RequestParam long noteId,@RequestParam String email) {

        return colaboratorService.addColaborator(token,noteId,email);
    }
    @GetMapping("/getCollobrator")
    public UserResponseDTO getColaboratorsOfNote(@RequestHeader String token, @RequestParam long noteId) {

        return colaboratorService.getColaboratorsOfNote(token,noteId);
    }
    @DeleteMapping("/removeCollobrator")
    public UserResponseDTO removeColaborator(@RequestHeader String token, @RequestParam long noteId,@RequestParam String email) {

        return colaboratorService.removeColaborator(token,noteId,email);
    }
}
