/**
 * 
 */
package de.bitech.javaday.spring.cache.aspect;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * CacheAspect verfolgt den deklarativen Ansatz analog {@link Cacheable}, ist aber weder an JDK-Proxy (nur Aufrufe von
 * au&szlig;en), noch an LoadTimeWeaving gebunden.
 * <p/>
 * <code>
 * cache:annotation-driven mode="aspectj"<br/>
 * context:load-time-weaver aspectj-weaving="on"
 * </code><br/>
 * nicht notwendig
 * 
 * @author awi
 * @since 03.07.2016
 * @version 0.0.1-SNAPSHOT
 *
 */
@Aspect
public class CacheAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheAspect.class);

	@Autowired
	@Qualifier("cacheManager")
	private CacheManager cacheManager;

	@Autowired
	@Qualifier("keyGenerator")
	private KeyGenerator keyGenerator;

	private Cache myCache;

	@PostConstruct
	public void init() {
		this.myCache = this.cacheManager.getCache("myCache");
	}

	@Pointcut("execution(* *.*(..))")
	public void allExecutions() {
		// NOP
	}

	@Pointcut("@annotation(de.bitech.javaday.spring.cache.aspect.MyCache)")
	public void myCache() {
		// NOP
	}

	@Pointcut("allExecutions() && myCache()")
	public void execMyCache() {
		// NOP
	}

	@SuppressWarnings("unchecked")
	@Around("execMyCache()")
	public Object aroundExecMyCache(ProceedingJoinPoint pProceedingJoinPoint) throws Throwable {
		Object target = pProceedingJoinPoint.getTarget();

		if (target == null) {
			LOGGER.warn("this aspect cannot be used with static method");
			return pProceedingJoinPoint.proceed();
		}

		LOGGER.debug("CacheAspect JoinPoint");

		Object ret = null;

		Class<?>[] parameterTypes = LogHelper.fetchParameterTypes(pProceedingJoinPoint.getSignature());

		Method method;
		String methodName = pProceedingJoinPoint.getSignature().getName();
		String className = pProceedingJoinPoint.getSignature().getDeclaringTypeName();
		try {
			method = pProceedingJoinPoint.getSignature().getDeclaringType().getDeclaredMethod(methodName, parameterTypes);

			Object[] params = pProceedingJoinPoint.getArgs();
			
			// condition
			int number = (int) params[0];
			if(number <= 1000000) {
				ret = pProceedingJoinPoint.proceed();
			} else {
				Object key = this.keyGenerator.generate(target, method, params);
				ValueWrapper valueWrapper = this.myCache.get(key);
				if (valueWrapper == null) {
					LoggerFactory.getLogger(className).info("{}: kein Cache-Eintrag fuer Key[{}] gefunden.", methodName, key);
					ret = pProceedingJoinPoint.proceed();
					this.myCache.put(key, ret);
				} else {
					LoggerFactory.getLogger(className).info("{}: Cache fuer Key[{}] gelesen.", methodName, key);
					ret = valueWrapper.get();
				}
			}

			return ret;

		} catch (NoSuchMethodException e) {
			LOGGER.warn("method {} not found", methodName);
			return pProceedingJoinPoint.proceed();
		}

	}

}
