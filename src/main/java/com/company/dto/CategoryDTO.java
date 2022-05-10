package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private Integer id;
    private String key;
    @NotNull(message = "Name Required!")
    private String name;
    @NotNull(message = "Name Required!")
    private String nameUz;
    @NotNull(message = "Name Required!")
    private String nameRu;
    @NotNull(message = "Name Required!")
    private String nameEn;
    private Integer profileId;
    private LocalDateTime createdDate;

}
