package com.flavourfit.ResponsesDTO;

import java.util.HashMap;
import java.util.Map;

public class PutResponse {
    private boolean success;
    private String message;
    private Map<String, Object> data;

    public PutResponse(boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public PutResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = new HashMap<>();
    }

    public boolean isSuccess() {
        return success;
    }


    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
