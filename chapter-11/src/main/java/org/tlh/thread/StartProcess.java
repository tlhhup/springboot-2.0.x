package org.tlh.thread;

import org.apache.logging.log4j.ThreadContext;
import org.tlh.thread.task.TestCaseTask;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 离歌笑tlh/hu ping on 2019/3/3
 * <p>
 * Github: https://github.com/tlhhup
 */
public class StartProcess {

    public static void main(String[] args) {
        ThreadContext.put("fileName", "logs/file.txt");
        System.setProperty("fileName","detail.txt");
        try {
            int threadNumber = 10;

            ExecutorService service = Executors.newCachedThreadPool();
            CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNumber, () -> {
                System.out.println("所有线程启动：" + System.currentTimeMillis());
            });
            //通过创建一个资源来控制第一个线程执行完，统计时间
            CountDownLatch countDownLatch = new CountDownLatch(1);

            for (int i = 1; i <= threadNumber; i++) {
                service.submit(new TestCaseTask(i, cyclicBarrier, countDownLatch));
            }
            service.shutdown();//等待所有线程执行完
            //等待第一个线程完成
            countDownLatch.await();
            System.out.println("第一个线程执行完：" + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
