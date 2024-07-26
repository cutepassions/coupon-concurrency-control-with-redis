package org.example.coupon.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponTransactionService {

    private final CouponRepository couponRepository;

    @Transactional
    public void issueCouponInTransaction(long couponSeq) {
        Coupon coupon = couponRepository.findById(couponSeq).orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다."));
        coupon.issueCoupon();
        couponRepository.save(coupon);
    }
}
