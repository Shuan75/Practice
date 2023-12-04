package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repsitory.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        // 注文画面には注文する顧客情報と商品情報が必要ですので、model objectに入れてViewに渡す
        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        // @RequestParam => form submit方式を使うとselect name=""のvalueを受ける、そしてBinding
        // 識別子(id)だけを渡して核心Rogicはserviceで作る理由は、persistcontextが存在する状態で照会できます。
        // つまり、注文しながら個数や価格が変更された場合、dirty checkingをしても自然に適用されるが、
        // persist状態が終わるとJPAと関係がないため修正が難しくなる

        // 一つの商品だけを注文する
        // ここを直すと色々の商品を纏めて注文でける
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        // 商品Listを検索するための検索条件が orderSearchに入れて受ける
        // 単純な照会する機能であれば、contollerからrepositoryを呼び出しても良い (architectureによって違う)
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancleOrder(orderId);
        return "redirect:/orders";
    }
}
