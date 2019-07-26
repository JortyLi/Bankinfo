package com.example.bankinfo.domain;

public class BankLogin {
    private Integer id;

    /**
     * 银行名称
     *
     * @mbggenerated
     */
    private String bankName;

    /**
     * 用户名
     *
     * @mbggenerated
     */
    private String userName;

    /**
     * 登陆名称
     *
     * @mbggenerated
     */
    private String loginName;

    /**
     * 登陆密码
     *
     * @mbggenerated
     */
    private String loginPass;

    /**
     * 银行状态
     *
     * @mbggenerated
     */
    private Integer bankState;

    /**
     * 银行ID
     *
     * @mbggenerated
     */
    private Integer bankConfid;

    private Integer page;

    private Integer rows;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass == null ? null : loginPass.trim();
    }

    public Integer getBankState() {
        return bankState;
    }

    public void setBankState(Integer bankState) {
        this.bankState = bankState;
    }

    public Integer getBankConfid() {
        return bankConfid;
    }

    public void setBankConfid(Integer bankConfid) {
        this.bankConfid = bankConfid;
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

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}