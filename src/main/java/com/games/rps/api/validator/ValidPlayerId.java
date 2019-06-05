package com.games.rps.api.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Size(max = 20)
public @interface ValidPlayerId {
    String message() default "Player id length must be not longer than 20 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
