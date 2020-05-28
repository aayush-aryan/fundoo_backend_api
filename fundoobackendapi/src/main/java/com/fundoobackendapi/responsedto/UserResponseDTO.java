package com.fundoobackendapi.responsedto;
public class UserResponseDTO {
    public Integer status;
    public String message;
    public Object result;

    public UserResponseDTO(Integer status,String message,Object result) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", userResponse=" + result +
                '}';
    }
}
