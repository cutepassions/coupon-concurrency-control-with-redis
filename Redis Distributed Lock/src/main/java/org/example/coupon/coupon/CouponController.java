package org.example.coupon.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/issue")
    public void issueCoupon() {
        couponService.issueCoupon(1L);
    }

    @PostMapping("/issue-with-lock")
    public void lockIssueCoupon() {
        couponService.issueCouponWithLock(1L);
    }

    @PostMapping("/issue-with-lock-in-transaction")
    public void lockIssueCouponInTransaction() {
        couponService.issueCouponWithLockInTransaction(1L);
    }

    @GetMapping("/quantity/{couponSeq}")
    public long getCouponQuantity(@PathVariable Long couponSeq) {
        return couponService.getCouponQuantity(couponSeq);
    }

    @GetMapping("/issuedQuantity/{couponSeq}")
    public long getIssuedCouponQuantity(@PathVariable Long couponSeq) {
        return couponService.getIssuedCouponQuantity(couponSeq);
    }


}
