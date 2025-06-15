package com.mygo.bangmall.search.thread;

import java.util.concurrent.*;

public class ThreadTest {
    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start");
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            System.out.println(Thread.currentThread().getName());
//            int i = 10 / 2;
//            System.out.println(i);
//        }, executor);

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            int i = 10 / 2;
            System.out.println(i);
            return i;
        }, executor);
        Integer integer = future.get();
        System.out.println("main...end" + integer);
    }

    public  void threads(String[] args) {
        System.out.println("main...start");
//        Thread01 thread01 = new Thread01();
//        thread01.start();
//        Runnable01 runnable = new Runnable01();
//        Thread thread = new Thread(runnable);
//        thread.start();
//        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
//        new Thread(futureTask).start();
        //service.execute(new Runnable01());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        System.out.println("main...end");


    }

    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            int i = 10 / 2;
            System.out.println(i);
        }
    }

    public static class Runnable01 implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            int i = 10 / 2;
            System.out.println(i);
        }
    }

    public static class Callable01 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName());
            int i = 10 / 2;
            System.out.println(i);
            return i;
        }
    }
}
