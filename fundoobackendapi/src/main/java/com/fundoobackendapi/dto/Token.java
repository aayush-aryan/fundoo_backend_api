package com.fundoobackendapi.dto;
public class Token {
    private String token;
    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }
}
