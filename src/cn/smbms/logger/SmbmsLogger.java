package cn.smbms.logger;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

//增强类，将日志增强写到此类中
public class SmbmsLogger {

	private Logger logger = Logger.getLogger(SmbmsLogger.class);

	// 前置增强
	public void before(JoinPoint jp) {
		logger.info("您调用的是" + jp.getTarget() + "类中的方法，方法名是"
				+ jp.getSignature().getName() + "入参是"
				+ Arrays.toString(jp.getArgs()));
	}
	// 后置增强
	public void afterReturning(JoinPoint jp, Object result) {
		logger.info("您调用的是" + jp.getTarget() + "类中的方法，方法名是"
				+ jp.getSignature().getName() + "，方法执行完返回值是"
				+ result);
	}
	// 异常抛出增强
	public void afterThrowing(JoinPoint jp, Exception e) {
		logger.error("您调用的是" + jp.getTarget() + "类中的方法，方法名是"
				+ jp.getSignature().getName() + "，方法执行过程中报错，错误信息是"
				+ e.getMessage());
	}
	
	// 最终增强
	public void after(JoinPoint jp) {
		logger.error("您调用的是" + jp.getTarget() + "类中的方法，方法名是"
				+ jp.getSignature().getName() + "，方法执行完毕");
	}

}