package gestion_inventarios.backend.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import gestion_inventarios.backend.domain.exception.DuplicateCategoryException;
import gestion_inventarios.backend.domain.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 — entidad no encontrada
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(UserNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiError.of(404, "Not Found", ex.getMessage()));
    }

    // 409 — categoría duplicada
    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<ApiError> handleDuplicateCategory(DuplicateCategoryException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiError.of(409, "Conflict", ex.getMessage()));
    }

    // 400 — validaciones de @Valid fallidas
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(f -> f.getField() + ": " + f.getDefaultMessage())
            .findFirst()
            .orElse("Datos inválidos");

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError.of(400, "Bad Request", message));
    }

    // 400 — reglas de negocio violadas (ej: stock negativo)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError.of(400, "Bad Request", ex.getMessage()));
    }

    // 400 — body JSON malformado o ausente
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError.of(400, "Bad Request", "El cuerpo de la petición es inválido o está ausente"));
    }

    // 401 — credenciales incorrectas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiError.of(401, "Unauthorized", "Email o contraseña incorrectos"));
    }

    // 500 — cualquier otro error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError.of(500, "Internal Server Error", "Error inesperado en el servidor"));
    }
}
