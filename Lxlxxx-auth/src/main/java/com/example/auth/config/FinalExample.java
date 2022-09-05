package com.example.auth.config;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FinalExample {

    //引用变量
    final int[] ints ;

  static FinalExample object;

    public FinalExample() {
        ints =new int[1];
        ints[0]=1;
    }

    public static void writer(){
        object = new FinalExample();
        int temp1 =object.ints[0];
        System.out.println("ints数组第一次写的值为："+temp1);
    }
    public static void writerTwo(){
        object.ints[0]=2;
        int temp2 =object.ints[0];
        System.out.println("ints数组第二次写的值为："+temp2);
    }
    public static void reader(){
        if (object!=null){
            int temp3 =object.ints[0];
            System.out.println("ints数组第一个值为："+temp3);
        }
    }

    public static void main(String[] args) {
        Writer writer = new Writer();
        WriterTwo writerTwo =new WriterTwo();
        Reader reader =new Reader();
        new Thread(writer,"线程A").start();
        new Thread(writerTwo,"线程B").start();
        new Thread(reader,"线程C").start();
        Thread thread1 =new Thread(()->{

        },"线程D");

    }

     static class Writer implements Runnable{
        @Override
        public void run() {

            try {
                System.out.println(Thread.currentThread().getName()+"执行第一次写的操作");
                writer();

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

     static class WriterTwo implements Runnable{
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+"执行第二次写的操作");
                writerTwo();

                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

     static class Reader implements Runnable{
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+"执行读数据操作");
                reader();

                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void getAsync() throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture aFuture = CompletableFuture.supplyAsync(() -> {

            return "执行A服务返回结果";
        }, executor);
        CompletableFuture bFuture = CompletableFuture.supplyAsync(() -> {

            return "执行A服务返回结果";
        }, executor);
        CompletableFuture cFuture = CompletableFuture.supplyAsync(() -> {

            return "执行A服务返回结果";
        }, executor);

        CompletableFuture.allOf(aFuture, bFuture, cFuture).join();

        aFuture.get();
        bFuture.get();
        cFuture.get();

    }
}

