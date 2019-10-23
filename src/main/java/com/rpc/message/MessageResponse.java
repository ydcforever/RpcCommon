package com.rpc.message;

import java.io.Serializable;

/**
 * Created by T440 on 2019/4/12.
 */
public class MessageResponse implements Serializable{

    private String requestId;

    private Throwable error;

    /**
     * 返回值
     */
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public MessageResponse() {

    }
}
