package com.company.service;

import com.company.dto.TagDTO;
import com.company.entity.TagEntity;
import com.company.enums.LangEnum;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.TagRepository;
import com.company.validation.TagValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ProfileService profileService;

    public TagDTO create(TagDTO dto, Integer pId) {

        TagValidation.isValid(dto); // validation

        Optional<TagEntity> optional = tagRepository.findByKey(dto.getKey());
        if (optional.isPresent()) {
            throw new ItemAlreadyExistsException("This Tag already used!");
        }

        TagEntity entity = new TagEntity();
        entity.setKey(dto.getKey());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setNameUz(dto.getNameUz());
        entity.setProfileId(pId);

        tagRepository.save(entity);
        return toDTO(entity);
    }

    public PageImpl<TagDTO> listByLang(LangEnum lang, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<TagDTO> dtoList = new ArrayList<>();

        Page<TagEntity> entityPage = tagRepository.findByVisible(true, pageable);

        entityPage.forEach(entity -> {
            TagDTO dto = new TagDTO();
            dto.setId(entity.getId());
            dto.setKey(entity.getKey());

            switch (lang) {
                case en:
                    dto.setName(entity.getNameEn());
                    break;
                case ru:
                    dto.setName(entity.getNameRu());
                    break;
                case uz:
                    dto.setName(entity.getNameUz());
                    break;
            }

            dtoList.add(dto);
        });

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    public TagDTO update(Integer id, TagDTO dto, Integer pId) {
//        ProfileEntity profileEntity = profileService.get(pId);

        TagValidation.isValid(dto); // validation

        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Not Found!"));

        if (!entity.getVisible()) {
            throw new ItemNotFoundException("Not Found!");
        }

        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
//        entity.setProfile(profileEntity);
        entity.setProfileId(pId);
        entity.setUpdatedDate(LocalDateTime.now());

        tagRepository.save(entity);
        return toDTO(entity);
    }

    public Boolean delete(Integer id) {
        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Not Found!"));

        if (!entity.getVisible()) {
            throw new ItemNotFoundException("Not Found!");
        }

        int n = tagRepository.updateVisible(false, id);
        return n > 0;
    }

    private TagDTO toDTO(TagEntity entity) {
        TagDTO dto = new TagDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        dto.setNameEn(entity.getNameEn());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
//        dto.setProfileId(entity.getProfile().getId());
        dto.setProfileId(entity.getProfileId());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private TagDTO toDTO(TagEntity entity, LangEnum lang) {
        TagDTO dto = new TagDTO();
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

    public List<TagDTO> getTagList(List<Integer> tagList, LangEnum lang) {
        List<TagEntity> entityList = tagRepository.findAllById(tagList);
        List<TagDTO> dtoList = new LinkedList<>();
        for (TagEntity entity : entityList) {
            dtoList.add(toDTO(entity, lang));
        }
        return dtoList;
    }


}
