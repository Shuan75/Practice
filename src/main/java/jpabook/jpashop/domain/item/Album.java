package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("A") // single_table戦略でDBの立場で区別
public class Album extends Item{

    private String artist;
    private String etc;
}
