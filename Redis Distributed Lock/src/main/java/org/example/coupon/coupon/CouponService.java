package org.example.coupon.coupon;

public interface CouponService {
    // 쿠폰 발급
    void issueCoupon(Long couponSeq);
    // 남은 쿠폰 수량
    long getCouponQuantity(Long couponSeq);
    // 발급 쿠폰 수량
    long getIssuedCouponQuantity(Long couponSeq);
    // 락을 적용한 쿠폰 발급
    void issueCouponWithLock(long couponSeq);
    // 락과 트랜잭션을 적용한 쿠폰 발급
    void issueCouponWithLockInTransaction(long couponSeq);
    // 어노테이션을 이용한 쿠폰 발급
    void issueCouponWithAnnotation(long couponSeq);
}
