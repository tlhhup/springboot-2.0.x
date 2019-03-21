package org.tlh.thread.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by 离歌笑tlh/hu ping on 2019/3/3
 * <p>
 * Github: https://github.com/tlhhup
 */
public class TestCaseTask implements Runnable {

    private int processId;

    private CyclicBarrier allStart;
    private CountDownLatch firstEnd;
    private Logger detail;;

    public TestCaseTask(int processId, CyclicBarrier allStart, CountDownLatch firstEnd) {
        this.processId = processId;
        this.allStart = allStart;
        this.firstEnd = firstEnd;

        detail = LogManager.getLogger("detail");
    }

    @Override
    public void run() {
        try {
            //1.等待所有线程启动
            this.allStart.await();
            Thread.sleep(2*1000);
            System.out.println(Thread.currentThread().getName()+":"+processId);
            //2.计数器减1,如果已经为0，此时减一没有问题,
            /**
             *
             *  protected boolean tryReleaseShared(int releases) {
                     // Decrement count; signal when transition to zero
                     for (;;) {
                         int c = getState();
                         if (c == 0) //0时返回false
                            return false;
                         int nextc = c-1;
                         if (compareAndSetState(c, nextc))
                            return nextc == 0;
                     }
                 }
             *
             */
            this.firstEnd.countDown();
            detail.info(Thread.currentThread().getName()+":"+processId+"退出:"+System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
