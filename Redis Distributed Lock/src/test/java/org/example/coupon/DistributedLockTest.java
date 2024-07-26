package org.example.coupon;

import org.example.coupon.coupon.Coupon;
import org.example.coupon.coupon.CouponRepository;
import org.example.coupon.coupon.CouponService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Redis 분산 락 테스트")
@SpringBootTest
public class DistributedLockTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void 분산_락_테스트() throws InterruptedException {
        couponRepository.save(new Coupon(1L, "테스트 쿠폰", 1000L, 0L));
        ExecutorService thread = Executors.newFixedThreadPool(32); // 32개 스레드 사용
        int numberOfAttempts = 30000; // 30000번의 쿠폰 발급 시도

        CountDownLatch endLatch = new CountDownLatch(numberOfAttempts);

        for (int i = 0; i < numberOfAttempts; i++) {
            thread.submit(() -> {
                try {
                    couponService.issueCouponWithLockInTransaction(1L); // 락을 적용한 쿠폰 발급
                } finally {
                    endLatch.countDown();
                }
            });
        }

        endLatch.await();

        long quantity = couponService.getCouponQuantity(1L);

        assertEquals(0, quantity);
    }

    @Test
    public void 일반_테스트() throws InterruptedException {
        couponRepository.save(new Coupon(2L, "테스트 쿠폰", 1000L, 0L));
        ExecutorService thread = Executors.newFixedThreadPool(32); // 32개 스레드 사용
        int numberOfAttempts = 30000; // 30000번의 쿠폰 발급 시도

        CountDownLatch endLatch = new CountDownLatch(numberOfAttempts);

        for (int i = 0; i < numberOfAttempts; i++) {
            thread.submit(() -> {
                try {
                    couponService.issueCoupon(2L); // 일반 쿠폰 발급
                } finally {
                    endLatch.countDown();
                }
            });
        }

        endLatch.await();

        long quantity = couponService.getCouponQuantity(2L);

        assertEquals(0, quantity);
    }


}
