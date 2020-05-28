package com.fundoobackendapi.serviceint;
import com.fundoobackendapi.dto.LoginDto;
import com.fundoobackendapi.exception.UserException;
import com.fundoobackendapi.responsedto.UserResponseDTO;
public interface IEmailService {
    Long sendTokenOnEmail(String email,String subject) throws UserException;
    void forgotPassword(String email) throws UserException;
    UserResponseDTO setPassword(LoginDto loginDto, String token) throws UserException;
    UserResponseDTO verifyEmail(String token)throws UserException;
}
