package com.klcxkj.zqxy.response;

import java.io.Serializable;

/**
 * autor:OFFICE-ADMIN
 * time:2018/7/27
 * email:yinjuan@klcxkj.com
 * description:
 */

public class RechargeEntity implements Serializable {


    /**
     * msg : 获取成功
     * thirdorderid : 201807271735137407282100
     * RechargeWay : 100
     * sign : I6dIvIBwwax5G9IrfW5Sgg3tPW%2Bw2p8%2FNxlASk%2FDORZUeEoLXyDrBKHzjTsjB5FFbHAg6QZ%2BOxEVaWlpJbvm8yBmN9838lUO9zGHD5ffXmJUPIjKCCXdeIMuCxm28QG2GIzdLxobHXmfCmDIiQASP5BnhfmgfuqFfL7wDy%2BmcZM%3D
     * toaccount : 1000060
     * thirdsystem : klwater
     * orderdesc : 新中新凯路对接充值
     * sno : 22
     * success : true
     * tranamt : 5000
     * thirdurl :
     * error_code : 0
     * praram1 : 学工号：22账号：22充值：5000
     * account : 18565651403
     * ordertype : phone
     */
    private String msg;
    private String thirdorderid;
    private String RechargeWay;
    private String sign;
    private String toaccount;
    private String thirdsystem;
    private String orderdesc;
    private String sno;
    private String success;
    private int tranamt;
    private String thirdurl;
    private String error_code;
    private String praram1;
    private String account;
    private String ordertype;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setThirdorderid(String thirdorderid) {
        this.thirdorderid = thirdorderid;
    }

    public void setRechargeWay(String RechargeWay) {
        this.RechargeWay = RechargeWay;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setToaccount(String toaccount) {
        this.toaccount = toaccount;
    }

    public void setThirdsystem(String thirdsystem) {
        this.thirdsystem = thirdsystem;
    }

    public void setOrderdesc(String orderdesc) {
        this.orderdesc = orderdesc;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setTranamt(int tranamt) {
        this.tranamt = tranamt;
    }

    public void setThirdurl(String thirdurl) {
        this.thirdurl = thirdurl;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setPraram1(String praram1) {
        this.praram1 = praram1;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getMsg() {
        return msg;
    }

    public String getThirdorderid() {
        return thirdorderid;
    }

    public String getRechargeWay() {
        return RechargeWay;
    }

    public String getSign() {
        return sign;
    }

    public String getToaccount() {
        return toaccount;
    }

    public String getThirdsystem() {
        return thirdsystem;
    }

    public String getOrderdesc() {
        return orderdesc;
    }

    public String getSno() {
        return sno;
    }

    public String getSuccess() {
        return success;
    }

    public int getTranamt() {
        return tranamt;
    }

    public String getThirdurl() {
        return thirdurl;
    }

    public String getError_code() {
        return error_code;
    }

    public String getPraram1() {
        return praram1;
    }

    public String getAccount() {
        return account;
    }

    public String getOrdertype() {
        return ordertype;
    }
}
