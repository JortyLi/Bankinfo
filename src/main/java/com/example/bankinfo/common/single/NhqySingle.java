package com.example.bankinfo.common.single;

import com.example.bankinfo.common.DateConvert;
import com.example.bankinfo.common.HttpClientUtil;
import com.example.bankinfo.common.SpringUtil;
import com.example.bankinfo.domain.BankConfiguration;
import com.example.bankinfo.domain.Record;
import com.example.bankinfo.service.RecordService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 农行企业银行-单例类
 */
public class NhqySingle {
    /**
     * 创建 BjrcbSingle 的一个对象
     */
    private static NhqySingle instance = new NhqySingle();

    /**
     * 让构造函数为 private，这样该类就不会被实例化
     */
    private NhqySingle() {
    }

    /**
     * 获取唯一可用的对象
     */
    public static NhqySingle getInstance() {
        if (instance == null) {
            synchronized (BjrcbSingle.class) {
                if (instance == null) {
                    instance = new NhqySingle();
                }
            }
        }
        return instance;
    }

    /**
     * 银行记录server
     */
    RecordService recordService;



    public void NhqyRun(BankConfiguration bankConfiguration, String tableName, RecordService recordService) {
        //银行请求得到HTML字符数据
        String result = HttpClientUtil.htRequest(bankConfiguration.getUrl(), "POST", bankConfiguration.getParam(), bankConfiguration.getCharsetname(), bankConfiguration.getCookie());
        //将银行html数据转换成Document处理
        Elements masthead = Jsoup.parse(result).select("table[class= e-table e-table-mouse e-table-mouse-tradeuse]").select("tr");

        //循环银行取得TR标签数据
        for (int i = 1; i < masthead.size()-1; i++) {
            Element element = masthead.get(i);
            //获取当前遍历中td元素数据
            Elements e_td = element.select("td");
            //获取已设置好的部分银行数据
            Record record = setBankInfo(e_td, tableName);
            record.setToken(SpringUtil.getToken());
            //保存已抓取的银行完整数据
            recordService.insertSelective(record);

        }
    }

    /**
     * 设置取得对应银行记录对应的数据{日期（yyyy-MM-dd）、收支、余额、交易类型、对方姓名}
     *
     * @param e_td      td元素数据
     * @param tableName 存入的数据库表名
     */
    private Record setBankInfo(Elements e_td, String tableName) {
        //银行记录接收类
        Record record = new Record();
        //日期
        String dateTime = DateConvert.convertString(DateConvert.convertStringToDate(e_td.get(0).text()),"yyyy-MM-dd HH:mm:ss");
        record.setTradinghour(dateTime);
        //收支
        String sr = e_td.get(1).text();
        String zc = e_td.get(2).text();
        Float incoming = ("\\".equals(sr)) ? Float.valueOf("-"+zc) : Float.valueOf(sr);
        record.setIncoming(incoming);
        //余额
        Float balance = Float.valueOf(e_td.get(3).text());
        record.setBalance(balance);
        //交易类型
        String tradeType = e_td.get(8).text().trim();
        record.setTradetype(tradeType);
        //对方姓名
        String remark = e_td.get(7).text().trim();
        record.setRemark(remark);
        //表名
        record.setTableName(tableName);
        return record;
    }

    public static void main(String[] args) {
//        String result = "";
//
//        //将银行html数据转换成Document处理
//        Elements masthead = Jsoup.parse(result).select("table[class= e-table e-table-mouse e-table-mouse-tradeuse]").select("tr");
//
//
//        Elements e_td = masthead.get(3).select("td");
String str = "支付宝-王兵辉支付宝转账-王兵辉支付宝转账";
        System.out.println(str.split("-")[1].replaceAll("支付宝转账",""));
//
//        System.out.println(e_td.toString());
//        //银行记录接收类
//        Record record = new Record();
//        //时间
//        String dateTime = DateConvert.convertString(DateConvert.convertStringToDate(e_td.get(0).text()),"yyyy-MM-dd HH:mm:ss");
//        record.setTradinghour(dateTime);
//        //收支
//        String sr = e_td.get(1).text();
//        String zc = e_td.get(2).text();
//        Float incoming = ("\\".equals(sr)) ? Float.valueOf("-"+zc) : Float.valueOf(sr);
//        record.setIncoming(incoming);
//        //余额
//        Float balance = Float.valueOf(e_td.get(3).text());
//        record.setBalance(balance);
//        //交易类型
//        String tradeType = e_td.get(8).text().trim();
//        record.setTradetype(tradeType);
//        //对方姓名
//        String remark = e_td.get(7).text().trim();
//        record.setRemark(remark);
//
//        System.out.println(record.toString());

    }

}
