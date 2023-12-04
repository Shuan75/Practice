package jpabook.jpashop.repsitory;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName; // 会員名前　parameter条件があると検索
    private OrderStatus orderStatus; //注文の状態[ORDER,CANCEL]
}
