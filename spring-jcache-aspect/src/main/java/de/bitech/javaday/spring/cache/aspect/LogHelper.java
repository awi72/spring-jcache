/**
 * 
 */
package de.bitech.javaday.spring.cache.aspect;

import java.lang.reflect.Array;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author awi
 * @since 03.07.2016
 * @version 0.0.1-SNAPSHOT
 *
 */
public class LogHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogHelper.class);

	private LogHelper() {
		// NOP
	}

	/**
	 * use in advice to read the attributes within a annotation
	 * 
	 * @param pSignature
	 *          from JoinPoint
	 * @return all Parameter-Types
	 */
	public static Class<?>[] fetchParameterTypes(Signature pSignature) {
		String params = StringUtils.substringBetween(pSignature.toLongString(), "(", ")");

		StringTokenizer tokenizer = new StringTokenizer(params, ",");
		Class<?>[] ret = new Class<?>[tokenizer.countTokens()];

		for (int i = 0; i < ret.length; i++) {
			String parameterName = tokenizer.nextToken().trim();
			try {
				ret[i] = fetchClass(parameterName);
			} catch (ClassNotFoundException e) {
				String msg = String.format("could not identify class with className=%s. Error Message=%s", parameterName,
						e.getMessage());
				LOGGER.error(msg, e);
			}

		}

		return ret;
	}

	/**
	 * 
	 * @param pLongName
	 *          full qualified class name
	 * @return Class for name
	 * @throws ClassNotFoundException
	 *           ClassNotFound
	 */
	public static Class<?> fetchClass(String pLongName) throws ClassNotFoundException {
		Class<?> ret = null;
		LOGGER.debug("fetchClass for {}", pLongName);
		if (!pLongName.contains(".")) {
			ret = fetchPrimitiveClass(pLongName);
		} else if (pLongName.endsWith("[]")) {
			Class<?> clazz = Class.forName(StringUtils.substringBefore(pLongName, "["));
			ret = Array.newInstance(clazz, 0).getClass();
		} else {
			// inner class ?
			String[] split = StringUtils.split(pLongName, ".");
			String left = StringUtils.left(split[split.length - 2], 1); // last package or inner class
			if (StringUtils.isAllUpperCase(left)) { // inner class
				StringBuilder sb = new StringBuilder();
				sb.append(StringUtils.substringBeforeLast(pLongName, "."));
				sb.append("$");
				sb.append(StringUtils.substringAfterLast(pLongName, "."));
				ret = Class.forName(sb.toString());
			} else {
				ret = Class.forName(pLongName);
			}

		}
		return ret;
	}

	/**
	 * 
	 * @param pPrimitiveName
	 *          simple name of a primitive type
	 * @return class that represents the primitiveName type
	 */
	public static Class<?> fetchPrimitiveClass(String pPrimitiveName) {
		Class<?> ret = null;
		switch (pPrimitiveName) {
		case "boolean":
			ret = boolean.class;
			break;
		case "boolean[]":
			ret = boolean[].class;
			break;
		case "char":
			ret = char.class;
			break;
		case "char[]":
			ret = char[].class;
			break;
		case "byte":
			ret = byte.class;
			break;
		case "byte[]":
			ret = byte[].class;
			break;
		case "short":
			ret = short.class;
			break;
		case "short[]":
			ret = short[].class;
			break;
		case "int":
			ret = int.class;
			break;
		case "int[]":
			ret = int[].class;
			break;
		case "long":
			ret = long.class;
			break;
		case "long[]":
			ret = long[].class;
			break;
		case "float":
			ret = float.class;
			break;
		case "float[]":
			ret = float[].class;
			break;
		case "double":
			ret = double.class;
			break;
		case "double[]":
			ret = double[].class;
			break;
		default:
			LOGGER.warn("unexpected primitive type: {}", pPrimitiveName);
			break;
		}
		return ret;
	}

}
