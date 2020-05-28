package com.fundoobackendapi.dto;
//import javax.validation.constraints.Pattern;

public class UserDto {

  //  @Pattern(regexp = "^[A-Z][a-z]{2,}$",message = "Start from upper case and length should be 3")
    public String firstName;

   // @Pattern(regexp = "^[A-Z][a-z]{2,}$",message = "Start from upper case and length should be 3")
    public String lastName;

   // @Pattern(regexp = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})",message = "Must contains 1 uppercase 1 lower case " +
         //   "1 spacial char 1 num and min length should 8")
    public String password;

   // @Pattern(regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",message = "Enter like abc@gmail.com")
    public String email;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword()

    public void setPassword(String password) {
        this.password = password;
    }

}
