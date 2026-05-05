package gestion_inventarios.backend.infrastructure.in.rest.product.rest;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import gestion_inventarios.backend.application.ports.in.category.FindCategoriesUseCase;
import gestion_inventarios.backend.application.ports.in.product.ProductUseCase;
import gestion_inventarios.backend.domain.model.products.Product;
import gestion_inventarios.backend.domain.model.products.ProductPrice;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.in.rest.product.dto.ProductCreateRequest;
import gestion_inventarios.backend.infrastructure.in.rest.product.dto.ProductExistResponse;
import gestion_inventarios.backend.infrastructure.in.rest.product.dto.ProductResponse;
import gestion_inventarios.backend.infrastructure.out.storage.FileStorageAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductUseCase productUseCase;
    private final FileStorageAdapter fileStorageAdapter;
    private final FindCategoriesUseCase categoryUseCase;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<PageResult<ProductResponse>> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResult<Product> result = productUseCase.findAll(new PageRequest(page, size));

        PageResult<ProductResponse> response = PageResult.of(
                result.content().stream().map(ProductResponse::from).toList(),
                result.page(), result.size(), result.totalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<PageResult<ProductResponse>> search(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam String search) {
        PageResult<Product> result = productUseCase.search(new PageRequest(page, size), search);

        PageResult<ProductResponse> response = PageResult.of(
                result.content().stream().map(ProductResponse::from).toList(),
                result.page(), result.size(), result.totalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("category/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<PageResult<ProductResponse>> getByCategory(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @PathVariable Long id) {
        PageResult<Product> result = productUseCase.findByCategoryId(id, new PageRequest(page, size));

        PageResult<ProductResponse> response = PageResult.of(
                result.content().stream().map(ProductResponse::from).toList(),
                result.page(), result.size(), result.totalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        Product product = productUseCase.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProductResponse.from(product));
    }

     @GetMapping("/sku/{sku}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<ProductExistResponse> getBySku(@PathVariable String sku) {
        try {
            productUseCase.findBySku(sku);
            return ResponseEntity.ok(ProductExistResponse.from(true));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ProductExistResponse.from(false));
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ProductResponse> create(@Valid @ModelAttribute ProductCreateRequest request) {

        String imageUrl = null;
        String storedFilename = null;

        if (request.image() != null && !request.image().isEmpty()) {
            storedFilename = fileStorageAdapter.saveFile(request.image(), "products");
            imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/images/products/")
                    .path(storedFilename)
                    .toUriString();
        }

        try {

            ProductPrice productPrice = new ProductPrice(
                    request.price(),
                    Currency.getInstance(request.currency()));

            var category = categoryUseCase.findById(request.category());


            Product newProduct = new Product(
                null,                  
                request.sku(),         
                request.name(),        
                request.description(), 
                category,              
                request.brand(),       
                request.model(),       
                request.color(),       
                request.size(),        
                imageUrl != null ? imageUrl : "",
                request.quantity(),    
                productPrice,          
                true,          
                LocalDateTime.now(),   
                LocalDateTime.now()    
        );

            Product savedProduct = productUseCase.save(newProduct);
            return ResponseEntity.ok(ProductResponse.from(savedProduct));
        } catch (RuntimeException e) {
            // Si ocurre un error al guardar el producto, eliminamos la imagen que se subió
            if (storedFilename != null) {
                fileStorageAdapter.deleteFile(storedFilename);
            }
            throw e; // Re-lanzamos la excepción para que el cliente reciba el error
        }

    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id, @Valid @ModelAttribute ProductCreateRequest request) {
        
        Product existingProduct = productUseCase.findById(id);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        String imageUrl = existingProduct.getImage();
        if (request.image() != null && !request.image().isEmpty()) {
            String storedFilename = fileStorageAdapter.saveFile(request.image(), "products");
            imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/images/products/")
                    .path(storedFilename)
                    .toUriString();
        }

        var category = categoryUseCase.findById(request.category());
        ProductPrice price = new ProductPrice(request.price(), Currency.getInstance(request.currency()));

        existingProduct.updateSku(request.sku());
        existingProduct.updateName(request.name());
        existingProduct.updateDescription(request.description());
        existingProduct.updateBrand(request.brand());
        existingProduct.updateModel(request.model());
        existingProduct.updateColor(request.color());
        existingProduct.updateSize(request.size());
        existingProduct.updateQuantity(request.quantity());
        existingProduct.updatePrice(price);
        existingProduct.updateCategory(category);
        existingProduct.updateImage(imageUrl);

        Product updatedProduct = productUseCase.save(existingProduct);
        return ResponseEntity.ok(ProductResponse.from(updatedProduct));
    }

}
