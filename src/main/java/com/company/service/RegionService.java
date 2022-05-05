package com.company.service;

import com.company.dto.RegionDTO;
import com.company.entity.ProfileEntity;
import com.company.entity.RegionEntity;
import com.company.enums.LangEnum;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.RegionAlreadyExistsException;
import com.company.repository.RegionRepository;
import com.company.validation.RegionValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ProfileService profileService;

    public RegionDTO create(RegionDTO dto, Integer pId) {
        ProfileEntity profileEntity = profileService.get(pId);
        RegionValidation.isValid(dto);

        Optional<RegionEntity> optional = regionRepository.findByKey(dto.getKey());
        if (optional.isPresent()) {
            throw new RegionAlreadyExistsException("This Region already used!");
        }

        RegionEntity entity = new RegionEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setKey(dto.getKey());
        entity.setProfile(profileEntity);

        regionRepository.save(entity);
        return toDTO(entity);
    }

    public List<RegionDTO> list() {
        List<RegionDTO> list = new ArrayList<>();
        regionRepository.findAllByVisible(true).forEach(entity -> {
            list.add(toDTO(entity));
        });
        if (list.isEmpty()) {
            throw new ItemNotFoundException("Not Found!");
        }
        return list;
    }

    public List<RegionDTO> list(LangEnum lang) {
        List<RegionEntity> entityList = regionRepository.findAllByVisible(true);
        List<RegionDTO> dtoList = new ArrayList<>();

        for (RegionEntity entity : entityList) {
            RegionDTO dto = new RegionDTO();
            dto.setKey(entity.getKey());
            dto.setId(entity.getId());

            switch (lang) {
                case uz:
                    dto.setName(entity.getNameUz());
                    break;
                case en:
                    dto.setName(entity.getNameEn());
                    break;
                case ru:
                    dto.setName(entity.getNameRu());
                    break;
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    public RegionDTO update(Integer id, RegionDTO dto) {
        RegionValidation.isValid(dto); // validation
        ProfileEntity profileEntity = profileService.get(dto.getProfileId());

        RegionEntity entity = regionRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found!"));

        if (!entity.getVisible()) {
            throw new ItemNotFoundException("Not Found!");
        }

        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setKey(dto.getKey());
        regionRepository.save(entity);
        return toDTO(entity);
    }

    public Boolean delete(Integer id) {
        RegionEntity entity = regionRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found!"));

        if (!entity.getVisible()) {
            throw new ItemNotFoundException("Not Found!");
        }

        int n = regionRepository.updateVisible(false, id);
        return n > 0;
    }

    public RegionDTO getById(Integer id, LangEnum lang) {
        RegionEntity regionEntity = regionRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Region not found");
        });

        return toDTO(regionEntity, lang);
    }

    private RegionDTO toDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setNameUz(entity.getNameUz());
        dto.setKey(entity.getKey());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private RegionDTO toDTO(RegionEntity entity, LangEnum lang) {
        RegionDTO dto = new RegionDTO();
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

}
