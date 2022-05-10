package com.company.controller;

import com.company.dto.CategoryDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.CategoryService;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody @Valid CategoryDTO dto, HttpServletRequest request) {

        return ResponseEntity.ok(categoryService.create(dto, JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN)));
    }

    @GetMapping("/adm/pagination")
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "3") int size) {
        return ResponseEntity.ok(categoryService.getList(page, size));
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @GetMapping("/public/list/{lang}")
    public ResponseEntity<?> getList(@PathVariable("lang") LangEnum lang) {
        return ResponseEntity.ok(categoryService.getRegionList(lang));
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody @Valid CategoryDTO dto) {
        log.info("updated : {}", dto);
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @DeleteMapping("/adm/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        log.info("deleted : {}", id);
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
