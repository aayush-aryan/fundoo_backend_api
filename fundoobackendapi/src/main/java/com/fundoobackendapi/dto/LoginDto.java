package com.fundoobackendapi.dto;
//import javax.validation.constraints.NotEmpty;

public class LoginDto {
   // @NotEmpty
    public String email;
   // @NotEmpty
    public String password;

    @Override
    public String toString() {
        return "LoginDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
