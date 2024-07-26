package org.example.coupon.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.config.DistributedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final RedissonClient redissonClient;
    private final CouponTransactionService couponTransactionService;


    @Transactional
    @Override
    public void issueCoupon(Long couponSeq) {
        Coupon coupon = couponRepository.findById(couponSeq).orElseThrow(()-> new RuntimeException("쿠폰이 존재하지 않습니다."));
        coupon.issueCoupon();
        couponRepository.save(coupon);
    }

    @Transactional(readOnly = true)
    @Override
    public long getCouponQuantity(Long couponSeq) {
        Coupon coupon = couponRepository.findById(couponSeq).orElseThrow(()-> new RuntimeException("쿠폰이 존재하지 않습니다."));
        return coupon.getQuantity();
    }

    @Transactional(readOnly = true)
    @Override
    public long getIssuedCouponQuantity(Long couponSeq) {
        Coupon coupon = couponRepository.findById(couponSeq).orElseThrow(()-> new RuntimeException("쿠폰이 존재하지 않습니다."));
        return coupon.getIssuedQuantity();
    }

    /**
     *  기존 쿠폰 발급 로직
     * @param couponSeq 쿠폰 식별자
     */
    @Transactional
    @Override
    public void issueCouponWithLock(long couponSeq) {
//        log.info("[로직 미 분리] 쿠폰 발급을 시작합니다.");
        RLock rLock = redissonClient.getLock("coupon");
        try {
            boolean available = rLock.tryLock(5L, 3L, TimeUnit.SECONDS);
            if (available) {
                Coupon coupon = couponRepository.findById(couponSeq).orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다."));
                coupon.issueCoupon();
                couponRepository.save(coupon);
            }
        } catch (InterruptedException e) {
//            log.warn("[로직 미 분리] 쿠폰 발급 중 문제가 발생했습니다. {}", e.getMessage());
        } finally {
            rLock.unlock();
//            log.info("[로직 미 분리] 쿠폰 발급을 완료했습니다.");
        }
    }

    /**
     *  분리된 쿠폰 발급 로직
     * @param couponSeq 쿠폰 식별자
     */
    @Override
    public void issueCouponWithLockInTransaction(long couponSeq) {
//        log.info("[로직 분리] 쿠폰 발급을 시작합니다.");
        RLock rLock = redissonClient.getLock("coupon");
        try {
            boolean available = rLock.tryLock(5L, 3L, TimeUnit.SECONDS);
            if (available) {
                couponTransactionService.issueCouponInTransaction(couponSeq);
            }
        } catch (InterruptedException e) {
//            log.warn("[로직 분리] 쿠폰 발급 중 문제가 발생했습니다. {}", e.getMessage());
        } finally {
            rLock.unlock();
//            log.info("[로직 분리] 쿠폰 발급을 완료했습니다.");
        }
    }

    /**
     *  어노테이션을 이용한 쿠폰 발급
     * @param couponSeq 쿠폰 식별자
     */
    @DistributedLock(key = "#couponSeq")
    @Override
    public void issueCouponWithAnnotation(long couponSeq) {
        Coupon coupon = couponRepository.findById(couponSeq).orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다."));
        coupon.issueCoupon();
        couponRepository.save(coupon);
    }

}

