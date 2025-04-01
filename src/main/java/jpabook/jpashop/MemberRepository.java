package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext // EntityManager 자동 주입
    EntityManager em; // 데이터베이스와 상호작용하는 객체

    public Long save(Member member) {
        em.persist(member); // DB에 저장, 바로 저장되는건 아니고 영속성컨텍스트(persistence context)에 등록했다가 트랜잭션이 커밋될 때 INSERT
        return member.getId(); // id(pk) 리턴
    }

    public Member find(Long id) {
        return em.find(Member.class, id); // id(pk)를 기준으로 조회, 영속성컨텍스트에서 찾고 없으면 DB에서 찾는다 (SELECT)
    }
}
