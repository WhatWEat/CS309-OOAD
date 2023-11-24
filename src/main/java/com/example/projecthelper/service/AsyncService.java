package com.example.projecthelper.service;

import com.example.projecthelper.util.AsyncTask;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    private final AsyncConfigurer asyncConfigurer;

    @Autowired
    public AsyncService(AsyncConfigurer asyncConfigurer) {
        this.asyncConfigurer = asyncConfigurer;
    }

    // 假设这是你要异步执行的任务
    public void executeTasks(List<AsyncTask> ats, int thd_cnt) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(thd_cnt); // 20 tasks
        for (int i = 0; i < thd_cnt; i++) {
            int finalI = i;
            System.err.println("thread" + i);
            asyncConfigurer.getAsyncExecutor().execute(() -> {
                try {
                    // Task logic here
                    ats.get(finalI).doTask();
                } finally {
                    latch.countDown();
                }
            });
            System.err.println("thread" + i);
        }
        latch.await(); // Wait for all tasks to complete
    }
}
