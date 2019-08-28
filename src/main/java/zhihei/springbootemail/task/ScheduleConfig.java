package zhihei.springbootemail.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Created with IntelliJ IDEA.
 * Author: zky
 * Date: 2019-03-12
 * Time: 16:07:55
 * Description:
 */
public class ScheduleConfig implements SchedulingConfigurer
{

    private Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar)
    {
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler());
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("dispatch-");
        scheduler.setAwaitTerminationSeconds(600);
        scheduler.setErrorHandler(throwable -> logger.error("调度任务发生异常", throwable));
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }
}
