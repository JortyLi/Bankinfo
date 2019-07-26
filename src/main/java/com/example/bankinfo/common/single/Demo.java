package com.example.bankinfo.common.single;

import com.example.bankinfo.common.DateConvert;
import com.example.bankinfo.common.FileUtil;
import com.example.bankinfo.common.HttpClientUtil;
import com.example.bankinfo.domain.Record;
import org.apache.commons.lang3.SerializationUtils;
import org.intellij.lang.annotations.Language;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.thymeleaf.expression.Sets;

import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {
    public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
       /* Scanner scanner =new Scanner(System.in);
        String s = scanner.next();
            switch (s){
                case "Hello":
                    System.out.println("The night is long that never finds the day !");
                    break;
                case "World":
                    System.out.println("无论黑夜有多么的漫长,白昼总是会到来的!!");
                    break;
                case "!":
                    System.out.println("Hello World");
                    break;
                case "我":
                    System.out.println("世界你好,你好世界");
                    break;
                default:
                    System.out.println("我想对你说:直接去就好了!!");
            }
        }*/
   /*     Record record = new Record();
        String s = "￥34.32 ";
        String balance = null;
        balance = s.split("￥")[1];
        if (balance.contains(",")) {
            balance = balance.replace(",", "").replaceAll(" ", "");
        }
        balance = balance.replaceAll(" ", "");
        //余额
        Float a = Float.valueOf(balance);
        record.setBalance(a);
        System.out.println(a);
        System.out.println(record.getBalance());*/

     /*   String [] a = {"1","2","3","4","5"};

        List<String> strings = Arrays.asList(a);
        for (String string : strings) {
            System.out.print(string + "  ");
        }

        ArrayList<String>lists = new ArrayList<>(Arrays.asList(a));
        for (String list : lists) {
            System.out.print(list + " ");
        }

        if (Arrays.asList(a).contains("1")){
            System.out.println("数组a里面包含:" + "1");
        }


        ArrayList<String> list = new ArrayList<String>(Arrays.asList("a", "b", "c","d"));
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String s = iter.next();
            if (s.equals("a")) {
                iter.remove();
            }
        }
        System.out.println(list);*/


        /*String s = URLEncoder.encode(null, "UTF-8");
        System.out.println(s);*/
        /*byte a =127;
        byte b =127;
        b +=a;
        System.out.println(b);*/

       /* String s = "银联入账：冯建锋支付宝转账";
        String substring = s.substring(0, s.indexOf("支付宝转账"));

        if (substring.contains("银联入账")){
            substring = substring.substring(5);
        }

        System.out.println(substring);*/

/*
        String date = "2016-05-01 12:30"; // 输入的数据 yyyy-mm-dd hh:mm:ss
        date = date+":59";
        System.out.println(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//格式化
        Date inDate = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();// 素输入时间
        calendar.setTime(inDate);
        calendar.add(Calendar.SECOND, 59);//加55分钟

        Date outDate = calendar.getTime();
        System.out.println(sdf.format(outDate));*/

        /*String s ="2019-04-01 11:10:59";
        String dateYer = DateConvert.consetDateYer(s);
        System.out.println(dateYer);*/
       /* String encode = URLEncoder.encode("2019-04-05 12:05:59", "UTF-8");
        System.out.println(encode);
        String decode = URLDecoder.decode("2019-04-05+12%3A05%3A59", "UTF-8");
        System.out.println(decode);*/
       /* String s1 = "2019-04-05 12:05:59";
        String s2 = "2019-04-01 11:16:59";
        Long long1 = Long.valueOf(s1.replaceAll("[-\\s:]", ""));
        Long long2 = Long.valueOf(s2.replaceAll("[-\\s:]", ""));
        if (long1 > long2){
            System.out.println("新抓取的数据大于已经抓取的数据,继续抓取");
        }else{
            System.out.println("小于已经抓取的数据,不抓取");
        }*/

           /* String result = HttpClientUtil.httpsRequest(url, "POST", parm, "UTF-8", cook);
            System.out.println(result);
            Elements masthead = Jsoup.parse(result).select("table[class=lst]").select("tr");
            for (int i = 0; i < masthead.size(); i++) {
                Element element = masthead.get(i);
                Elements e_td = element.select("td");
                System.out.println(e_td);
            }
            String s = parm.substring(parm.indexOf("dse_sessionId="),parm.indexOf("&")).split("=")[1];
            String requestTokenid =parm.substring(parm.indexOf("requestTokenid="),parm.indexOf("&dse_applicationId")).split("=")[1];

            System.out.println(s);
            System.out.println(requestTokenid);*/


        /*String decode = URLDecoder.decode("%0D%0A%0D%0A%0D%0A0101010%7C0101%7C%E4%BA%BA%E6%B0%91%E5%B8%81%7C0%7C0", "UTF-8");
        System.out.println(decode);*/

        /*String aa ="ACC_NO=6236683850000315982&ACCSIGN=%0D%0A%0D%0A%0D%0A0101010%7C0101%7C%E4%BA%BA%E6%B0%91%E5%B8%81%7C0%7C0&START_DATE=20190407&";
       String b = aa.substring(aa.indexOf("&ACCSIGN="),aa.indexOf("&START_DATE")).split("=")[1];
        System.out.println( b);*/


      /*  ArrayList<String>pokerBox = new ArrayList<String>();
        ArrayList<String>colors = new ArrayList<String>();
        ArrayList<String>number = new ArrayList<String>();

        colors.add("♥");
        colors.add("♦");
        colors.add("♠");
        colors.add("♣");

        for (int i = 2; i <= 10; i++) {
            number.add(i + "");
        }
        number.add("J");
        number.add("Q");
        number.add("K");
        number.add("A");

        for (String color : colors) {
            for (String s : number) {
                String card = color + s ;
                pokerBox.add(card);
            }
        }
        pokerBox.add("大鬼");
        pokerBox.add("小鬼");

        Collections.shuffle(pokerBox);

        ArrayList<String>player1 = new ArrayList<>();
        ArrayList<String>player2 = new ArrayList<>();
        ArrayList<String>player3 = new ArrayList<>();
        ArrayList<String>dipai = new ArrayList<>();

        for (int i = 0; i < pokerBox.size(); i++) {
            String card = pokerBox.get(i);
            if (i >= 51){
                dipai.add(card);
            }else {
                if (i % 3 == 0){
                    player1.add(card);
                }else if (i % 3 == 1){
                    player2.add(card);
                }else {
                    player3.add(card);
                }
            }
        }
        System.out.println("张山:" + player1);
        System.out.println("李四:" + player2);
        System.out.println("王五:" + player3);
        System.out.println("底牌:" + dipai);*/

        /*Calendar time = Calendar.getInstance();
        time.get(Calendar.HOUR_OF_DAY);
        time.get(Calendar.MINUTE);
        time.get(Calendar.SECOND);
        System.out.println(time.get(Calendar.HOUR_OF_DAY)+":"+time.get(Calendar.MINUTE)+":"+time.get(Calendar.SECOND));*/
        /*Date date = new Date();
        DateFormat df2 = DateFormat.getDateTimeInstance();//可以精确到时分秒
        String time = df2.format(date);
        String[] s = time.split(" ");
        System.out.println(s[1]);
        System.out.println("====================");*/
        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        System.out.println(format.split(" ")[1]);
        System.out.println("====================");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sdf.format(new Date()));*/

       /* String name = "王剑支付宝转账";
        int i = name.indexOf("支付宝");
        String s = null;
        if (name.contains("账")){
            s = name.substring(0,i);
        }else {
            s = name.substring(0,i);
        }

        System.out.println(s);*/


                String str = "14.27.12";
                String pattern = "\\d{2}\\.\\d{2}\\.\\d{2}";

                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(str);
                System.out.println(m.matches());
            }
        }