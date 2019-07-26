package com.example.bankinfo;

import com.example.bankinfo.common.*;
import com.example.bankinfo.common.single.BjrcbSingle;
import com.example.bankinfo.common.single.GsSingle;
import com.example.bankinfo.common.single.NhqySingle;
import com.example.bankinfo.common.single.ZxSingleton;
import com.example.bankinfo.domain.BankConfiguration;
import com.example.bankinfo.domain.BankLogin;
import com.example.bankinfo.domain.Record;
import com.example.bankinfo.service.BankConfigurationService;
import com.example.bankinfo.service.BankLoginService;
import com.example.bankinfo.service.RecordService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.juli.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.logging.Level;

@Controller
@EnableScheduling
@SpringBootApplication
public class BankInfoApplication {

    /*银行配置1表名*/
    public static final String BANK_CONFIG_TABLE_NAME = "bank_configuration";
    /*银行配置2表名*/
    public static final String BANK_CONFIG_TABLE_NAME_2 = "bank_configuration_copy1";

    /*银行配置1表名*/
    public static final String BANK_RECORD_TABLE_NAME = "record";
    /*银行配置2表名*/
    public static final String BANK_RECORD_TABLE_NAME_2 = "record_copy1";

    /*定时开关*/
    public static boolean IS_START = false;
    /*第二个定时开关*/
    public static boolean IS_START1 = false;
    /*暂停开关*/
    public static boolean IS_STOP_START = true;


    /*当前银行*/
    public static BankConfiguration bankConfiguration;
    /*第二个当前银行*/
    public static BankConfiguration bankConfiguration1;

    @Autowired
    BankConfigurationService service;

    @Autowired
    RecordService recordService;

    @Autowired
    BankLoginService bankLoginService;
    static WebClient webclient = null;

    static HtmlPage hpage = null;
    @Value("${bank.yin.yue.url}")
    public String url;

    @Value("${mp3_url}")
    public String mp3URL;

    @Value("${puFaDo}")
    public String yinghUrl;

    @Value("${jb.station.sumMonye}")
    public String showSumMonye;

    @Value("${station}")
    public String currentStation;

    public static Float daySum;

    public static Float daySum1;


    /*当天开始时间*/
    public static String startTime;

    /*当天结束时间*/
    public static String endTime;


    public Integer number = 0;
    public Integer pnumber = 0;
    public Integer pnumber1 = 0;

    public static String startDate;
    public static String startDate1;

    public static String loginName;
    public static String loginPass;

    @RequestMapping("/")
    public ModelAndView index() { return new ModelAndView("index"); }

    @RequestMapping("/recordList")
    public ModelAndView recordList() {
        ModelAndView modelAndView = new ModelAndView("bank_record");
        if (IS_START) {
            modelAndView.addObject("bankName", bankConfiguration.getBankName());
            modelAndView.addObject("startDate", startDate);
        }
        if (IS_START1) {
            modelAndView.addObject("bankName1", bankConfiguration1.getBankName());
            modelAndView.addObject("startDate1", startDate1);
        }
        return modelAndView;
    }

    @RequestMapping("/recordPages")
    @ResponseBody
    public ResponseEntity<String> recordPages(Record record, Integer index) {
        JSONObject jsonObject = new JSONObject();

        //初始化当天时间
        if (startTime == null && endTime == null) {
            startTime = DateConvert.getStartTime();
            endTime = DateConvert.getEndTime();
        }
        if (index == 1) {
            record.setTableName(BANK_RECORD_TABLE_NAME);
        } else if (index == 2) {
            record.setTableName(BANK_RECORD_TABLE_NAME_2);
        } else {
            return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
        }
        /*设置分页参数*/
        PageHelper.startPage(record.getPage(), record.getRows());
        /**获取到数据集进行分页**/
        PageInfo<Record> pageInfo = new PageInfo<Record>(recordService.findAll(record));
        /*设置响应数据*/
        Float sumMoney = 0f;
        Float sumMoney1 = 0f;
        Float newMoney = 0f;
        Float newMoney1 = 0f;
        Float balance = 0f;
        Float balance1 = 0f;
        String showMoney = null;
        String showMoney1 = null;
        if (bankConfiguration != null) {
            String money = HttpSendHelper.sendGets(showSumMonye, "station=" + sourceStation(currentStation));
            if (isJSON(money)) {
                JSONObject object = JSONObject.fromObject(money);
                if (object.size() > 1) {
                    showMoney = JSONObject.fromObject(money).get("money").toString();
                } else {
                    showMoney = "0";
                }
            } else {
                showMoney = "0";
            }
            daySum = recordService.findDaySumMoney(BANK_RECORD_TABLE_NAME, startTime, endTime);
            if ("l".equals(currentStation))
                newMoney = recordService.findNewMoney(BANK_RECORD_TABLE_NAME, bankConfiguration.getChangeTime());
            sumMoney = recordService.findSumMoney(BANK_RECORD_TABLE_NAME, bankConfiguration.getChangeTime());
            balance = recordService.findBalance(BANK_RECORD_TABLE_NAME);
        }
//        if (bankConfiguration1 != null) {
//            String money = HttpSendHelper.sendGet(showSumMonye, "station=" + sourceStation(currentStation));
//            if (isJSON(money)) {
//                JSONObject object = JSONObject.fromObject(money);
//                if (object.size() > 1) {
//                    showMoney1 = JSONObject.fromObject(money).get("money").toString();
//                } else {
//                    showMoney1 = "0";
//                }
//            }
//            daySum1 = recordService.findDaySumMoney(BANK_RECORD_TABLE_NAME_2, startTime, endTime);
//            if ("l".equals(currentStation))
//                newMoney1 = recordService.findNewMoney(BANK_RECORD_TABLE_NAME_2, bankConfiguration1.getChangeTime());
//            sumMoney1 = recordService.findSumMoney(BANK_RECORD_TABLE_NAME_2, bankConfiguration1.getChangeTime());
//            balance1 = recordService.findBalance(BANK_RECORD_TABLE_NAME_2);
//        }
        jsonObject.put("showMoney", showMoney);
        jsonObject.put("showMoney1", showMoney1);
        jsonObject.put("daySum", daySum);
        jsonObject.put("daySum1", daySum1);
        jsonObject.put("newMoney", newMoney);
        jsonObject.put("newMoney1", newMoney1);
        jsonObject.put("sumMoney", sumMoney);
        jsonObject.put("sumMoney1", sumMoney1);
        jsonObject.put("balance", balance);
        jsonObject.put("balance1", balance1);
        jsonObject.put("station", StationEnum.getDescriptionByName(currentStation));
        jsonObject.put("rows", pageInfo.getList());
        jsonObject.put("total", pageInfo.getTotal());
        return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
    }


    @RequestMapping("/bankeList")
    @ResponseBody
    public ResponseEntity<String> bankeList(BankLogin bankLogin) {
        JSONObject jsonObject = new JSONObject();
        /*设置分页参数*/
        PageHelper.startPage(bankLogin.getPage(), bankLogin.getRows());
        /**获取到数据集进行分页**/
        PageInfo<BankLogin> pageInfo = new PageInfo<BankLogin>(bankLoginService.findAll(bankLogin));
        jsonObject.put("rows", pageInfo.getList());
        jsonObject.put("total", pageInfo.getTotal());
        return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
    }

    @RequestMapping("/bankConfig")
    public ModelAndView bankConfig(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("bank_config");
        modelAndView.addObject("banks", service.findAll(new BankConfiguration(BANK_CONFIG_TABLE_NAME)));
        modelAndView.addObject("banks1", service.findAll(new BankConfiguration(BANK_CONFIG_TABLE_NAME_2)));
        modelAndView.addObject("openBank", service.findOpenBank(BANK_CONFIG_TABLE_NAME));
        modelAndView.addObject("openBank1", service.findOpenBank(BANK_CONFIG_TABLE_NAME_2));
        return modelAndView;
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<String> update(BankConfiguration bankConfiguration) {
        String result;
        try {
            //关闭所有银行
            service.updateAllNotOpen(BANK_CONFIG_TABLE_NAME);
            //更新为选择银行配置
            bankConfiguration.setTableName(BANK_CONFIG_TABLE_NAME);
            String name = bankConfiguration.getBankName();
            service.updateByPrimaryKeySelective(bankConfiguration);
            this.bankConfiguration = service.findOpenBank(BANK_CONFIG_TABLE_NAME);
            this.bankConfiguration.setTableName(BANK_CONFIG_TABLE_NAME);
            IS_START = true;
            result = "更新成功！";
            System.out.println("您的配置①已经成功更改---请等待银行收支记录！！！！");
        } catch (Exception e) {
            e.printStackTrace();
            result = "更新异常！";
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @RequestMapping("/upbankList")
    @ResponseBody
    public ResponseEntity<String> upbankList(BankLogin bankLogin, int isOpen) {
        String result;
        try {
            //关闭所有银行
            service.updateAllNotOpen(BANK_CONFIG_TABLE_NAME);
            //更新为选择银行配置
            bankConfiguration = new BankConfiguration();
            bankConfiguration.setTableName(BANK_CONFIG_TABLE_NAME);
            bankConfiguration.setIsOpen(isOpen);
            BankLogin bank = bankLoginService.selectByPrimaryKey(bankLogin.getId());
            String name = bank.getBankName();
            bankConfiguration.setBankName(name);
            bankConfiguration.setId(bank.getBankConfid());
            service.updateByPrimaryKeySelective(bankConfiguration);
            String url = "";
            loginName = bank.getLoginName();
            loginPass = bank.getLoginPass();
            this.bankConfiguration = service.findOpenBank(BANK_CONFIG_TABLE_NAME);
            this.bankConfiguration.setTableName(BANK_CONFIG_TABLE_NAME);
            if (hpage != null) {
                hpage = null;
            }
            IS_START = true;
            result = "更新成功！";
            System.out.println("您的配置①已经成功更改---请等待银行收支记录！！！！");
        } catch (Exception e) {
            e.printStackTrace();
            result = "更新异常！";
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }


    @RequestMapping("/update1")
    @ResponseBody
    public ResponseEntity<String> update1(BankConfiguration bankConfiguration) {
        String result;
        try {
            //关闭所有银行
            service.updateAllNotOpen(BANK_CONFIG_TABLE_NAME_2);
            //更新为选择银行配置
            bankConfiguration.setTableName(BANK_CONFIG_TABLE_NAME_2);
            service.updateByPrimaryKeySelective(bankConfiguration);
            this.bankConfiguration1 = service.findOpenBank(BANK_CONFIG_TABLE_NAME_2);
            this.bankConfiguration1.setTableName(BANK_CONFIG_TABLE_NAME_2);
            IS_START1 = true;
            result = "更新成功！";
            System.out.println("您的配置②已经成功更改---请等待银行收支记录！！！！");
        } catch (Exception e) {
            e.printStackTrace();
            result = "更新异常！";
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @RequestMapping("/upChangeTime")
    @ResponseBody
    public ResponseEntity<String> upChangeTime(Integer index) {
        String result;
        try {
            if (bankConfiguration != null && index == 1) {
                //更新为选择银行配置
                bankConfiguration.setChangeTime(DateConvert.convertString(new Date()));
                bankConfiguration.setTableName(BANK_CONFIG_TABLE_NAME);
                service.updateByPrimaryKeySelective(bankConfiguration);
                result = "换盾成功！";
            } else if (bankConfiguration1 != null && index == 2) {
                //更新为选择银行配置
                bankConfiguration1.setChangeTime(DateConvert.convertString(new Date()));
                bankConfiguration1.setTableName(BANK_CONFIG_TABLE_NAME_2);
                service.updateByPrimaryKeySelective(bankConfiguration1);
                result = "换盾成功！";
            } else {
                result = "当前银行未开始抓取数据！换盾失败！！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "换盾异常！";
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @RequestMapping("/upStop")
    @ResponseBody
    public ResponseEntity<String> upStop(Integer index) {
        String result = "";
        try {
            if (bankConfiguration != null && index == 1) {
                IS_STOP_START = false;
                result = "暂停-抓取！";
            }
            if (bankConfiguration != null && index == 0) {
                IS_STOP_START = true;
                result = "继续-抓取！";
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "暂停异常！";
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }



    @RequestMapping("/upStatus")
    @ResponseBody
    public ResponseEntity<String> upChangeTime(Record record, Integer index) {
        String result = "参数异常，请重新再试";
        try {
            result = getUpdateType(record, index, result, 0);
        } catch (Exception e) {
            e.printStackTrace();
            result = "返回异常！";
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @RequestMapping("/setDate")
    @ResponseBody
    public ResponseEntity<String> setDate(String startDate, Integer index) {
        String result = "参数异常，请重新再试";
        try {
            if (!StringUtils.isEmpty(startDate) && index == 1) {
                this.startDate = startDate;
                bankConfiguration = service.findOpenBank(BANK_CONFIG_TABLE_NAME);
                result = "开启成功";
            }
            if (!StringUtils.isEmpty(startDate) && index == 2) {
                this.startDate1 = startDate;
                bankConfiguration1 = service.findOpenBank(BANK_CONFIG_TABLE_NAME_2);
                result = "开启成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "开启异常！";
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @RequestMapping("/redoUpdate")
    @ResponseBody
    public ResponseEntity<String> redoUpdate(Record record, Integer index) {
        String result = "参数异常，请重新再试";
        try {
            result = getUpdateType(record, index, result, 4);
        } catch (Exception e) {
            e.printStackTrace();
            result = "返回异常！";
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    /**
     * 加币状态更新公共类
     *
     * @param record 银行记录对象
     * @param index  银行记录表格下标
     * @param result 修改返回信息
     * @return String
     */
    private String getUpdateType(Record record, Integer index, String result, Integer status) {
        record.setIshandle(status);
        if (index == 1) {
            record.setTableName(BANK_RECORD_TABLE_NAME);
            //更新为选择银行配置
            recordService.updateByPrimaryKeySelective(record);
            result = (status == 0) ? "返回成功！" : "人工处理成功！";
        } else if (index == 2) {
            record.setTableName(BANK_RECORD_TABLE_NAME_2);
            //更新为选择银行配置
            recordService.updateByPrimaryKeySelective(record);
            result = (status == 0) ? "返回成功！" : "人工处理成功！";
        }
        return result;
    }

    /**
     * 每天12点更新当前银行为当天日期 0 0 0 * * ?
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDate() throws FileNotFoundException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        String paramDate = DateConvert.convertString(calendar.getTime(), "yyyyMMdd");
        if (IS_START) {
            bankConfiguration.setParam(bankConfiguration.getParam().replaceAll(paramDate, DateConvert.convertString(new Date(), "yyyyMMdd")));
            bankConfiguration.setChangeTime(DateConvert.convertString(new Date()));
            startDate = DateConvert.convertString(new Date());
        }
        if (IS_START1) {
            bankConfiguration1.setParam(bankConfiguration1.getParam().replaceAll(paramDate, DateConvert.convertString(new Date(), "yyyyMMdd")));
            bankConfiguration1.setChangeTime(DateConvert.convertString(new Date()));
            startDate1 = DateConvert.convertString(new Date());
        }
        service.updateByPrimaryKeySelective(bankConfiguration);
        number = 0;
    }

    /**
     * 每天12点更新当前时间和总和 0 0 0 * * ?
     * 0 0/1 * * * ?
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void toDaySum() {
        startTime = null;
        endTime = null;
    }


    /**
     * 银行抓取数据 30秒一次
     */
    @Scheduled(fixedRate = 40 * 1000)
    public void getBankInfo() {
        if (IS_START && IS_STOP_START) {
            IS_START = false;
            System.out.println("抓取配置①数据-----开始");
            if (this.bankConfiguration == null)
                bankConfiguration = service.findOpenBank(BANK_CONFIG_TABLE_NAME);
            switch (bankConfiguration.getBankName()) {
                case "农业银行":
                    int recordLength = recordService.findAll(new Record(DateConvert.convertString(new Date(), "yyyy-MM-dd"), BANK_RECORD_TABLE_NAME)).size();
                    for (int i = 0; i < recordLength / 10 + 1; i++) {
                        Thread thread = new Thread(new NongHangThread(bankConfiguration, recordService, url, BANK_RECORD_TABLE_NAME));
                        thread.start();
                    }
                    break;
                case "招商银行":
                    //连接银行接口得到返回数据
                    String zhaoShangResult = HttpClientUtil.httpsRequest(bankConfiguration.getUrl(), "POST", bankConfiguration.getParam(), bankConfiguration.getCharsetname(), bankConfiguration.getCookie());
                    if (zhaoShangResult.contains("IP")) {
                        IS_START = false;
                        System.out.println("------------抓取" + bankConfiguration.getBankName() + "异常，请【重新】登录银行、【重新】更新配置！！！！！！！！！");
                        new MP3Player(url).play();
                    }
                    if (isJSON(zhaoShangResult)) {
                        //根据对应银行获取该处理后的数据
                        List<Record> zhaoShangBankInfos = getZhaoShangInfo(zhaoShangResult);
                        //将数据加入本系统数据库
                        addBankInfo(zhaoShangBankInfos, bankConfiguration, BANK_RECORD_TABLE_NAME);
                    }
                    break;
                case "浦发银行":
                    //连接银行接口得到返回数据
                    String pufayinhangOutResult = "";
                    if (pnumber >= 20) {//分页
                        pufayinhangOutResult = HttpClientUtil.httpsRequests(bankConfiguration.getUrl(), "POST", bankConfiguration.getParam(), bankConfiguration.getCharsetname(), bankConfiguration.getCookie(), "application/x-json-stream; charset=utf-8", 447);
                    } else {//第一页
                        pufayinhangOutResult = HttpClientUtil.httpsRequests(bankConfiguration.getUrl(), "POST", bankConfiguration.getParam(), bankConfiguration.getCharsetname(), bankConfiguration.getCookie(), "application/x-json-stream; charset=utf-8", 446);
                    }
                    if (bankInfoGetError(pufayinhangOutResult)) {
                        new MP3Player(url).play();
                        return;
                    }
                    List<Record> getPuFaInBankInfos = getPuFaInfo(pufayinhangOutResult, bankConfiguration);
                    addBankInfo(getPuFaInBankInfos, bankConfiguration, BANK_RECORD_TABLE_NAME);
                    break;
               /* case "工商银行":
                    String gongshangResult = HttpClientUtil.httpsRequest(bankConfiguration.getUrl(), "POST", bankConfiguration.getParam(), "GBK", bankConfiguration.getCookie());
                    if (bankInfoGetError(gongshangResult)) {
                        new MP3Player(url).play();
                        return;
                    }
                    List<Record> getgongshangInfos = getGongShangIfo(gongshangResult, bankConfiguration);
                    addBankInfo(getgongshangInfos, bankConfiguration, BANK_RECORD_TABLE_NAME);
                    break;*/
                case "平安银行":
                    String pingAnOutResult = HttpClientUtil.httpsRequest(bankConfiguration.getUrl(), "POST",
                            bankConfiguration.getParam(), bankConfiguration.getCharsetname(), bankConfiguration.getCookie());
                    if (bankInfoGetError(pingAnOutResult)) {
                        new MP3Player(url).play();
                        return;
                    }
                    List<Record> getPingAnBankInfos = getPingAnInfo(pingAnOutResult);
                    addBankInfo(getPingAnBankInfos, bankConfiguration, BANK_RECORD_TABLE_NAME);
                    break;
                case "兴业银行":
                    List<Record> xingyeBankInfo = null;
                    if (hpage == null) {
                        getHttp(loginName, loginPass);
                        xingyeBankInfo = getxinyeList();
                    } else {
                        xingyeBankInfo = getxinyeList();
                    }
                    addBankInfo(xingyeBankInfo, bankConfiguration, BANK_RECORD_TABLE_NAME);
                    break;
               /* case "中信银行":
                    String zhongxinOutResult = HttpClientUtil.htRequest(bankConfiguration.getUrl(), "POST",
                            bankConfiguration.getParam(), bankConfiguration.getCharsetname(), bankConfiguration.getCookie());
                    if (bankInfoGetError(zhongxinOutResult)) {
                        new MP3Player(url).play();
                        return;
                    }
                    List<Record> zhongxinBankInfo = getZhongXinInFo(zhongxinOutResult);
                    addBankInfo(zhongxinBankInfo, bankConfiguration, BANK_RECORD_TABLE_NAME);
                    break;*/
                case "邮政银行":
                    String youzhengOutResult = HttpClientUtil.httpsRequest(bankConfiguration.getUrl(), "POST",
                            bankConfiguration.getParam(), bankConfiguration.getCharsetname(), bankConfiguration.getCookie());
                    if (bankInfoGetError(youzhengOutResult)) {
                        new MP3Player(url).play();
                        return;
                    }
                    List<Record> youzhengBankInfo = getYouZheng(youzhengOutResult);
                    //根据站点识别邮政是否查询
                    youzhengBankInfo = isOKyouzhengQuery(currentStation, youzhengBankInfo);
                    addBankInfo(youzhengBankInfo, bankConfiguration, BANK_RECORD_TABLE_NAME);
                    break;
                case "北京农商银行":
                    BjrcbSingle.getInstance().BjrcbRun(bankConfiguration, BANK_RECORD_TABLE_NAME,recordService);
                    break;
                case "农行企业银行":
                    NhqySingle.getInstance().NhqyRun(bankConfiguration, BANK_RECORD_TABLE_NAME,recordService);
                    break;
                case "工商银行":
                    GsSingle.getInstance().GsRun(bankConfiguration,BANK_RECORD_TABLE_NAME,recordService);
                    break;
                case "中信银行":
                    ZxSingleton.getInstance().ZxRun(bankConfiguration,BANK_RECORD_TABLE_NAME,recordService);
                    break;

            }
            System.out.println("抓取配置①数据-----结束");
            IS_START = true;
        }
    }

    /**
     * 根据站点识别邮政是否查询
     *
     * @param currentStation   当前站点
     * @param youzhengBankInfo 邮政银行数据所有集合
     * @return 处理后数据集合
     */
    private List<Record> isOKyouzhengQuery(String currentStation, List<Record> youzhengBankInfo) {
        //新集合除了结果
        List<Record> list = new ArrayList<>();
        //      执行处理
        if ("yes".equals(currentStation)) {
            //list = ?
            list=youzhengBankInfo.subList(0,10);
        }else {
            list =  youzhengBankInfo;
        }
//        返回结果
        return list;
    }


    /**
     * 兴业银行登陆页面
     *
     * @param loginName
     * @param loginPass
     */
    public static void getHttp(String loginName, String loginPass) {
        webclient = new WebClient(BrowserVersion.CHROME);
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http.client").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(Level.OFF);
        //参数设置
        // 1 启动JS
//        webclient.getOptions().setJavaScriptEnabled(true);
//        // 2 禁用Css，可避免自动二次请求CSS进行渲染
        webclient.getOptions().setCssEnabled(false);

        webclient.getOptions().setDownloadImages(false);

        //3 启动客户端重定向
        webclient.getOptions().setRedirectEnabled(true);
        // 4 运行错误时，当JS执行出错的时候是否抛出异常
        webclient.getOptions().setThrowExceptionOnScriptError(false);
        webclient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        // 5 设置超时
        //  webclient.getOptions().setTimeout(6000);
        //6 设置忽略证书
        webclient.getOptions().setUseInsecureSSL(true);
        //7 设置Ajax
        webclient.setAjaxController(new NicelyResynchronizingAjaxController());

        //当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webclient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        webclient.addRequestHeader("Connection", "tkeep-alive");
        webclient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
        webclient.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        webclient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
        webclient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
        HtmlPage htmlpage = null;
        String url = "https://personalbank.cib.com.cn/pers/main/login.do";
        try {

            htmlpage = webclient.getPage(url);
            webclient.waitForBackgroundJavaScript(10000);
            final HtmlForm form = htmlpage.getFormByName("loginForm");
            form.setActionAttribute("/pers/main/j_spring_security_check");
            final HtmlInput rdd = (HtmlInput) form.getByXPath("//*[@id=\"logintype0\"]").get(0);
            rdd.click();

            final HtmlTextInput uname = (HtmlTextInput) form.getByXPath("//*[@id=\"loginNameTemp\"]").get(0);
            //获取用户名输入焦点
            uname.focus();
            uname.setValueAttribute(loginName);
            final HtmlPasswordInput pss = (HtmlPasswordInput) form.getByXPath("//*[@id=\"iloginPwd\"]").get(0);
            //获取密码框输入焦点
            pss.focus();
            pss.setValueAttribute(loginPass);
            final HtmlPage nextpage = htmlpage.getElementById("loginSubmitBtn").click();


            // 等待JS驱动dom完成获得还原后的网页
            webclient.waitForBackgroundJavaScript(1000);
            // System.out.println(nextpage.getElementById("FIN01_02").click().getWebResponse().getContentAsString());
            hpage = nextpage.getElementById("FIN01_02").click();
            webclient.waitForBackgroundJavaScript(1000);
        } catch (IOException e) {
            return;
        }
    }

    /**
     * 兴业银行页面数据获取
     *
     * @return
     */
    public static List<Record> getxinyeList() {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http.client").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        List<Record> records = new ArrayList<>();
        try {
            HtmlTextInput textDate = (HtmlTextInput) hpage.getHtmlElementById("beginDate");
            textDate.focus();
            textDate.setValueAttribute(DateConvert.convertDateString(new Date()));
            //textDate.setValueAttribute("2018-02-11");
            HtmlPage httpage = hpage.getElementById("form_0").click();
            webclient.waitForBackgroundJavaScript(1000);
            int sp = 0;
            try {
                if (!"".equals(httpage.getElementById("sp_2").getTextContent())) {
                    sp = Integer.valueOf(httpage.getElementById("sp_2").getTextContent());
                }
                //int sp = Integer.valueOf(httpage.getElementById("sp_2").getTextContent());
                //System.out.println(httpage.asXml());
                records = getXingyeInfo(httpage.asXml());
                HtmlPage htsp = null;
                do {
                    if (sp > 1) {
                        htsp = httpage.getElementById("next").click();
                        webclient.waitForBackgroundJavaScript(1000);
                        records.addAll(getXingyeInfo(httpage.asXml()));
                        sp--;
                    }
                } while (sp != 1);
            } catch (NullPointerException e) {
                hpage = null;
                return records;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }


    /**
     * 银行抓取数据 30秒一次
     */
    @Scheduled(fixedRate = 30 * 1000)
    public void getBank2Info() {
        if (IS_START1 && IS_STOP_START) {
            IS_START1 = false;
            System.out.println("抓取配置②数据-----开始");
            if (this.bankConfiguration1 == null)
                bankConfiguration1 = service.findOpenBank(BANK_CONFIG_TABLE_NAME_2);
            switch (bankConfiguration1.getBankName()) {
                case "农业银行":
                    int recordLength = recordService.findAll(new Record(DateConvert.convertString(new Date(), "yyyy-MM-dd"), BANK_RECORD_TABLE_NAME_2)).size();
                    for (int i = 0; i < recordLength / 10 + 1; i++) {
                        Thread thread = new Thread(new NongHangThread(bankConfiguration1, recordService, url, BANK_RECORD_TABLE_NAME_2));
                        thread.start();
                    }
                    break;
                case "招商银行":
                    //连接银行接口得到返回数据
                    String zhaoShangResult = HttpClientUtil.httpsRequest(bankConfiguration1.getUrl(), "POST", bankConfiguration1.getParam(), bankConfiguration1.getCharsetname(), bankConfiguration1.getCookie());
                    if (zhaoShangResult.contains("IP")) {
                        IS_START1 = false;
                        System.out.println("------------抓取" + bankConfiguration1.getBankName() + "异常，请【重新】登录银行、【重新】更新配置！！！！！！！！！");
                        new MP3Player(url).play();
                    }
                    if (isJSON(zhaoShangResult)) {
                        //根据对应银行获取该处理后的数据
                        List<Record> zhaoShangBankInfos = getZhaoShangInfo(zhaoShangResult);
                        //将数据加入本系统数据库
                        addBankInfo(zhaoShangBankInfos, bankConfiguration1, BANK_RECORD_TABLE_NAME_2);
                    }
                    break;
                case "浦发银行":
                    //连接银行接口得到返回数据
                    String pufayinhangOutResult = "";
                    if (pnumber1 >= 20) {//分页
                        pufayinhangOutResult = HttpClientUtil.httpsRequests(bankConfiguration1.getUrl(), "POST", bankConfiguration1.getParam(), bankConfiguration1.getCharsetname(), bankConfiguration1.getCookie(), "application/x-json-stream; charset=utf-8", 447);
                    } else {//第一页
                        pufayinhangOutResult = HttpClientUtil.httpsRequests(bankConfiguration1.getUrl(), "POST", bankConfiguration1.getParam(), bankConfiguration1.getCharsetname(), bankConfiguration1.getCookie(), "application/x-json-stream; charset=utf-8", 446);
                    }
                    if (bankInfoGetError(pufayinhangOutResult)) return;
                    List<Record> getPuFaInBankInfos = getPuFaInfo(pufayinhangOutResult, bankConfiguration1);
                    addBankInfo(getPuFaInBankInfos, bankConfiguration1, BANK_RECORD_TABLE_NAME_2);
                    break;
                case "工商银行":
                    String gongshangResult = HttpClientUtil.httpsRequest(bankConfiguration1.getUrl(), "POST", bankConfiguration1.getParam(), "GBK", bankConfiguration1.getCookie());
                    if (bankInfoGetError(gongshangResult)) {
                        new MP3Player(url).play();
                        return;
                    }
                    List<Record> getgongshangInfos = getGongShangIfo(gongshangResult, bankConfiguration);
                    addBankInfo(getgongshangInfos, bankConfiguration1, BANK_RECORD_TABLE_NAME_2);
                    break;
                case "平安银行":
                    String pingAnOutResult = HttpClientUtil.httpsRequest(bankConfiguration1.getUrl(), "POST",
                            bankConfiguration1.getParam(), bankConfiguration1.getCharsetname(), bankConfiguration1.getCookie());
                    if (bankInfoGetError(pingAnOutResult)) return;
                    List<Record> getPingAnBankInfos = getPingAnInfo(pingAnOutResult);
                    addBankInfo(getPingAnBankInfos, bankConfiguration1, BANK_RECORD_TABLE_NAME_2);
                    break;
                case "兴业银行":
                    break;
                case "中信银行":
                    String zhongxinOutResult = HttpClientUtil.htRequest(bankConfiguration1.getUrl(), "POST",
                            bankConfiguration1.getParam(), bankConfiguration1.getCharsetname(), bankConfiguration1.getCookie());
                    if (bankInfoGetError(zhongxinOutResult)) {
                        new MP3Player(url).play();
                        return;
                    }
                    List<Record> zhongxinBankInfo = getZhongXinInFo(zhongxinOutResult);
                    addBankInfo(zhongxinBankInfo, bankConfiguration1, BANK_RECORD_TABLE_NAME_2);
                    break;
            }
            System.out.println("抓取配置②数据-----结束");
            IS_START1 = true;
        }
    }

    /**
     * 银行数据信息获取失败
     *
     * @param result 结果集
     * @return boolean true 失败 --- false 成功
     */
    private boolean bankInfoGetError(String result) {
        if (result != null) {
            /*根据返回辨别是否失败--农行--工商--招商--建行*/
            if (result.contains("会话超时") || result.contains("跳转页面") || "".equals(result) || result.contains("服务器超时，请重试！") || result.contains("安全隐患") || result.toString().contains("会话已失效") || result.contains("统一报错") || result.contains("系统超时")) {
                this.IS_START = false;
                System.out.println("------------抓取" + bankConfiguration.getBankName() + "异常，请【重新】登录银行、【重新】更新配置！！！！！！！！！");
                return true;
            }

        }
        return false;

    }


    /**
     * 邮政银行
     *
     * @param result
     * @return
     */
    public static List<Record> getYouZheng(String result) {
        List<Record> records = new ArrayList<>();
        JSONArray jsoar = JSONObject.fromObject(result).getJSONArray("transDetailList1");
        for (int i = 0; i < jsoar.size(); i++) {
            JSONObject jsob = (JSONObject) jsoar.get(i);
            Record record = new Record();
            String time = DateConvert.gainTime(jsob.get("tranTime").toString());
            record.setTradinghour(time);
            String inOut = jsob.get("inOut").toString();
            if (inOut.contains("2")) {
                record.setIncoming(Float.valueOf("-" + jsob.get("tranAmt").toString()));
                record.setRemark(jsob.get("toAccName").toString());
            } else{
                record.setIncoming(Float.valueOf(jsob.get("tranAmt").toString()));
                String merNameCn = jsob.get("merNameCn").toString();
                if(merNameCn.contains("支付宝-")){
                    record.setRemark(merNameCn.split("-")[1].replaceAll("支付宝转账",""));
                }else {
                    record.setRemark(jsob.get("fromAccName").toString());
                }
            }
            record.setBalance(Float.valueOf(jsob.get("balance").toString()));
            records.add(record);
        }
        return records;
    }


    /**
     * 兴业银行
     *
     * @param result 接口返回数据
     * @return List<Record> 银行信息合集
     */
    public static List<Record> getXingyeInfo(String result) {
        List<Record> records = new ArrayList<>();
        String rec = null;
        Document doc = Jsoup.parse(result);
        Elements elmtr = doc.getElementById("list").select("tbody").select("tr");
        for (int i = 1; i < elmtr.size(); i++) {
            Record re = new Record();
            Elements elmtd = elmtr.get(i).select("td");
            for (int j = 0; j < elmtd.size(); j++) {
                re.setTradinghour(elmtd.get(0).text());
                if ("     ".equals(elmtd.get(2).text())) {
                    re.setIncoming(Float.valueOf(getMoney(elmtd.get(3).text())));
                } else {
                    String money = getMoney(elmtd.get(2).text());
                    if (!money.contains("-")) {
                        re.setIncoming(Float.valueOf("-" + money));
                    } else {
                        re.setIncoming(Float.valueOf(money));
                    }
                }
                re.setBalance(Float.valueOf(getMoney(elmtd.get(4).text())));
                rec = elmtd.get(6).text();
                if (!"".equals(rec)) {
                    if (rec.contains("支付宝")) {
                        re.setRemark(elmtd.get(9).text().substring(0, elmtd.get(9).text().indexOf("支付宝转账")));
                    } else {
                        re.setRemark(rec + "-" + elmtd.get(5).text());
                    }
                } else {
                    re.setRemark(elmtd.get(5).text());
                }
                break;
            }
            records.add(re);
        }
//        JSONArray jsoar = JSONObject.fromObject(result).getJSONArray("rows");
//        List<Record> records = new ArrayList<>();
//        String rec = null;
//        for (int i = 0; i < jsoar.size(); i++) {
//            Record re = new Record();
//            JSONArray jsarr = jsoar.getJSONObject(i).getJSONArray("cell");
//            for (int j = 0; j < jsarr.size(); j++) {
//                re.setTradinghour(jsarr.getString(0));
//                if ("".equals(jsarr.getString(2))) {
//
//                    String money = jsarr.getString(3);
//
//                    re.setIncoming(Float.valueOf(getMoney(money)));
//
//                } else {
//                    String money = getMoney(jsarr.getString(2));
//                    if (!money.contains("-")) {
//                        re.setIncoming(Float.valueOf("-" + money));
//                    } else {
//                        re.setIncoming(Float.valueOf(money));
//                    }
//                }
//
//                re.setBalance(Float.valueOf(getMoney(jsarr.getString(4))));
//                rec = jsarr.getString(6);
//                if (!"".equals(jsarr.getString(6))) {
//                    if (rec.contains("支付宝")) {
//                        re.setRemark(jsarr.getString(9).substring(0, jsarr.getString(9).indexOf("支付宝转账")));
//                    } else {
//                        re.setRemark(rec + "-" + jsarr.getString(5));
//                    }
//                } else {
//                    re.setRemark(jsarr.getString(5));
//                }
//                break;
//            }
//            records.add(re);
//        }
        return records;
    }

    /**
     * 截取带逗号字符串，转float
     *
     * @param money
     * @return
     */
    public static String getMoney(String money) {
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
     * 工商银行收支信息获取
     *
     * @param result
     * @param bankCon
     * @return
     */
    public List<Record> getGongShangIfo(String result, BankConfiguration bankCon) {
        List<Record> gongshangList = new ArrayList<Record>();
        String semoney = null;
        Document docs = Jsoup.parse(result);
        Elements masthead = docs.select("table.lst").select("tbody").select("tr");

        for (int i = 0; i < masthead.size() - 1; i++) {
            Record rec = new Record();
            semoney = masthead.get(i + 1).select("td").select("span").text().replace(",", "");
            rec.setRemark(masthead.get(i + 1).select("td").get(1).text());
            rec.setBalance(Float.valueOf(masthead.get(i + 1).select("td").get(4).text().replace(",", "")));
            rec.setIncoming(Float.valueOf(semoney));
            String mname = masthead.get(i + 1).select("td").get(5).text();
            if ("".equals(mname)) {
                String sn = masthead.get(i + 1).select("td").get(5).select("script").get(0).toString();
                sn = sn.substring(sn.lastIndexOf("('") + 2, sn.indexOf("'))"));
                try {
                    sn = URLDecoder.decode(sn, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                rec.setRemark(sn + "-" + rec.getRemark());
            }

            String details = gongshangdetails(masthead, bankCon, i);
            rec.setTradinghour(gongshangTime(bankCon, details, semoney));
            gongshangList.add(rec);
        }
//        if (gongshangList.size() == 20) {
//            if(result.contains("javascript:onNextPage")){
//                gongshangPage(bankCon, docs);
//            }
//
//
//        }
        return gongshangList;
    }


    /**
     * 工商详情页面param值设置
     *
     * @param masthead
     * @param bankCon
     * @param i
     * @return
     */
    public String gongshangdetails(Elements masthead, BankConfiguration bankCon, int i) {
        String details = null;
        String st = null;
        String sts = null;
        String param = bankCon.getParam();
        String dse_sessionId = param.substring(param.indexOf("dse_sessionId="), param.indexOf("&")).split("=")[1];
        String requestTokenid = param.substring(param.indexOf("requestTokenid="), param.indexOf("&dse_applicationId")).split("=")[1];
        String begDate = param.substring(param.indexOf("begDate="), param.indexOf("&endDate=")).split("=")[1];
        String endDate = param.substring(param.indexOf("endDate="), param.indexOf("&ishomecard=")).split("=")[1];
        String tes = masthead.get(i + 1).select("td").get(6).getElementsByAttribute("href").attr("href");
        String es = tes.substring(tes.indexOf("('") + 2, tes.indexOf("')"));
        String[] s = es.split("','");
        System.out.println("最新工商银行详情参数"+es);
        if (s[1].contains("2")) {
            sts = s[5];
            st = s[5];
        } else {
            st = s[5];
            sts = s[6];
        }
        for (int j = 0; j < s.length; j++) {
            details = "dse_sessionId=" + dse_sessionId + "&requestTokenid=" + requestTokenid + "&dse_applicationId=-1&dse_operationName=per_AccountQueryHistoryDetailOp&dse_pageId=4&acctNum=" + s[2] + "&DRCRF=" + s[1] + "&ESERIALNO=" + s[0] + "&historyFlag=1&tranDateHistory=" + s[4] + "&payCardNumHDM=" + st + "&recCardNumHDM=" + sts + "&bgDateHistory=" + begDate + "&edDateHistory=" + endDate + "&cardNum=" + st + "&cardNumAffi=" + st + "&cardType=011&affiFlag=&cardNumAffiDetail=" + st + "";
            break;
        }
        return details;
    }

    /**
     * 工商收支时间获取
     *
     * @param bankCon
     * @param details
     * @param semoney
     * @return
     */
    public String gongshangTime(BankConfiguration bankCon, String details, String semoney) {
        String time = null;
        String request = HttpClientUtil.httpsRequest(bankCon.getUrl(), "POST", details, "GBK",
                bankCon.getCookie());

        Document docs = Jsoup.parse(request);
        Elements masthead = docs.select("TABLE.normaltbl").select("TBODY").select("TR").select("table").select("tbody").select("tr");
        Elements mast = null;
        for (int k = 0; k < masthead.size(); k++) {
            if (semoney.contains("-")) {
                mast = masthead.get(9).select("td");
            } else {
                mast = masthead.get(7).select("td");
            }
            for (int j = 0; j < mast.size(); j++) {
                time = DateConvert.consetDateYer(mast.get(1).text().replaceAll(" ", " "));
                break;
            }
            break;
        }
        System.out.println("最新获取工商银行交易详情时间:"+time);
        return time;
    }

    /**
     * 工商翻页参数设置
     *
     * @param bankCon
     * @param docs
     */
    public void gongshangPage(BankConfiguration bankCon, Document docs) {
        String incomeSum = null, timestmp = null, payoutSum = null, pageflag = null, flag = null;
        String param = bankCon.getParam();
        String newParam = null;
        String strs = docs.select("table.tblWidth").select("a.link").get(21).attr("href");
        String[] strt = strs.substring(strs.indexOf("('") + 2, strs.indexOf("')")).split("','");
        for (int i = 0; i < strt.length; i++) {
            timestmp = strt[3];
            incomeSum = strt[4];
            payoutSum = strt[5];
            break;
        }
        int pos = 20;
        String Begin_pos = param.substring(param.indexOf("Begin_pos="), param.indexOf("&acctIndex")).split("=")[1];
        if (Begin_pos.contains("-1")) {
            Begin_pos = String.valueOf(pos + Integer.valueOf(Begin_pos) + 1);
            param = param.replace("pageflag=0", "pageflag=2");
            param = param.replace("&flag=", "&flag=1");
        } else {
            Begin_pos = String.valueOf(pos + Integer.valueOf(Begin_pos));
        }

        String be = param.replace("Begin_pos=-1", "Begin_pos=" + Begin_pos);
        String inc = be.replace("incomeSum=", "incomeSum=" + incomeSum);
        String ti = inc.replace("timestmp=", "timestmp=" + timestmp);
        String pay = ti.replace("payoutSum=", "payoutSum=" + payoutSum);
        newParam = pay;
        bankCon.setParam(newParam);
    }


    /**
     * 判断是否是json
     *
     * @param result json字符串
     * @return boolean
     */
    public static boolean isJSON(String result) {
        boolean flag = true;
        try {
            JSONObject jsonObject = JSONObject.fromObject(result);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 添加数据
     *
     * @param bankInfos
     */
    private void addBankInfo(List<Record> bankInfos, BankConfiguration bankConfiguration, String tableName) {
        int nuber = 0;
        String bankName = bankConfiguration.getBankName();
        if ("兴业银行".equals(bankName) || "中信银行".equals(bankName)) {
            bankInfos = removeDuplicate(bankInfos);
        }
        for (Record record : bankInfos) {
            record.setTableName(tableName);
            record.setToken(SpringUtil.getToken());
            recordService.insertSelective(record);
        }
        if ("浦发银行".equals(bankName)) {
            JSONObject jsonObject = JSONObject.fromObject(bankConfiguration.getParam());
            if (jsonObject.has("SeqNo")) {
                nuber = jsonObject.getInt("SeqNo");
                if (nuber % 2 == 0) {
                    nuber = nuber + 1;
                    jsonObject.put("BeginNumber", String.valueOf(nuber));
                    jsonObject.remove("SeqNo");
                    bankConfiguration.setParam(String.valueOf(jsonObject));
                    service.updateByPrimaryKeySelective(bankConfiguration);
                }
                if (nuber != 0) {
                    pnumber = nuber;
                }
            }
        }
    }

    /**
     * 去重
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

    /**
     * 招商收支信息获取
     *
     * @param result 接口返回数据
     * @return List<Record> 银行信息合集
     */
    private List<Record> getZhaoShangInfo(String result) {
        List<Record> records = new ArrayList<Record>();
        JSONObject jsonObject = JSONObject.fromObject(result);
        try {
            Iterator iterator = jsonObject.keys();
            String value = null;
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                if (key.equals("content")) {
                    value = (String) jsonObject.get(key);
                    value = URLDecoder.decode(value, "UTF-8");
                    break;
                }
            }
            Document doc = Jsoup.parse("<table>" + value + "</table>");
            Elements links = doc.getElementsByTag("tr");
            for (int i = 1; i < links.size(); i++) {
                Elements tds = links.get(i).getElementsByTag("td");
                Record record = new Record();
                String time = null;
                String tmon = null;
                for (int j = 0; j < tds.size(); j++) {
                    switch (j) {
                        case 0:
                            time = tds.get(j).text();
                            record.setTradinghour(time);
                            break;
                        case 1:
                            tmon = tds.get(j).text().replaceAll(",", "");
                            if (tmon.contains("$Plus$")) {
                                tmon = tmon.substring(tmon.lastIndexOf("$") + 1);
                            }
                            record.setIncoming(Float.valueOf(tmon));
                            break;
                        case 2:
                            record.setBalance(Float.valueOf(tds.get(j).text().replaceAll(",", "")));
                            break;
                        case 4:
                            String rname = tds.get(j).text();
                            if (rname.contains("支付宝")) {
                                String[] arr = rname.split("支付宝");
                                rname = arr[0];
                            }
                            if ("".equals(rname)) {
                                long ls = DateConvert.getLong(time);
                                rname = String.valueOf(ls);
                            }
                            record.setRemark(rname);
                            break;
                    }
                }
                records.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * 平安银行
     *
     * @param result 页面数据
     * @return List<Record> 银行信息合集
     */
    public List<Record> getPingAnInfo(String result) {
        List<Record> list = new ArrayList<>();
        Object object = JSONObject.fromObject(result).getJSONObject("data").get("result_value");
        JSONArray jsonArrays = JSONArray.fromObject(object);
        for (int i = 0; i < jsonArrays.size(); i++) {
            Record record = new Record();
            JSONObject jsonObject = jsonArrays.getJSONObject(i);
            record.setTradinghour(jsonObject.get("tranTime").toString());
            if (jsonObject.get("toAcctFlag").toString().contains("01")) {
                record.setRemark(jsonObject.get("fromClientName").toString() + "-" + jsonObject.get("remark"));
                record.setIncoming(Float.valueOf(jsonObject.get("tranAmt").toString()));
            } else {
                record.setRemark(jsonObject.get("toClientName").toString() + "-" + jsonObject.get("remark"));
                record.setIncoming(Float.valueOf("-" + jsonObject.get("tranAmt").toString()));
            }
            record.setBalance(Float.valueOf(jsonObject.get("balance").toString()));

            list.add(record);
        }
        return list;
    }


    /**
     * 浦发银行
     *
     * @param result 接口返回数据
     * @return List<Record> 银行信息合集
     */
    public List<Record> getPuFaInfo(String result, BankConfiguration bankConfiguration) {
        String txType = null;
        String cursDate = null;
        String businessId = null;
        String crDtIndicator = null;
        List<Record> records = new ArrayList<>();
        JSONArray jsona = JSONObject.fromObject(result).getJSONArray("LoopResult");
        if (jsona.size() != 0) {
            for (int i = 0; i < jsona.size(); i++) {
                Record bankRecord = new Record();
                JSONObject jsonb = jsona.getJSONObject(i);
                Iterator iter = jsonb.keys();
                while (iter.hasNext()) {
                    String url = null;
                    cursDate = DateConvert.convertTurnString(jsonb.get("OccursDate").toString().trim());
                    bankRecord.setTradinghour(cursDate);
                    crDtIndicator = jsonb.getString("CrDtIndicator");
                    txType = jsonb.get("TxType").toString().trim();
                    businessId = jsonb.getString("BusinessId");
                    if (crDtIndicator.contains("0")) {
                        if (jsonb.getString("TxAmount").contains("-")) {
                            bankRecord.setIncoming(Float.valueOf(jsonb.get("TxAmount").toString().trim()));
                        } else {
                            bankRecord.setIncoming(Float.valueOf("-" + jsonb.get("TxAmount").toString().trim()));
                        }
                    } else {
                        bankRecord.setIncoming(Float.valueOf(jsonb.get("TxAmount").toString().trim()));
                    }
                    bankRecord.setBalance(Float.valueOf(jsonb.get("Balance").toString().trim()));
                    // bankRecord.setRemark(txType.substring(txType.indexOf(":") + 1, txType.length()));
                    if (txType.contains("支付宝")) {
                        bankRecord.setRemark(txType.substring(txType.indexOf(":") + 1, txType.lastIndexOf("支")));
                    } else {
                        if (!"".equals(businessId)) {
                            businessId = jsonb.get("BusinessId").toString();
                            String remitFlag = jsonb.get("RemitFlag").toString();
                            url = yinghUrl + "?RemitFlag=" + remitFlag + "&BusinessId=" + businessId;
                            String pufayinhangOutResult = HttpClientUtil.httpsRequests(url, "POST", "", bankConfiguration.getCharsetname(), bankConfiguration.getCookie(), "application/x-json-stream; charset=utf-8", 446);
                            bankRecord.setRemark(getName(pufayinhangOutResult) + "-" + txType);
                        } else {
                            if (txType.contains("批量付WZ")) {
                                txType = txType.substring(txType.indexOf("Z") + 1, txType.length());
                                bankRecord.setRemark(txType);
                            } else if (!txType.contains("null")) {
                                bankRecord.setRemark(txType);
                            }
                        }
                    }
                    break;
                }
                records.add(bankRecord);
                JSONObject jsonObject = JSONObject.fromObject(bankConfiguration.getParam());
                jsonObject.put("SeqNo", jsonb.getString("SeqNo"));
                bankConfiguration.setParam(jsonObject.toString());
            }
        }
        return records;
    }


    /**
     * 中信银行
     *
     * @param request
     * @return
     */
    public List<Record> getZhongXinInFo(String request) {
        List<Record> records = new ArrayList<>();
        Document docs = Jsoup.parse(request);
        Elements masthead = docs.select("table").select("tbody").select("tr");
        Elements el = null;
        String re = null;
        for (int i = 0; i < masthead.size() - 1; i++) {
            Record rec = new Record();
            el = masthead.get(i).select("td");
            for (int j = 0; j < el.size(); j++) {
                rec.setTradinghour(el.get(0).text());
                String incom = null;
                String balance = null;
                if (el.get(1).text().contains("--")) {
                    incom = "-" + el.get(2).text().split("￥")[1];
                } else {
                    incom = el.get(1).text().split("￥")[1];
                }
                if (incom.contains(",")) {
                    incom = incom.replace(",", "");
                }
                rec.setIncoming(Float.valueOf(incom));
                balance = el.get(3).text().split("￥")[1];
                if (balance.contains(",")) {
                    balance = balance.replace(",", "").replaceAll(" ", "");
                }
                balance = balance.replaceAll(" ", "");
                rec.setBalance(Float.valueOf(balance));
                re = el.get(6).text();
                if (re.length() > 1) {
                    if (el.get(4).text().contains("支付宝")) {
                        rec.setRemark(re.substring(0, re.indexOf("支付宝转账")));
                    } else {
                        /*rec.setRemark(el.get(4).text() + "-" + el.get(6).text());*/
                        rec.setRemark(el.get(4).text());
                    }
                } else {
                    rec.setRemark(el.get(4).text());
                }
            }
            records.add(rec);
        }

//        return grabZhongXinTime(records);
        return records;
    }

    /**
     * 获取中信当天数据
     *
     * @param zx
     * @return
     */
    public List<Record> grabZhongXinTime(List<Record> zx) {
        String tra = null;
        String time = DateConvert.convertDateString(new Date());
        List<Record> zxTime = new ArrayList<>();
        for (Record rec : zx) {
            Record record = new Record();
            tra = rec.getTradinghour();
            if (tra.indexOf(time) != -1) {
                record.setTradinghour(rec.getTradinghour());
                record.setIncoming(rec.getIncoming());
                record.setBalance(rec.getBalance());
                record.setRemark(rec.getRemark());
                zxTime.add(record);
            }
        }
        return zxTime;
    }


    /**
     * 站点转换
     *
     * @param station 风控站点
     * @return number 秀场站点
     */
    public String sourceStation(String station) {
        String number = "";
        if (station != null || station != "") {
            switch (station) {
                case "a":
                    number = "1";
                    break;
                case "b":
                    number = "2";
                    break;
                case "c":
                    number = "3";
                    break;
                case "d":
                    number = "4";
                    break;
                case "e":
                    number = "5";
                    break;
                case "f":
                    number = "6";
                    break;
                case "g":
                    number = "7";
                    break;
                case "h":
                    number = "8";
                    break;
                case "l":
                    number = "9";
                    break;
            }
        }
        return number;
    }

    @ResponseBody
    @RequestMapping("/parameters")
    public ResponseEntity<String> parameters(Record rec) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("parameter", service.selectByPrimaryKey(rec.getId()));

        return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
    }


    /**
     * 浦发html页面截取
     *
     * @param resultHtml 浦发html页面
     * @return resName 收付款姓名
     */
    public String getName(String resultHtml) {
        Document doc = Jsoup.parse(resultHtml);
        Elements e = null;
        String resName = null;
        String state = doc.getElementsByTag("table").select("tr").eq(0).select("th").eq(0).text();
        if (state.contains("互联汇入")) {
            e = doc.getElementsByTag("table").select("tr").eq(3).select("td").eq(1);
            resName = e.get(0).text();
        } else if (state.contains("跨行汇款汇入")) {
            e = doc.getElementsByTag("table").select("tr").eq(2).select("td").eq(1);
            resName = e.get(0).text();
        } else if (state.contains("互联汇出")) {
            e = doc.getElementsByTag("table").select("tr").eq(3).select("td").eq(3);
            if (e.size() == 0) {
                e = doc.getElementsByTag("table").select("tr").eq(3).select("td").eq(1);
            }
            resName = e.get(0).text();
        }
        return resName;
    }

    @RequestMapping("/promptMessage")
    @ResponseBody
    public ResponseEntity<String> promptMessage() {
        JSONObject jsonObject = new JSONObject();

        Record namber = recordService.findNamber(new Record());

        jsonObject.put("msg", namber.getTradinghour());

        return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
    }

    public static void main(String[] args) throws IOException {

    }
}
