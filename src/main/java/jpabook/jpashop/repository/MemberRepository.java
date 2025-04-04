package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member); // DB에 INSERT
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); // 조회, 인수 : 타입클래스, pk
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class) // Member 하나씩 조회해서 Member 객체로 저장, jpal은 from에 엔티티 객체 써야함
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name",
                        Member.class)
                .setParameter("name", name) // 쿼리 name 설정
                .getResultList();
    }
}
