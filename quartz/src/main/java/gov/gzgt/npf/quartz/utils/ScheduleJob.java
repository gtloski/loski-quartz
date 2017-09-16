package gov.gzgt.npf.quartz.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gzgt.npf.quartz.entity.ScheduleJobEntity;

public class ScheduleJob implements Job{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ExecutorService service = Executors.newSingleThreadExecutor(); 

	public void execute(JobExecutionContext context){
		ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap()
        		.get(ScheduleJobEntity.JOB_PARAM_KEY);
		//任务开始时间
        long startTime = System.currentTimeMillis();
        try {
            //执行任务
        	logger.info("任务准备执行，任务ID：" + scheduleJob.getJobId());
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(), 
            		scheduleJob.getMethodName(), scheduleJob.getParams());
            Future<?> future = service.submit(task);
			future.get();
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			logger.info("任务执行完毕，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");
		} catch (Exception e) {
			long times = System.currentTimeMillis() - startTime;
			logger.info("任务执行失败，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");
			logger.debug(e.getMessage());
		}
	}

}
