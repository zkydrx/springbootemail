package zhihei.springbootemail.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Author: zky
 * Date: 2019-03-12
 * Time: 16:06:22
 * Description:
 */
@Component
public class ScheduledPayTasks
{
    private Logger logger = LoggerFactory.getLogger(ScheduledPayTasks.class);

    @Scheduled(cron = "0 0 5,15 * * ? ")
    @Async
    public void sendEmailTask()
    {
        //每天的5:00,12:00,17:00自动发送email


    }


}
