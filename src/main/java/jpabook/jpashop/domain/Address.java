package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable // 値タイプを定義する場所に表示 , どこかに組み込まれることができるという関数
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // publicに設定すると、多くの人が呼び出すことができるので、むやみにnewを生成できないように設定
    // @Embeddableはdefault constructorをprotectedで設定するのが少し安全
    // JPA具現libraryがobjectを生成する時Reflection,proxyのような技術を使えるように支援すつべきから
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
