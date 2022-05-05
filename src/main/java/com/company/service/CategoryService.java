package com.company.service;

import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.exceptions.AppBadRequestException;
import com.company.exceptions.AppForbiddenException;
import com.company.exceptions.CategoryAlredyExistsException;
import com.company.repository.CategoryRepository;
import com.company.validation.CategoryValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto, Integer pId) {
        ProfileEntity profile = profileService.get(pId);
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            throw new AppForbiddenException("Not access");
        }
        CategoryValidation.isValid(dto);
        CategoryEntity category = categoryRepository.findByKey(dto.getKey());
        if (category != null) {
            throw new CategoryAlredyExistsException("Category Already Exists");
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setKey(dto.getKey());
        entity.setProfileId(pId);

        categoryRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public PageImpl<CategoryDTO> getList(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CategoryEntity> pagination = categoryRepository.findAll(pageable);

        List<CategoryEntity> profileEntityList = pagination.getContent();
        long totalElement = pagination.getTotalElements();
        List<CategoryDTO> dtoList = profileEntityList.stream().map(this::toDTO).toList();
        return new PageImpl<CategoryDTO>(dtoList, pageable, totalElement);
    }

    public CategoryDTO getById(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Id Not Found");
        }
        CategoryEntity category = optional.get();
        return toDTO(category);
    }

    public CategoryDTO getById(Integer id, LangEnum lang) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Id Not Found");
        }
        CategoryEntity category = optional.get();
        return toDTO(category, lang);
    }

    public String update(Integer id, CategoryDTO dto) {
        ProfileEntity profile = profileService.get(dto.getProfileId());
        if (!profile.getRole().equals(ProfileRole.ADMIN)) {
            throw new AppForbiddenException("Not access");
        }
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Id Not Found");
        }
        CategoryValidation.isValid(dto);
        CategoryEntity entity = categoryRepository.findByKey(dto.getKey());
        if (entity != null) {
            throw new CategoryAlredyExistsException("Category alredy exists");
        }
        CategoryEntity category = optional.get();
        category.setNameUz(dto.getNameUz());
        category.setNameEn(dto.getNameEn());
        category.setNameRu(dto.getNameRu());
        category.setKey(dto.getKey());
        category.setProfileId(dto.getProfileId());

        categoryRepository.save(category);
        return "Success";
    }

    public String delete(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("Id Not Found");
        }
        CategoryEntity entity = optional.get();
        categoryRepository.delete(entity);
        return "Success";
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setKey(entity.getKey());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }

    private CategoryDTO toDTO(CategoryEntity entity, LangEnum lang) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        switch (lang) {
            case uz:
                dto.setName(entity.getNameUz());
                break;
            case ru:
                dto.setName(entity.getNameRu());
                break;
            case en:
                dto.setName(entity.getNameEn());
                break;
        }

        return dto;
    }

    public List<CategoryDTO> getRegionList(LangEnum lang) {
        List<CategoryEntity> entityList = categoryRepository.findAll();

        List<CategoryDTO> list = new ArrayList<>();
        for (CategoryEntity entity : entityList) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(entity.getId());
            dto.setKey(entity.getKey());
            switch (lang) {
                case uz: {
                    dto.setName(entity.getNameUz());
                }
                case en: {
                    dto.setName(entity.getNameEn());
                }
                case ru: {
                    dto.setName(entity.getNameRu());
                }
            }
            list.add(dto);
        }
        return list;
    }
}
