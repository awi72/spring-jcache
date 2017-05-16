/**
 * 
 */
package de.bitech.javaday.spring.cache.aspect;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

/**
 * 
 * @author awi
 * @since 03.07.2016
 * @version 0.0.1-SNAPSHOT
 *
 */
public class MyKeyGenerator implements KeyGenerator {

	private static final String SEPARATOR = "|";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.cache.interceptor.KeyGenerator#generate(java.lang.Object, java.lang.reflect.Method,
	 * java.lang.Object[])
	 */
	@Override
	public Object generate(Object pTarget, Method pMethod, Object... pParams) {

		StringBuilder sb = new StringBuilder();
		sb.append(pTarget.getClass().getSimpleName()).append(SEPARATOR);
		sb.append(pMethod.getName());

		for (int i = 0; i < pParams.length; i++) {
			Object param = pParams[i];
			String keyPart = param.getClass().isAssignableFrom(int.class) ? String.valueOf(param) : param.toString();
			sb.append(SEPARATOR).append(keyPart);
		}

		return sb.toString();
	}

}
