package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "orders") // 테이블 이름 지정
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 지연로딩
    @JoinColumn(name = "member_id") // member_id가 fk
    private Member member; // 주문 회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 일대다,OrderItem의 order가 관계 주인, OrderItem이 Order를 참조, Order의 상태 변화가 OrderItem에도 전파
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 일대일, 지연로딩 엔티티가 실제로 필요할 때 로딩
    @JoinColumn(name = "delivery_id") // delivery_id가 fk
    private Delivery delivery; // 배송정보
    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING) // 속성이 enum 질때 DB에 저장할 방법 지정 (enum 문자열 이름)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
