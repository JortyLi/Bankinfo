package com.example.bankinfo.common.single;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chengzi on 2019/7/11 1:11
 */
public class DeadLock {

    public static void main(String[] args) throws Exception {
        final List<Integer>list1 = Arrays.asList(1,2,3);
        final List<Integer>list2 = Arrays.asList(4,5,6);

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (list1){
                    for (Integer integer : list1) {
                        System.out.println(integer);
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (list2){
                        for (Integer integer : list2) {
                            System.out.println(integer);
                        }
                    }
                }
            }
        }).start();



        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (list2){
                    for (Integer integer : list2) {
                        System.out.println(integer);
                    }

                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                synchronized (list1){
                    for (Integer integer : list1) {
                        System.out.println(integer);
                    }
                }
            }
        }).start();
    }

}
