package cn.xyz.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class Task {
    //喇叭部生产喇叭产量及效率统计表推送
    @Scheduled(cron = "0 15 10 * * ?")
    private void task1() {
        System.err.println("执行静态定时任务时间: ");
    }
}
