package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // JPQL
    public List<Order> findAll(OrderSearch orderSearch) {

        //language=JPQL
        String jpql = "select o From Order o join o.member m"; // Order와 Member를 조인

        boolean isFirstCondition = true;

        // 주문 상태가 검색 조건에 있을 경우
        if (orderSearch.getOrderStatus() != null) {
            // 첫 번째 조건이면 where를 붙이고, 아니면 and를 붙임
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            // 상태 조건 추가 (파라미터 바인딩 형태로)
            jpql += " o.status = :status";
        }

        // 회원 이름이 검색 조건에 있을 경우
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            // 첫 번째 조건이면 where를 붙이고, 아니면 and를 붙임
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            // 이름 조건 추가 (like 검색으로 부분 일치 가능)
            jpql += " m.name like :name";
        }

        // TypedQuery 생성 (최대 1000건 제한)
        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        // 파라미터가 존재할 경우 바인딩
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        // 결과 리스트 반환
        return query.getResultList();
    }

    // JPA Criteria
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        // CriteriaBuilder 객체
        CriteriaBuilder cb = em.getCriteriaBuilder();
        // 반환 타입이 Order인 쿼리 객체 생성
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        // 쿼리의 루트 (from 절에 해당), Order 엔티티를 기준으로 시작
        Root<Order> o = cq.from(Order.class);
        // member 연관 엔티티와 조인 (INNER JOIN 방식)
        Join<Order, Member> m = o.join("member", JoinType.INNER);
        // 검색 조건들을 담을 리스트
        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태가 검색 조건에 있을 경우
        if (orderSearch.getOrderStatus() != null) {
            // "order.status = :status" 조건 생성
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status); // 조건 리스트에 추가
        }

        // 회원 이름이 검색 조건에 있을 경우
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            // "member.name like %이름%" 조건 생성 (부분 검색)
            Predicate name = cb.like(m.<String>get("name"),
                    "%" + orderSearch.getMemberName() + "%");
            criteria.add(name); // 조건 리스트에 추가
        }

        // 위에서 모은 조건들을 and로 묶어 where 절에 설정
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        // 완성된 CriteriaQuery로 쿼리 실행, 결과는 최대 1000건으로 제한
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);

        // 결과 리스트 반환
        return query.getResultList();
    }

}
