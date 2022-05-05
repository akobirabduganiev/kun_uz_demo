package com.company.dto;

import com.company.enums.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDTO extends BaseDTO {
    private LikeStatus status;
    private Integer profileId;
    private Integer articleId;


    private Integer likeCount;
    private Integer disLikeCount;
}
