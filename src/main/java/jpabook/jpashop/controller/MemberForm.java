package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "会員の名前は必須です。") // jakarta validationを通うじて有効性検査
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
