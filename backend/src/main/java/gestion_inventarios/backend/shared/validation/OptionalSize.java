package gestion_inventarios.backend.shared.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OptionalSizeValidator.class)
@Documented
public @interface OptionalSize {
    String message() default "El campo debe tener al menos {min} caracteres si se proporciona";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    int min() default 0;
    int max() default Integer.MAX_VALUE;
}

class OptionalSizeValidator implements ConstraintValidator<OptionalSize, String> {
    private int min;
    private int max;

    @Override
    public void initialize(OptionalSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Si es null o vacío, es válido (opcional)
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        // Si tiene contenido, validar tamaño
        return value.length() >= min && value.length() <= max;
    }
}
