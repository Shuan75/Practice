package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // entityとmapping tableを指定
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue // JPAがTableのPK値を自動的に生成してくれるannotation
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    // JPQL select * from order o; -> SQL select * from oder 100 + 1(order) N + 1 問題
    // memberを持ち出すために１件queryがN個が伝送するからEAGARは禁止
    // 必要時はFetch join , entity grape機能を使う
    @JoinColumn(name = "member_id") // mappingを指定　Fk
    // ＊＊FKがある場所を関連関係の主にする
    private Member member;


    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    //　persist(orderItemA),persist(orderItemB),persist(orderItemC) perist(order)
    // => perist(order)

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // OneToOneではどこでも構いませんが、長所と短所は存在する
    // object指向開発者はたくさん接近するtableにFkを指定する
    // このProjectでは普通Orderを見て配送(Delivery)をするからordersを主人に設定
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDateTime; // 注文時間

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 注文状態　[ORDER, CANCEL]

    //==連関関係メソッド==//
    // 双方向の時に使う
    public void setMember(Member member) {
        this.member= member;
        member.getOrders().add(this);
        // member.getOrders().add(order); , order.setMember(member);　二つを合わせる
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==生成メソッド==//
    // 注文生成時複雑なBusniess　Rogicを凝集
    //　後で生成の変更がある時、このメソッドだけを変更する
    // そして@NoArgsConstructor(access = AccessLevel.PROTECTED)から
    // 直接生成を防いでこれを使う
    // Domain Model Pattern
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { // 複数送信可能
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // 進行状況
        order.setOrderDateTime(LocalDateTime.now());
        return order;
    }

    //==busniess rogic==//

    // 注文キャンセル
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMPLETE) {
            throw new IllegalStateException("配送完了済みの品はキャンセルできません");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancle();
        }
    }

    //==照会 Rogic==//
    // 計算が必要時

    // 全体注文価額
    public int getTotalPrice() {

         int totalPrice = 0;
         for (OrderItem orderItem : orderItems) {
             totalPrice += orderItem.getTotalPrice();
         }
        return totalPrice;
        // 同じコード　return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }
}
