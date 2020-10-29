package com.company.util;

public class Msg {

    private final int code;
    private final String description;
    private Object content;


    private Msg(int code, String description, Object content) {
        this.code = code;
        this.description = description;
        this.content = content;
    }

    public Msg(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Msg newSuccessMsg(){
        return new Msg(0, "success");
    }
    public static Msg newSuccessMsg(Object content){
        return new Msg(0, "success", content);
    }
    public static Msg newFailedMsg(int code, String description){
        return new Msg(code, description);
    }
    public static Msg newFailedMsg(int code, String description, Object content){
        return new Msg(code, description, content);
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Object getContent() {
        return content;
    }
}
