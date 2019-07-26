package com.example.bankinfo.common.single;

import com.example.bankinfo.common.HttpClientUtil;
import com.example.bankinfo.common.SpringUtil;
import com.example.bankinfo.domain.BankConfiguration;
import com.example.bankinfo.domain.Record;
import com.example.bankinfo.service.RecordService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.thymeleaf.util.StringUtils;

/**
 * 中信银行 ---单例类
 */
public class ZxSingleton {
    /**
     * 创建ZxSingleton 的一个对象
     */
    public static ZxSingleton instance = new ZxSingleton();


    /**
     * 让构造函数为 private，这样该类就不会被实例化
     */
    private ZxSingleton(){

    }

    /**
     * 获取唯一可用的对象
     */
    public static ZxSingleton getInstance(){
        if (instance ==null){
            synchronized (ZxSingleton.class){
                if (instance ==null){
                    instance = new ZxSingleton();
                }
            }
        }
        return  instance;
    }


    public void ZxRun(BankConfiguration bankConfiguration, String tableName, RecordService recordService) {

        //银行请求获取得到HTML字符数据
        String result = HttpClientUtil.htRequest(bankConfiguration.getUrl(),"POST",bankConfiguration.getParam(),bankConfiguration.getCharsetname(),bankConfiguration.getCookie());
        //将银行HTML数据转换成Document处理
        Document document = Jsoup.parse(result);

        Elements masthead = document.select("table").select("tbody").select("tr");

        //循环银行取得TR标签数据
        for (int i = 0; i < masthead.size()-1; i++) {
            Element el = masthead.get(i);

            //获取当前遍历中td元素数据
            Elements e_td = el.select("td");
            //获取已设置好的部分银行数据
            Record record = setBankInfo(e_td,tableName);
            //保存已抓取的银行完整数据
            record.setToken(SpringUtil.getToken());
            recordService.insertSelective(record);
        }



    }

    private Record setBankInfo(Elements e_td, String tableName) {
        Record record =new Record();
        String re = null;
        for (int i = 0; i < e_td.size(); i++) {
            //详细交易时间
            record.setTradinghour(e_td.get(0).text());
            String incom = null;
            String balance = null;

            if (e_td.get(1).text().contains("--")){
                incom = "-" + e_td.get(2).text();
                if (incom.contains("--")) {

                    incom = incom.replaceAll("--","-");
                }
            }else{
                incom = e_td.get(1).text();
            }

            if (incom.contains(",")){
                incom = incom.replace(",", "");
            }
            //收入金额
            record.setIncoming(Float.valueOf(incom));

            re = e_td.get(6).text();
            if (re.length()>1){
                if (e_td.get(4).text() == null || StringUtils.isEmpty(e_td.get(4).text())){
                    record.setRemark(re);
                }else {
                    if (e_td.get(4).text().contains("支付宝")){
                        re = re.substring(0,re.indexOf("支付宝转账"));
                        if (re.contains("银联入账")){
                            re = re.substring(5);
                        }
                        record.setRemark(re);
                        //record.setRemark(re.substring(0,re.indexOf("支付宝转账")));
                    }else{
                        //转账联系人
                        record.setRemark(e_td.get(4).text());
                    }
                }
            }else{
                record.setRemark(e_td.get(4).text());
            }


            balance = e_td.get(3).text();
            if (balance.contains(",")){
                balance = balance.replace(",", "").replaceAll(" ", "");
            }
            balance = balance.replaceAll(" ", "");
            //余额
            record.setBalance(Float.valueOf(balance));

        }
        //数据库表名
        record.setTableName(tableName);

        return record;
    }
}
