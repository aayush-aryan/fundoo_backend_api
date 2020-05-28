package com.fundoobackendapi.serviceint;
import com.fundoobackendapi.dto.LoginDto;
import com.fundoobackendapi.dto.UserDto;
import com.fundoobackendapi.exception.UserException;
import com.fundoobackendapi.responsedto.UserResponseDTO;

public interface IUserService {
    UserResponseDTO registerUser(UserDto userDto) throws UserException;

    UserResponseDTO loginUser(LoginDto loginDto) throws UserException;

    UserResponseDTO logoutUser(String email) throws UserException;

    UserResponseDTO getAll();
}
