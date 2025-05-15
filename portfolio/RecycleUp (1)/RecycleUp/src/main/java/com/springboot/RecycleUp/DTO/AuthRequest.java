package com.springboot.RecycleUp.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;

    public AuthRequest(String email, String password){this.email = email; this.password = password;}

    public String getEmail(){return this.email;}
    public String getPassword(){return this.password;}
}
