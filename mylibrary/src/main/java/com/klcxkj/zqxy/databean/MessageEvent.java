package com.klcxkj.zqxy.databean;

/**
 * autor:OFFICE-ADMIN
 * time:2018/3/13
 * email:yinjuan@klcxkj.com
 * description:传递的事件类实体
 */

public class MessageEvent {

    private int orderPrice; //充值金额
    private String orderNo;  //充值的订单编号
    private String eventType;//事件类型


    private boolean b; //是不是本帐号结束用水。（true是自己结束，false是他人结束）
    private String timeid; //时间序号
    private int mproductid;//项目编号
    private int mdeviceid;//设备ID
    private int maccountid;//用户账号ID
    private String accounttypeString; //账户类型（1 管理员 2普通用户）
    private int usercount;//账户使用次数
    private String ykmoneyString; //预扣金额
    private String consumeMoneString;//消费金额
    private String rateString;//费率
    private String macString;//mac地址



    public MessageEvent() {

    }
    public MessageEvent(String eventType) {
        this.eventType = eventType;
    }

    public MessageEvent(String eventType, boolean b, String timeid, int mproductid, int mdeviceid, int maccountid,
                        String accounttypeString, int usercount, String ykmoneyString, String consumeMoneString,
                        String rateString, String macString) {
        this.eventType = eventType;
        this.b = b;
        this.timeid = timeid;
        this.mproductid = mproductid;
        this.mdeviceid = mdeviceid;
        this.maccountid = maccountid;
        this.accounttypeString = accounttypeString;
        this.usercount = usercount;
        this.ykmoneyString = ykmoneyString;
        this.consumeMoneString = consumeMoneString;
        this.rateString = rateString;
        this.macString = macString;
    }

    public MessageEvent(String eventType, int mproductid, int maccountid) {
        this.eventType = eventType;
        this.mproductid = mproductid;
        this.maccountid = maccountid;

    }

    public MessageEvent(String eventType, String timeid, int mproductid, int mdeviceid, int maccountid, String accounttypeString, int usercount, String ykmoneyString,
                        String rateString, String macString) {
        this.eventType = eventType;
        this.timeid = timeid;
        this.mproductid = mproductid;
        this.mdeviceid = mdeviceid;
        this.maccountid = maccountid;
        this.accounttypeString = accounttypeString;
        this.usercount = usercount;
        this.ykmoneyString = ykmoneyString;
        this.rateString = rateString;
        this.macString = macString;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public String getTimeid() {
        return timeid;
    }

    public void setTimeid(String timeid) {
        this.timeid = timeid;
    }

    public int getMproductid() {
        return mproductid;
    }

    public void setMproductid(int mproductid) {
        this.mproductid = mproductid;
    }

    public int getMdeviceid() {
        return mdeviceid;
    }

    public void setMdeviceid(int mdeviceid) {
        this.mdeviceid = mdeviceid;
    }

    public int getMaccountid() {
        return maccountid;
    }

    public void setMaccountid(int maccountid) {
        this.maccountid = maccountid;
    }

    public String getAccounttypeString() {
        return accounttypeString;
    }

    public void setAccounttypeString(String accounttypeString) {
        this.accounttypeString = accounttypeString;
    }

    public int getUsercount() {
        return usercount;
    }

    public void setUsercount(int usercount) {
        this.usercount = usercount;
    }

    public String getYkmoneyString() {
        return ykmoneyString;
    }

    public void setYkmoneyString(String ykmoneyString) {
        this.ykmoneyString = ykmoneyString;
    }

    public String getConsumeMoneString() {
        return consumeMoneString;
    }

    public void setConsumeMoneString(String consumeMoneString) {
        this.consumeMoneString = consumeMoneString;
    }

    public String getRateString() {
        return rateString;
    }

    public void setRateString(String rateString) {
        this.rateString = rateString;
    }

    public String getMacString() {
        return macString;
    }

    public void setMacString(String macString) {
        this.macString = macString;
    }
}
