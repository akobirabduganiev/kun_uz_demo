package com.company.dto;

import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.service.ArticleService;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private Integer id;
    private String title;
    private String description;
    private String content;
    private Boolean visible;

    private Integer profileId;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private ArticleStatus status;
    private LocalDateTime publishedDate;

    private String attachId;
    private Integer categoryId;
    private Integer regionId;
    private Integer typeId;

    private Integer viewCount;
    private Integer sharedCount;

    private List<Integer> tagIdList; // create
    private List<TagDTO> tagList; // get full

    private AttachDTO image;
    private LikeDTO like;
    private CategoryDTO category;
    private RegionDTO region;
    private ArticleTypeDTO articleType;


}
