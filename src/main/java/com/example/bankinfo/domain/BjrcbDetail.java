package com.example.bankinfo.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class BjrcbDetail {


    private String amount="";
    private String trsDate="";
    private String acNo2="";
    private String acName2="";
    private String msgCode="";
    private String channel="";
    private String hostJnlNo="";
    private String dCFlag="";
    private String postscript_L="";
    private String note_L="";
    private String trsTime="";
    private String acNo="";
    private String acName="";
    private String otherBranchNo="";


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTrsDate() {
        return trsDate;
    }

    public void setTrsDate(String trsDate) {
        this.trsDate = trsDate;
    }

    public String getAcNo2() {
        return acNo2;
    }

    public void setAcNo2(String acNo2) {
        this.acNo2 = acNo2;
    }

    public String getAcName2() {
        return acName2;
    }

    public void setAcName2(String acName2) {
        this.acName2 = acName2;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getHostJnlNo() {
        return hostJnlNo;
    }

    public void setHostJnlNo(String hostJnlNo) {
        this.hostJnlNo = hostJnlNo;
    }

    public String getdCFlag() {
        return dCFlag;
    }

    public void setdCFlag(String dCFlag) {
        this.dCFlag = dCFlag;
    }

    public String getPostscript_L() {
        return postscript_L;
    }

    public void setPostscript_L(String postscript_L) {
        this.postscript_L = postscript_L;
    }

    public String getNote_L() {
        return note_L;
    }

    public void setNote_L(String note_L) {
        this.note_L = note_L;
    }

    public String getTrsTime() {
        return trsTime;
    }

    public void setTrsTime(String trsTime) {
        this.trsTime = trsTime;
    }

    public String getAcNo() {
        return acNo;
    }

    public void setAcNo(String acNo) {
        this.acNo = acNo;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getOtherBranchNo() {
        return otherBranchNo;
    }

    public void setOtherBranchNo(String otherBranchNo) {
        this.otherBranchNo = otherBranchNo;
    }

    public String toDetailParam() {
        String result = null;
        try {
            result = "Amount=" + amount +
                    "&TrsDate=" + trsDate +
                    "&AcNo2=" + acNo2 +
                    "&AcName2=" + URLEncoder.encode(acName2, "GBK") +
                    "&MsgCode=" + URLEncoder.encode(msgCode, "GBK") +
                    "&Channel=" + channel +
                    "&HostJnlNo=" + hostJnlNo +
                    "&DCFlag=" + dCFlag +
                    "&Postscript_L=" + postscript_L +
                    "&Note_L=" + note_L +
                    "&TrsTime=" + trsTime +
                    "&AcNo=" + acNo +
                    "&AcName=" + URLEncoder.encode(acName, "GBK") +
                    "&OtherBranchNo=" + otherBranchNo;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
