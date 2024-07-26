package org.example.coupon.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class Coupon {

    @Id
    @Comment("쿠폰 식별자")
    @Column(name = "coupon_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "coupon_name")
    @Comment("쿠폰 이름")
    private String name;

    @Column(name = "coupon_quantity")
    @Comment("쿠폰 수량")
    private Long quantity;

    @Column(name = "coupon_issued_quantity")
    @Comment("쿠폰 발급 수량")
    private Long issuedQuantity;


    /**
     *  쿠폰 발급 로직
     */
    public void issueCoupon() {
        if (this.quantity > 0) {
            this.quantity--;
            this.issuedQuantity++;
        }
    }

}
