package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 親子関係戦略
// 一つのtableに全部入れる , JOINED = 正規化style , Table_Per_Classは　ex)book , movie , albumという三つで分ける戦略
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item { // Itemの具現体を作成させるるために抽象Classを宣言

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity; // 在庫数量

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // Business Rogic
    // dataを持ってる所にBusiness methodがある方が凝集力高い
    // だからstockQuantityを持ってるItem Entityにある方が管理が良い

    // 在庫数量増加
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 在庫数量減少
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0 ) { // 在庫が０より少ないとダメだからexception処理
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
