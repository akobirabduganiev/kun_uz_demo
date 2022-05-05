package com.company.repository;

import com.company.entity.ArticleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTypeRepository extends JpaRepository<ArticleTypeEntity, Integer> {
    ArticleTypeEntity findByKey(String key);
}
