package com.company.util;

import java.util.HashMap;
import java.util.Map;

public class ExceptionAssembly {
    private String messageDelimiter = ":";
    private final Map<String, String> errors = new HashMap<>();


    public ExceptionAssembly() {
    }

    public ExceptionAssembly(String messageDelimiter) {
        this.messageDelimiter = messageDelimiter;
    }

    public ExceptionAssembly executeAll(ExecuteCode... executeCodes) {


        for (ExecuteCode executeCode : executeCodes) {
            try {
                executeCode.execute();
            } catch (Exception e) {
                String[] tmp = e.getMessage().split(messageDelimiter);
                errors.put(tmp[0], tmp[1]);

            }
        }
        return this;
    }
    public Map<String,String> finish(){
        return errors;
    }
    public void throwIfNotEmpty() throws AssemblyException {
        if (!errors.isEmpty()){
            throw AssemblyException.multiple(errors);
        }
    }

}
