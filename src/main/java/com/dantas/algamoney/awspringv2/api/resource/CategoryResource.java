package com.dantas.algamoney.awspringv2.api.resource;

import com.dantas.algamoney.awspringv2.api.model.Category;
import com.dantas.algamoney.awspringv2.api.repository.CategoryRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    @Autowired
    private CategoryRepository repository;

    @GetMapping()
    public ResponseEntity<List<Category>> listAllCategories(){
        List<Category> categories = repository.findAll();
        return categories.isEmpty() ?ResponseEntity.noContent().build() : ResponseEntity.ok(categories);

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> findCategoryById(@PathVariable Long categoryId){
        Category category = repository.findById(categoryId).orElse(null);
        return Objects.isNull(category) ? ResponseEntity.notFound().build() : ResponseEntity.ok(category);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void insertCategory(@Valid @RequestBody Category category, HttpServletResponse response){
        Category categoryCreated = repository.save(category);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryCreated.getId())
                .toUri();

        response.setHeader("Location", uri.toASCIIString());
    }
}
