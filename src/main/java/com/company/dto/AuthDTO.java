package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class AuthDTO {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
