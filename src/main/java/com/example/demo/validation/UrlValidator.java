package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UrlValidator implements ConstraintValidator<Url,String> {
    private static final String URL_PATTERN = "^(http|https)://.*$";

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        return url != null && Pattern.compile(URL_PATTERN).matcher(url).matches();
    }
}
