/**
 * 
 */
package de.bitech.javaday.spring.cache;

import java.util.List;

/**
 * @author awi
 * @since 03.07.2016
 * @version 0.0.1-SNAPSHOT
 *
 */
public interface MySlowlyService {
  
  List<Integer> factorization(int pNumber);

}
