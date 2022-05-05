package com.company.validation;

import com.company.dto.ArticleDTO;
import com.company.exceptions.AppBadRequestException;

public class ArticleValidation {
    public static void isValid(ArticleDTO dto) {
        if (dto.getTitle().trim().length() < 3 || dto.getTitle() == null) {
            throw new AppBadRequestException("Title Not Valid");
        }
        if (dto.getDescription().trim().length() < 3 || dto.getDescription() == null) {
            throw new AppBadRequestException("Description Not Valid");
        }
        if (dto.getContent().trim().length() < 3 || dto.getContent() == null) {
            throw new AppBadRequestException("Content Not Valid");
        }
        if (dto.getRegionId() == null) {
            throw new AppBadRequestException("Region Not Valid");
        }
        if (dto.getTypeId() == null) {
            throw new AppBadRequestException("Region Not Valid");
        }
        if (dto.getCategoryId() == null) {
            throw new AppBadRequestException("Region Not Valid");
        }
    }
}
