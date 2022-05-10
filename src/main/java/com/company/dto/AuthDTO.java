package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class AuthDTO {
    @NotNull(message = "Email required! ")
    @Email(message = "Email Required!")
    private String email;
    @NotNull(message = "Password Required!")
    private String password;
}
