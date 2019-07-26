package com.example.bankinfo.common;

import org.springframework.util.ResourceUtils;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * 获取txt文件内容
     *
     * @param file txt文件
     * @return 字符
     */
    public static String getTxtData(File file) {
        StringBuffer data = new StringBuffer();
        try {
            if (file.isFile() && file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader br = new BufferedReader(reader);
                String line = "";
                line = br.readLine();
                while (line != null) {
                    data.append(line + "\r\n");
                    line = br.readLine(); // 一次读入一行数据
                }
                reader.close();
                return data.toString().substring(0, data.toString().length() - 2);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
    /**
     * 更新txt文件
     *
     * @param file txt文件
     * @param data 更新内容
     */
    public static void updateTxtData(File file, String data) {
        if (StringUtils.isEmpty(data)) return;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            BufferedWriter out = new BufferedWriter(write);
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据文件位置获取银行需要的数据
     *
     * @param startCode 截取开始位置
     * @param endCode   截取结束位置
     * @return List<String>
     */
    public static List<String> getBankInfoByFiles(String filePath, String startCode, String endCode, boolean isDelete) {
        //返回信息集合
        List<String> list = new ArrayList<String>();
        try {
            //文件夹位置取出所有文件
            File pathFile = ResourceUtils.getFile(filePath);
            for (File file : pathFile.listFiles()) {
                String fileInfo = getTxtData(file);
                if (isDelete) file.delete();//删除文件
                Integer startLine = fileInfo.indexOf(startCode);
                if (startLine < 0) {
                    continue;
                }
                Integer endLine = fileInfo.indexOf(endCode);
                String bankInfo = fileInfo.substring(startLine, endLine);

                list.add(bankInfo);
                System.out.println(bankInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
//        // get file list where the path has
//        File file = ResourceUtils.getFile("D:\\idea-project\\video-monitor\\src\\main\\resources\\server-caching");
//        getFile(file);

//        System.out.println(URLDecoder.decode("ni%0Adaye%0Ade%0Adaye%0A45898572394750923%0A39085293043%0A%E5%8F%91%E9%A1%BA%E4%B8%B0%0Ajgkjhglhlkhkjhlkj%0A","UTF-8"));
//        System.out.println("fuwuqi1_201803161613.txt".substring(0,"fuwuqi1_201803161613.txt".length()-4));
//        System.out.println("fuwuqi1_201803161613.txt".substring("fuwuqi1_201803161613.txt".length()-3,"fuwuqi1_201803161613.txt".length()));

//        System.out.println(FileUtil.getBankInfoByFiles("C:\\Users\\fengzi\\Desktop\\charles", "<form name=\"jhform\"", "</form", false).size());
//        System.out.println(ResourceUtils.getFile("C:\\Users\\fengzi\\Desktop\\charles\\charles201803311720.trace").delete());

//        System.out.println(URLDecoder.decode("%E7%8E%8B%E6%AD%A6%E5%8A%9B","UTF-8"));

//        String url = "C:\\Users\\jhq89\\Documents\\农行\\";
//        String str = FileUtil.getBankInfoByFiles(url, "{", "}", false);

    }
}
