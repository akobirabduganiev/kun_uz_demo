package com.company.dto;

import com.company.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    @NotNull(message = "Name Required!")
    private String name;
    @NotNull(message = "Surname Required!")
    private String surname;
    @NotNull(message = "Email Required!")
    private String email;
    @NotNull(message = "Password Required!")
    private String password;
    private ProfileRole role;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDate;

    private String jwt;

    private AttachDTO image;
}
