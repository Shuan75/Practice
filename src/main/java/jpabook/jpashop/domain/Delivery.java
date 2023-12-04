package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded // 内蔵Type
    private Address address;

    @Enumerated(EnumType.STRING)
    // ORDINALは1,2,3という数字でcolumnに入れる
    // この方法は後で他の状態ができたらDB照会時XXXに表示される
    // だからSTRINGを使うこと
    private DeliveryStatus status; // REDAY, COMPLETE
}
