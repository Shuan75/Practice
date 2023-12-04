package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repsitory.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService { // ItemServiceはItemRepositoryに委任だけをするclass

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // dirty checkingによって　dataを変更する方法
    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity) { // parameter多い場合Dtoを使うx
        Item item = itemRepository.findOne(id); // // findItemで探して持つのはPersist状態

        // findItem.change(name, price, stockQuantity);
        // 少なくともこのようなメソッドを作って使う方が良い
        // これを逆に探すとどこかで変更するのがわかる
        // **setterは使わないこと**

        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);

        // 値を設定したらspringのTransactionalによってcommitになる
        // JPAはflushする　flush => persist contextの中変更するのを探す、探して変わった値をDBにupdate
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
