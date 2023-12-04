package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // packageと中にあることを全部component scanをしながらspring beanで自動登録
public class JpashopApplication {

	public static void main(String[] args) {


		SpringApplication.run(JpashopApplication.class, args);
	}

}
