package gov.gzgt.npf.quartz.utils;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleRunnable implements Runnable {
	
	private Logger logger = (Logger) LoggerFactory.getLogger(getClass());

	private Object target;
	
	private Method method;
		
	private String params;
	
	public ScheduleRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException {
		try {
			this.target = Class.forName(beanName).newInstance();
			this.params = params;
			if(StringUtils.isNotBlank(params)){
				this.method = target.getClass().getDeclaredMethod(methodName, String.class);
			}else{
				this.method = target.getClass().getDeclaredMethod(methodName);
			}
		} catch (ClassNotFoundException e) {
			logger.debug("找不到映射的类："+beanName);
		} 
	}

	public void run() {
		try {
			if(StringUtils.isNotBlank(params)){
				method.invoke(target, params);
			}else{
				method.invoke(target);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("执行任务失败");
		}
	}

}
