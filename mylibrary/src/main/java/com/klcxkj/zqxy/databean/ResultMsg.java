package com.klcxkj.zqxy.databean;

/**
 * autor:OFFICE-ADMIN
 * time:2017/12/18
 * email:yinjuan@klcxkj.com
 * description:
 */

public class ResultMsg {


    /**
     * error_code : 3
     * message : 今天已经预约了一次
     */
    private String error_code;
    private String message;

    private String success;
    private String msg;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError_code() {
        return error_code;
    }

    public String getMessage() {
        return message;
    }
}
