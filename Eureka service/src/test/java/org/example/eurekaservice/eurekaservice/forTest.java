package org.example.eurekaservice.eurekaservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("일반 테스트")
@SpringBootTest
class forTest {

	private final String url = "http://localhost:9000/api/coupon";

	@Test
	public void test() throws InterruptedException {
		int numberOfAttempts = 30000; // 30000번의 쿠폰 발급 시도
		CountDownLatch endLatch = new CountDownLatch(numberOfAttempts);
		ExecutorService thread = Executors.newFixedThreadPool(32);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		for (int i = 0; i < numberOfAttempts; i++) {
			thread.submit(() -> {
				try {
					restTemplate.postForEntity(url+"/issue", headers, String.class);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					endLatch.countDown();
				}
			});
		}

		endLatch.await();

		Long quantity = restTemplate.getForObject(url+"/quantity/1", long.class);
		assertEquals(quantity, 0L);


	}

}
