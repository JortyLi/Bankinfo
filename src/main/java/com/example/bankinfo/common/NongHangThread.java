package com.example.bankinfo.common;

import com.example.bankinfo.BankInfoApplication;
import com.example.bankinfo.domain.BankConfiguration;
import com.example.bankinfo.domain.Record;
import com.example.bankinfo.service.RecordService;
import javazoom.jl.player.Player;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class NongHangThread implements Runnable {

    /*当前银行*/
    private BankConfiguration bankConfiguration;
    private RecordService recordService;
    private String url;
    private String tableName;

    public NongHangThread(BankConfiguration bankConfiguration, RecordService recordService, String url, String tableName) {
        this.bankConfiguration = bankConfiguration;
        this.recordService = recordService;
        this.url = url;
        this.tableName = tableName;
    }

    @Override
    public void run() {
        //连接银行接口得到返回数据
        String nongHangResult = HttpClientUtil.httpsRequest(bankConfiguration.getUrl(), "POST", bankConfiguration.getParam(), bankConfiguration.getCharsetname(), bankConfiguration.getCookie());
        if (!nongHangResult.contains("公共错误页面")) {
            /*银行数据信息获取是否失败*/
            if (bankInfoGetError(nongHangResult)) {
                new MP3Player(url).play();
            } else {
                //根据对应银行获取该处理后的数据
                List<Record> nongHangBankInfos = getNongHangInfo(nongHangResult);
                //将数据加入本系统数据库
                addBankInfo(nongHangBankInfos);
            }
        }
    }

    /**
     * 银行数据信息获取失败
     *
     * @param result 结果集
     * @return boolean true 失败 --- false 成功
     */
    private boolean bankInfoGetError(String result) {
        /*根据返回辨别是否失败--农行--工商--招商--建行*/
        if (result.contains("会话超时") || result.contains("跳转页面") || result.contains("安全隐患") || result.contains("errorCode")) {
            System.out.println("------------抓取" + bankConfiguration.getBankName() + "异常，请【重新】登录银行、【重新】更新配置！！！！！！！！！");
            if(BankInfoApplication.IS_START)
                BankInfoApplication.IS_START = false;
            if(BankInfoApplication.IS_START1)
                BankInfoApplication.IS_START1 = false;
            return true;
        } else
            return false;
    }

    /**
     * 农行收支信息获取
     *
     * @param result 接口返回数据
     * @return List<Record> 银行信息合集
     */
    public List<Record> getNongHangInfo(String result) {
        List<Record> records = new ArrayList<Record>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(result);
            List<String> list = (List<String>) jsonObject.get("table");

            for (int i = 0; i < list.size(); i++) {
                JSONArray jsonArray = JSONArray.fromObject(String.valueOf(list.get(i)));
                Record record = new Record();
                int e = 1;
                for (int j = 0; j < jsonArray.size(); j++) {
                    record.setTradinghour(jsonArray.get(j).toString() + " " + jsonArray.get(j + e++).toString());
                    if ("".equals(jsonArray.get(jsonArray.size() - 1)))
                        record.setIncoming(Float.valueOf(jsonArray.get(jsonArray.size() - 2).toString()));
                    else
                        record.setIncoming(Float.valueOf("-" + jsonArray.get(jsonArray.size() - 1).toString()));
                    e++;
                    if (jsonArray.get(j + e).toString().contains(",")) {
                        record.setBalance(Float.valueOf(jsonArray.get(j + e++).toString().replaceAll(",", "")));
                    } else {
                        record.setBalance(Float.valueOf(jsonArray.get(j + e++).toString()));
                    }
                    if (jsonArray.get(j + 4).toString().contains("支付宝")){
                        String remark = jsonArray.get(j + 4).toString();
                        record.setRemark(remark);
                    }else {
                        record.setRemark(jsonArray.get(j + 4).toString());
                    }
                    records.add(record);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * 截取字段
     * @param remark
     * @return
     */
    private String toChar(String remark) {
        String s =null;
        if (remark.length()<=15){
            s = remark.substring(0, 2);
        }else{
            s = remark.substring(0,3);
        }
        return s;
    }

    /**
     * 添加数据
     *
     * @param bankInfos
     */
    private void addBankInfo(List<Record> bankInfos) {
        for (int i = 0; i < bankInfos.size(); i++) {
            Record record = bankInfos.get(i);
            record.setToken(SpringUtil.getToken());
            record.setTableName(tableName);
            recordService.insertSelective(record);
        }
    }
}
