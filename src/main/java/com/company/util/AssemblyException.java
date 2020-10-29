package com.company.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AssemblyException extends Exception{
    private final Map<String, String> errors;

    private AssemblyException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public static AssemblyException singleton(String errorKey, String error){
        return new AssemblyException(Collections.singletonMap(errorKey, error));
    }
    public static AssemblyException multiple(Map<String, String> errors){
        return new AssemblyException(errors);
    }


}
