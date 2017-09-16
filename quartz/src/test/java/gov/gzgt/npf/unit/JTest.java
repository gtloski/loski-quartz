package gov.gzgt.npf.unit;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gzgt.npf.quartz.entity.ScheduleJobEntity;
import gov.gzgt.npf.quartz.utils.ScheduleJob;

public class JTest {
	
	private static Logger logger = LoggerFactory.getLogger(JTest.class);
	
	private final static String JOB_NAME = "TASK_";

    public void createScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        try {
        	//构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(scheduleJob.getJobId())).build();

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
            		.withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob.getJobId())).withSchedule(scheduleBuilder).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleJobEntity.JOB_PARAM_KEY, scheduleJob);
            
            scheduler.scheduleJob(jobDetail, trigger);
            
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
	
	public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }
	
	public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }
	
	@Test
	public void test(){
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			//新建任务
//			ScheduleJobEntity scheduleJob = new ScheduleJobEntity();
//            scheduleJob.setBeanName("gov.gzgt.npf.quartz.job.TsstJob");
//            scheduleJob.setMethodName("test");
//            scheduleJob.setCreateTime(new Date());
//            scheduleJob.setCronExpression("0 */1 * * * ?");
//            scheduleJob.setJobId(486913L);
//            scheduleJob.setParams("liguitian");
//            scheduleJob.setRemark("biaozu");
//            scheduleJob.setStatus(1);
//            createScheduleJob(scheduler, scheduleJob); 
            try {
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception se) {
        	se.printStackTrace();
            logger.error(se.getMessage(), se);
        }
	}
}
