package com.klcxkj.zqxy.databean;

import java.util.List;

/**
 * autor:OFFICE-ADMIN
 * time:2018/10/30
 * email:yinjuan@klcxkj.com
 * description:
 */
public class KLRechargeBean {


    /**
     * data : [{"czprjid":"9999998","czid":255,"czvalue":"50"},{"czprjid":"9999998","czid":256,"czvalue":"100"},{"czprjid":"9999998","czid":257,"czvalue":"150"},{"czprjid":"9999998","czid":258,"czvalue":"200"},{"czprjid":"9999998","czid":259,"czvalue":"300"}]
     * error_code : 0
     * message : 获取成功
     */
    private List<DataEntity> data;
    private String error_code;
    private String message;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public String getError_code() {
        return error_code;
    }

    public String getMessage() {
        return message;
    }

    public class DataEntity {
        /**
         * czprjid : 9999998
         * czid : 255
         * czvalue : 50
         */
        private String czprjid;
        private int czid;
        private String czvalue;

        public void setCzprjid(String czprjid) {
            this.czprjid = czprjid;
        }

        public void setCzid(int czid) {
            this.czid = czid;
        }

        public void setCzvalue(String czvalue) {
            this.czvalue = czvalue;
        }

        public String getCzprjid() {
            return czprjid;
        }

        public int getCzid() {
            return czid;
        }

        public String getCzvalue() {
            return czvalue;
        }
    }
}
