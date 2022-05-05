package com.company.validation;

import com.company.dto.ProfileDTO;
import com.company.exceptions.AppBadRequestException;

public class ProfileValidation {

    public static void isValid(ProfileDTO dto) {
        if (dto.getName() == null || dto.getName().trim().length() < 3) {
            throw new AppBadRequestException("Name not valid");
        }
        if (dto.getSurname() == null || dto.getSurname().trim().length() < 3) {
            throw new AppBadRequestException("Surname not valid");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().length() < 5) {
            throw new AppBadRequestException("Password not valid");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().length() < 3 || !dto.getEmail().contains("@")) {
            throw new AppBadRequestException("Email not valid");
        }
        if (dto.getRole() == null) {
            throw new AppBadRequestException("Role can not be null");
        }
    }
}
