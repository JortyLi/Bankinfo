package com.example.bankinfo.common;

import com.example.bankinfo.BankInfoApplication;
import com.example.bankinfo.domain.BankConfiguration;
import com.example.bankinfo.domain.Record;
import com.example.bankinfo.service.RecordService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class XingYeYinHangThread implements Runnable {

    /*当前银行*/
    private BankConfiguration bankConfiguration;
    private RecordService recordService;
    private String url;
    private String tableName;

    public XingYeYinHangThread(BankConfiguration bankConfiguration, RecordService recordService, String url, String tableName) {
        this.bankConfiguration = bankConfiguration;
        this.recordService = recordService;
        this.url = url;
        this.tableName = tableName;
    }

    @Override
    public void run() {
        String xingYeOutResult = HttpClientUtil.httpsRequest(bankConfiguration.getUrl(), "POST", bankConfiguration.getParam(), bankConfiguration.getCharsetname(), bankConfiguration.getCookie());
        if (!bankInfoGetError(xingYeOutResult)) {
            /*银行数据信息获取是否失败*/
            if (bankInfoGetError(xingYeOutResult)) {
                new MP3Player(url).play();
            } else {
                //根据对应银行获取该处理后的数据
                List<Record> nongHangBankInfos = getXingyeInfo(xingYeOutResult);
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
        /*根据返回辨别是否失败--兴业银行*/
        if (result.contains("会话超时") || result.contains("跳转页面") || "".equals(result) || result.contains("服务器超时，请重试！") || result.contains("安全隐患") || result.toString().contains("会话已失效") || result.contains("统一报错")) {
            System.out.println("------------抓取" + bankConfiguration.getBankName() + "异常，请【重新】登录银行、【重新】更新配置！！！！！！！！！");
            if (BankInfoApplication.IS_START)
                BankInfoApplication.IS_START = false;
            if (BankInfoApplication.IS_START1)
                BankInfoApplication.IS_START1 = false;
            return true;
        }
        return false;
    }

    /**
     * 兴业银行
     *
     * @param result 接口返回数据
     * @return List<Record> 银行信息合集
     */
    public List<Record> getXingyeInfo(String result) {
        JSONArray jsoar = JSONObject.fromObject(result).getJSONArray("rows");
        List<Record> records = new ArrayList<>();
        String rec = null;
        for (int i = 0; i < jsoar.size(); i++) {
            Record re = new Record();
            JSONArray jsarr = jsoar.getJSONObject(i).getJSONArray("cell");
            for (int j = 0; j < jsarr.size(); j++) {
                re.setTradinghour(jsarr.getString(0));
                if ("".equals(jsarr.getString(2))) {

                    String money = jsarr.getString(3);

                    re.setIncoming(Float.valueOf(getMoney(money)));

                } else {
                    String money = getMoney(jsarr.getString(2));
                    if (!money.contains("-")) {
                        re.setIncoming(Float.valueOf("-" + money));
                    }else { re.setIncoming(Float.valueOf(money));}
                }

                re.setBalance(Float.valueOf(getMoney(jsarr.getString(4))));
                rec = jsarr.getString(6);
                if (!"".equals(jsarr.getString(6))) {
                    if (rec.contains("支付宝")) {
                        re.setRemark(jsarr.getString(9).substring(0, jsarr.getString(9).indexOf("支付宝转账")));
                    } else {
                        re.setRemark(rec + "-" + jsarr.getString(5));
                    }
                } else {
                    re.setRemark(jsarr.getString(5));
                }
                break;
            }
            records.add(re);
        }
        return records;
    }

    /**
     * 截取带逗号字符串，转float
     *
     * @param money
     * @return
     */
    public String getMoney(String money) {
        String reMoney = null;
        if (money != null) {
            if (money.contains(",")) {
                reMoney = money.replace(",", "");
                return reMoney;
            }
            return money;
        }
        return null;
    }


    /**
     * 添加数据
     *
     * @param bankInfos
     */
    private void addBankInfo(List<Record> bankInfos) {
        String bankName = bankConfiguration.getBankName();
        if ("兴业银行".equals(bankName)) {
            bankInfos = removeDuplicate(bankInfos);
        }
        for(Record record:bankInfos){
            record.setTableName(tableName);
            record.setToken(SpringUtil.getToken());
            recordService.insertSelective(record);
        }
    }

    /**
     * 兴业去重
     *
     * @param list
     * @return
     */
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
