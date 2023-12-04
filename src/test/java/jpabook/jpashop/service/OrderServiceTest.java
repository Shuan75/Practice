package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repsitory.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    // TestはMethodを単位Testするのが良い
    // JPAと関わって同作するのを見るため通合Test作成

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 商品注文() throws Exception {
        // given
        Member member = createMember();

        Book book = createBook("JPA",10000, 10);

        int orderCount = 2;
        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        // Assertはimport staticをしたから省略可能
        assertEquals("商品注文時状態はORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("注文した商品種類数が正確すべき", 1, getOrder.getOrderItems().size());
        assertEquals("注文価額は価額＊数量", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("注文数量だけの在庫が減るべき", 8, book.getStockQuantity());
    }



    @Test(expected = NotEnoughStockException.class)
    public void 商品注文_在庫数量超過() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("JPA",10000,10);

        int orderCount = 11;

        // when
        orderService.order(member.getId(), item.getId(), orderCount);

        // then
        fail("在庫数量Exceptionが発生すべき");

    }

    @Test
    public void 注文キャンセル() throws Exception {
        // given
        Member member = createMember();
        Book item = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancleOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("注文キャンセル時状態はCANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("注文キャンセル商品はその分在庫が増加すべき", 10, item.getStockQuantity());
    }


    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("会員");
        member.setAddress(new Address("ソウル","1路", "123-123"));
        em.persist(member);
        return member;
    }
}