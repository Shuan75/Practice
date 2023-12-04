package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // protected OrderItem() {} 同じ
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    // 一つのorderが複数のItemを持つことができますので ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    //==生成メソッド==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem(); // 後でcouponとか割引可能性があるから　new constructor
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;

    }

    //==Busniess Rogic==//

    // cancle時OrderItemにもcancleを伝送
    public void cancle() {
        getItem().addStock(count); // 在庫数量reset
    }

    // 注文する時、価額と数量が必要
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
