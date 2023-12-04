package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repsitory.ItemRepository;
import jpabook.jpashop.repsitory.MemberRepository;
import jpabook.jpashop.repsitory.OrderRepository;
import jpabook.jpashop.repsitory.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //注文
    @Transactional // data変更だからreadonly = false
    public Long order(Long memberId, Long itemId, int count) {

        //entity照会
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //配送情報生成
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.REDAY);

        //注文商品生成
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // OrderItem orderItem1 = new OrderItem();
        // orderItem1.setCount();
        // このような方法はメンテナンス的に分散するから大変だからprotectedで防ぐ

        //注文生成
        Order order = Order.createOrder(member, delivery, orderItem);


        //注文貯蔵
        orderRepository.save(order);
        // cascadeをしたからorderItems,deliveryが自動にpersist
        // orderItemとdeliveryが他で使うところがあるとcascade禁止
        return order.getId();

    }

    //キャンセル
    @Transactional
    public void cancleOrder(Long orderId) {

        //注文entity照会
        Order order = orderRepository.findOne(orderId);
        //キャンセル
        order.cancel();
        //mybatis,jdbc template, sql queryなどを作成してすると update queryを開発者がrepositoryに伝送が必要
        //JPAを使うと変更したdataをdirty checkする
    }


    //検索
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
