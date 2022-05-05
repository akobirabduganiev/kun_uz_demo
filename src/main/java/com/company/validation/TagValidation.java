package com.company.validation;

import com.company.dto.TagDTO;
import com.company.exceptions.AppBadRequestException;

public class TagValidation {
    public static void isValid(TagDTO dto) {
        if (!dto.getNameUz().startsWith("#") || dto.getNameUz().trim().length() < 3 || dto.getNameUz() == null) {
            throw new AppBadRequestException("Name Uz Not Valid");
        }
        if (!dto.getNameEn().startsWith("#") || dto.getNameEn().trim().length() < 3 || dto.getNameEn() == null) {
            throw new AppBadRequestException("Name En Not Valid");
        }
        if (!dto.getNameRu().startsWith("#") || dto.getNameRu().trim().length() < 3 || dto.getNameRu() == null) {
            throw new AppBadRequestException("Name Ru Not Valid");
        }
        if (!dto.getKey().startsWith("#") || dto.getKey().trim().length() < 3 || dto.getKey() == null) {
            throw new AppBadRequestException("Key Not Valid");
        }
    }
}
