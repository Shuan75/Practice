package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repsitory.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // springとIntegrationをする
@SpringBootTest
@Transactional // rollback可能 testだからDBに転送が不可能すべき
public class MemberServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    @Autowired EntityManager em;
    @Test
    // @Rollback(value = false) 確認がしたい時使う
    public void 会員登録() throws Exception {
        // given
        Member member = new Member();
        member.setName("Kim");

        // when
        Long saveId = memberService.join(member);

        // then
        em.flush();// persist contextにある内容うをDBに反映
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class) // try cacth文のcatch設定
    public void 重複_会員_例外() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("Kim");

        Member member2 = new Member();
        member2.setName("Kim");

        // when
        memberService.join(member1);
        memberService.join(member2); // exception発生すべき

        // then
        fail("Exceptionが発生すべき");
    }

}