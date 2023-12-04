package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))
    // objectはcollectionがあるから多対多関係が可能が、DBはできないから
    // 1対多、対多1関係に解かれる中間Tableが必要
    // **実務では中間Table(CATEGORY_ITEM)にcolumnを追加することができず、
    // 細かくqueryを実行することが難しいため、実務で使用するには限界があります
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==連関関係メソッド==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
