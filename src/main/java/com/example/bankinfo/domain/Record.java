package com.example.bankinfo.domain;

public class Record {
    private Integer id;

    /**
     * 交易时间
     *
     * @mbggenerated
     */
    private String tradinghour;

    /**
     * 收支
     *
     * @mbggenerated
     */
    private Float incoming;

    /**
     * 余额
     *
     * @mbggenerated
     */
    private Float balance;

    /**
     * 交易类型
     *
     * @mbggenerated
     */
    private String tradetype;

    /**
     * 是否加币,0:未加币，1：已加币,2无法加币，100重复记录
     *
     * @mbggenerated
     */
    private Integer ishandle;

    private Integer uid;

    private String station;

    private String remark;

    private String order;

    private String token;

    /**
     * 0未统计，1已统计
     *
     * @mbggenerated
     */
    private Integer isSum;

    private String tableName;

    private Integer page;

    private Integer rows;

    private String gsite;

    private String bname;

    private int number;
    public Record(String tradinghour,String tableName) {
        this.tradinghour = tradinghour;
        this.tableName = tableName;
    }

    public Record(Integer ishandle,String tableName) {
        this.ishandle = ishandle;
        this.tableName = tableName;
    }
    public Record(String tableName,Integer isSum,String bname,String remark) {
        this.tableName = tableName;
        this.isSum = isSum;
        this.bname = bname;
        this.remark = remark;
    }

    public Record() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTradinghour() {
        return tradinghour;
    }

    public void setTradinghour(String tradinghour) {
        this.tradinghour = tradinghour == null ? null : tradinghour.trim();
    }

    public Float getIncoming() {
        return incoming;
    }

    public void setIncoming(Float incoming) {
        this.incoming = incoming;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public String getTradetype() {
        return tradetype;
    }

    public void setTradetype(String tradetype) {
        this.tradetype = tradetype == null ? null : tradetype.trim();
    }

    public Integer getIshandle() {
        return ishandle;
    }

    public void setIshandle(Integer ishandle) {
        this.ishandle = ishandle;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station == null ? null : station.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order == null ? null : order.trim();
    }

    public Integer getIsSum() {
        return isSum;
    }

    public void setIsSum(Integer isSum) {
        this.isSum = isSum;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public String getGsite() {
        return gsite;
    }

    public void setGsite(String gsite) {
        this.gsite = gsite;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "{" +
                "时间=" + tradinghour +
                ", 收支=" + incoming +
                ", 余额=" + balance +
                ", 交易类型=" + tradetype +
                ", 描述=" + remark  +
                "}";
    }
}