package gestion_inventarios.backend.infrastructure.in.rest.category.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestion_inventarios.backend.application.ports.in.DeleteCategoryUseCase;
import gestion_inventarios.backend.application.ports.in.FindCategoriesUseCase;
import gestion_inventarios.backend.application.ports.in.FindSearchCategoriesUseCase;
import gestion_inventarios.backend.application.ports.in.SaveCategoryUseCase;
import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.in.rest.category.dto.CategoryRequest;
import gestion_inventarios.backend.infrastructure.in.rest.category.dto.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final FindCategoriesUseCase findCategoriesUseCase;
    private final FindSearchCategoriesUseCase findSearchCategoriesUseCase;
    private final SaveCategoryUseCase saveCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @GetMapping
    public ResponseEntity<PageResult<CategoryResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResult<Category> result = findCategoriesUseCase.findAll(new PageRequest(page, size));

        PageResult<CategoryResponse> response = PageResult.of(
                result.content().stream().map(CategoryResponse::from).toList(),
                result.page(), result.size(), result.totalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        Category category = findCategoriesUseCase.findById(id);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResult<CategoryResponse>> search(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "q", required=true, defaultValue = "") String search) {
        PageResult<Category> result = findSearchCategoriesUseCase.search(new PageRequest(page, size), search);

        PageResult<CategoryResponse> response = PageResult.of(
                result.content().stream().map(CategoryResponse::from).toList(),
                result.page(), result.size(), result.totalElements());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryRequest request) {
        // Para crear, el id debe ser null
        request.setId(null);
        Category category = saveCategoryUseCase.save(request);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody @Valid CategoryRequest request) {
        request.setId(id);
        Category category = saveCategoryUseCase.save(request);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteCategoryUseCase.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
