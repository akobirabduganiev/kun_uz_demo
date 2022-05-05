package com.company.repository;

import com.company.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllByArticleId(Integer articleId, Pageable pageable);

    Page<CommentEntity> findAllByProfileId(Integer profile, Pageable pageable);

}
