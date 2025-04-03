package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B") // 구분 컬럼 값 지정
@Getter
@Setter
public class Book extends Item {
    private String author;
    private String isbn;
}
