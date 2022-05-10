package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.enums.LangEnum;
import com.company.exceptions.AppBadRequestException;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.mapper.ArticleSimpleMapper;
import com.company.repository.ArticleRepository;
import com.company.validation.ArticleValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Value("${server.domain.name}")
    private String domainName;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private ArticleTypeService articleTypeService;
    @Autowired
    private TagService tagService;


    public ArticleDTO create(ArticleDTO dto, Integer pId) {
        Optional<ArticleEntity> optional = articleRepository.findByTitle(dto.getTitle());
        if (optional.isPresent()) {
            throw new ItemAlreadyExistsException("This Article already used!");
        }

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setProfileId(pId);

        entity.setStatus(ArticleStatus.CREATED);

        entity.setAttachId(dto.getAttachId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setRegionId(dto.getRegionId());
        entity.setTypeId(dto.getTypeId());
        entity.setTagList(dto.getTagIdList());

        articleRepository.save(entity);
        return toDTO(entity);
    }

    public List<ArticleDTO> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ArticleDTO> dtoList = new ArrayList<>();

        articleRepository.findByVisible(true, pageable).forEach(entity -> dtoList.add(toDTO(entity)));

        return dtoList;
    }

    public ArticleDTO update(Integer id, ArticleDTO dto, Integer pId) {
        ProfileEntity profileEntity = profileService.get(pId);

        ArticleValidation.isValid(dto); // validation

        ArticleEntity entity = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Not Found!"));

        if (!entity.getVisible()) {
            throw new ItemNotFoundException("Not Found!");
        }

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setProfile(profileEntity);
        entity.setUpdatedDate(LocalDateTime.now());

        articleRepository.save(entity);
        return toDTO(entity);
    }

    public Boolean delete(Integer id) {
        ArticleEntity entity = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Not Found!"));

        if (!entity.getVisible()) {
            throw new ItemNotFoundException("Not Found!");
        }

        int n = articleRepository.updateVisible(false, id);
        return n > 0;
    }

    public List<ArticleDTO> getTop5ByTypeId(Integer typeId) {
        // Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
//        List<ArticleEntity> entityList = articleRepository.findTop5ByTypeIdAndStatus(typeId, ArticleStatus.PUBLISHED, sort);

        /*Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ArticleEntity> page = articleRepository.findAllByTypeId(typeId, pageable);*/

        List<ArticleSimpleMapper> entityList = articleRepository.getTypeId(typeId, ArticleStatus.PUBLISHED.name());
        List<ArticleDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            ArticleDTO dto = new ArticleDTO();
            dto.setId(entity.getId());
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setPublishedDate(entity.getPublished_date());

            dto.setImage(attachService.toOpenURLDTO(entity.getAttach_id()));
            dtoList.add(dto);
        });
        return dtoList;
    }

    public ArticleDTO getByIdPublished(Integer articleId, LangEnum lang) {
        Optional<ArticleEntity> optional = articleRepository.findByIdAndStatus(articleId, ArticleStatus.PUBLISHED);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Item not found");
        }
        return toDetailDTO(optional.get(), lang);
    }

    public ArticleDTO getByIdAdAdmin(Integer articleId, LangEnum lang) {
        Optional<ArticleEntity> optional = articleRepository.findById(articleId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Item not found");
        }

        return toDetailDTO(optional.get(), lang);
    }

    public PageImpl<ArticleDTO> publishedListByRegion(Integer regionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ArticleDTO> dtoList = new ArrayList<>();

        Page<ArticleEntity> entityPage = articleRepository.findByRegionIdAndStatus(regionId, pageable, ArticleStatus.PUBLISHED);
        entityPage.stream().forEach(entity ->
                dtoList.add(toSimpleDTO(entity))
        );

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());

    }

    public PageImpl<ArticleDTO> publishedListByCategoryId(int page, int size, Integer cId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ArticleDTO> dtoList = new ArrayList<>();

        Page<ArticleEntity> entityPage = articleRepository.findByCategoryIdAndStatus(cId, pageable, ArticleStatus.PUBLISHED);
        entityPage.stream().forEach(entity -> dtoList.add(toSimpleDTO(entity)));

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    public PageImpl<ArticleDTO> publishedListByTypeId(int page, int size, Integer tId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ArticleDTO> dtoList = new ArrayList<>();

        Page<ArticleEntity> entityPage = articleRepository.findByTypeIdAndStatus(tId, pageable, ArticleStatus.PUBLISHED);
        entityPage.stream().forEach(entity -> dtoList.add(toSimpleDTO(entity)));

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    public List<ArticleDTO> last4() {
        List<ArticleDTO> dtoList = new ArrayList<>();

        List<ArticleSimpleMapper> entityPage = articleRepository.getLast4(ArticleStatus.PUBLISHED.name());
        entityPage.forEach(entity -> dtoList.add(toSimpleDTO(entity)));

        return dtoList;
    }

    public List<ArticleDTO> top4ByRegionId(Integer rId) {
//        Pageable pageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ArticleDTO> dtoList = new ArrayList<>();

        List<ArticleSimpleMapper> entityPage = articleRepository.getByRegionIdLast4(rId, ArticleStatus.PUBLISHED.name());
        entityPage.forEach(entity -> dtoList.add(toSimpleDTO(entity)));

        return dtoList;
    }

    public List<ArticleDTO> top4ByCategoryId(Integer cId) {
        List<ArticleDTO> dtoList = new ArrayList<>();

        List<ArticleSimpleMapper> entityPage = articleRepository.getByCategoryIdLast4(cId, ArticleStatus.PUBLISHED.name());
        entityPage.forEach(entity -> dtoList.add(toSimpleDTO(entity)));

        return dtoList;
    }

    public Boolean changeStatus(Integer aId, ArticleStatus status) {
        ArticleEntity entity = get(aId);
        try {
            if (entity.getStatus().equals(status)) {
                return false;
            }
            entity.setStatus(status);
            return articleRepository.updateStatus(status, aId) > 0;

        } catch (RuntimeException e) {
            throw new AppBadRequestException("Status not valid!");
        }
    }

    public String getShared(LangEnum lang, Integer id) {
        ArticleEntity article = get(id);
        articleRepository.updateSharedCount(article.getSharedCount() + 1, id);
        return domainName + "/article/" + lang + "/" + id;
    }

    public void updateViewCount(Integer articleId) {
        articleRepository.updateViewCount(articleId);
    }

    public ArticleDTO toDetailDTO(ArticleEntity entity, LangEnum lang) {
        ArticleDTO dto = toDTO(entity);

        dto.setViewCount(entity.getViewCount());
        dto.setSharedCount(entity.getSharedCount());

        dto.setLike(likeService.getLIkeAndDislikeCount(entity.getId()));  // like
        dto.setCategory(categoryService.getById(entity.getCategoryId(), lang)); // category
        dto.setRegion(regionService.getById(entity.getRegionId(), lang)); // region
        dto.setArticleType(articleTypeService.getById(entity.getTypeId(), lang)); // type
        dto.setTagList(tagService.getTagList(entity.getTagList(), lang)); // tag
        return dto;
    }

    private ArticleDTO toSimpleDTO(ArticleSimpleMapper entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublished_date());

        dto.setImage(attachService.toOpenURLDTO(entity.getAttach_id()));

        return dto;
    }

    private ArticleDTO toSimpleDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublishedDate());

        dto.setImage(attachService.toOpenURLDTO(entity.getAttachId()));

        return dto;
    }

    private ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());

        dto.setProfileId(entity.getProfileId());

        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPublishedDate(entity.getPublishedDate());

        dto.setImage(attachService.toOpenURLDTO(entity.getAttachId()));

        return dto;
    }

    public ArticleEntity get(Integer articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> {
            throw new ItemNotFoundException("Article Not found");
        });
    }
}
