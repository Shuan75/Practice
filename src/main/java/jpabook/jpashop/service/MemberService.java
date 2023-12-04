package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repsitory.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // spring beanに登録
@Transactional(readOnly = true)
// jpaのデータ変更やロジックはなるべくTransactionalの中で実行すべき, public メソッドたちは基本的にtransactionに入られる
@RequiredArgsConstructor // finalがつけてるfieldだけconstructor生成
public class MemberService {

    private final MemberRepository memberRepository; // 変更することがないからfinal、そしてcompile時点でcheckができる

    // 会員記入
    @Transactional // readOnly= trueをするとdata変更が不可能
    public Long join(Member member) {
        validateDuplicateMember(member); // 重複会員検証ロジック
        memberRepository.save(member);
        return member.getId();
    }


    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        // 同じ値があるか探した後　返還値をfindMembersに入れる
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("既に存在する会員です。");
        }
    }
    //　実務ではmulti threadや色々の状況に備えてDBからMemberのnameをunique条件を追加

    //　会員全体照会
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 会員一件照会
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
