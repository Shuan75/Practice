package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 内蔵タイプを表現した、というannotation
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member") // 一人の会員がいくつのorderができるのでOneToMany
    // order tableにあるmember filedによってmappingになったという表現
    private List<Order> orders = new ArrayList<>();
    // colltectionはfieldで初期化するのが安全
    // NPE問題から安全
    // hibernateがentityをpersistする時hibernateが提供する内蔵collectionで変更する
    // だから他の開発者がset collectionで変更すると問題が発生
    // よってfieldで初期化するのが一番安全で簡潔
}
