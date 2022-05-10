package com.company.dto;

import com.company.enums.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDTO extends BaseDTO {
    @NotNull(message = "Status Required!")
    private LikeStatus status;
    private Integer profileId;
    private Integer articleId;

    private Integer likeCount;
    private Integer disLikeCount;
}
