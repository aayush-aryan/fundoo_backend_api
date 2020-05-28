package com.fundoobackendapi.exception;
import com.fundoobackendapi.responsedto.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ResponseHelper {
    public static UserResponseDTO statusResponse(Integer status, String message, Object result) {
        UserResponseDTO statusResponse = new UserResponseDTO(status,message,result);
        return statusResponse;
    }
}
