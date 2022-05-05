package com.company.mapper;

import java.time.LocalDateTime;

public interface ArticleSimpleMapper {
    Integer getId();

    String getTitle();

    String getDescription();

    String getAttach_id();

    LocalDateTime getPublished_date();
}
