package com.cledson_leite.library_api.api.exceptions;

import com.cledson_leite.library_api.exceptions.BusinessException;
import org.springframework.validation.BindingResult;

import java.util.*;

public class AppErrors {
    private List<String> errors;
    public AppErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<String>() ;
        bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
    }

    public AppErrors(BusinessException exception) {
        this.errors = new ArrayList<String>() ;
        this.errors.add(exception.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}

