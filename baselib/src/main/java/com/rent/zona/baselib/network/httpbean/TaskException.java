package com.rent.zona.baselib.network.httpbean;

import com.google.gson.JsonSyntaxException;


public class TaskException extends Exception {
    private static final long serialVersionUID = 9106188399276740144L;
    public int status;
    public String desc;

    public TaskException(int statusCode, String desc) {
        this.status = statusCode;
        this.desc = desc;
    }

    public TaskException(String message, String desc) {
        super(message);
        this.desc = desc;
    }

    @Override
    public String getMessage() {
        return toString();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TaskException{");
        sb.append("status_code=").append(status);
        sb.append(", desc='").append(desc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

