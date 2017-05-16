/**
* 
*/
package de.bitech.javaday.spring.cache;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author awi
 * @since 03.07.2016
 * @version 0.0.1-SNAPSHOT
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/bitech/javaday/spring/context-cache.xml")
@DirtiesContext
// @ActiveProfiles("jdk-proxy")
public class MyTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyTest.class);

	@Autowired
	private MySlowlyService mySlowlyService;

	@Test
	public void firstCalculation() {
		for (int i = 0; i < 3; i++) {
			Assert.assertEquals(Arrays.asList(3, 715827881), this.mySlowlyService.factorization(2147483643));
			LOGGER.info("loop:{}", i + 1);
		}
	}

	@Test
	public void secondCalculation() {
		for (int i = 0; i < 5; i++) {
			Assert.assertEquals(Arrays.asList(3, 5, 823), this.mySlowlyService.factorization(12345));
			LOGGER.info("loop:{}", i + 1);
		}
	}
	
	@Test
	@Ignore
	public void findDuration() {
		for(int i = Integer.MAX_VALUE; i > 649657 ;i--) {			
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			List<Integer> result = this.mySlowlyService.factorization(i);
			stopWatch.stop();
			long duration = stopWatch.getTime();
			if(duration > 1000 && duration < 9000) {				
				LOGGER.info("duration:{}, in:{}, result:{}", stopWatch.getTime(), i, result);
			}
		}
	}

}
