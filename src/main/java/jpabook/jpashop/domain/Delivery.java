package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // 속성이 enum 일때 DB에 저장할 방법 지정 (enum 문자열 이름으로 저장)
    private DeliveryStatus status; // ENUM [READY(준비), COMP(배송)]
}
