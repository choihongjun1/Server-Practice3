package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue // pk 설정, pk 자동생성
    @Column(name = "member_id") // 테이블 컬럼 이름
    private Long id;

    private String name;

    @Embedded // 다른 클래스를 속성으로 포함
    private Address address;

    @OneToMany(mappedBy = "member") // 일대다, Order의 member가 관계 주인, Order가 Member를 참조
    private List<Order> orders = new ArrayList<>();
}
