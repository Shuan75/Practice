package jpabook.jpashop.repsitory;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // component scanをしながら自動的にspring beanとして登録し、JPA例外をspringの例外に変換、DBに接近するコード
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member); // jpaが貯蔵するロジック
        // Persistence Contextにmember entityを入れる
        // Transactionがcommitになる時点でDBに反映する
        // つまりDBにInsert queryを伝送
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); // memberを探して返還
        // find(Type , Pk)
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
        // createQuery(query , 返還タイプ) memberをlistに作ってくれる
        // SQLはテーブルを対象にqueryをする
        // だが、この文はEntity objectを対象にqueryをする
        // memberのentityObjectのvalueをmにあげてentityMemberを探す
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        // parameterをbindingするが名前を照会
        // parameter binding => queryに作成される特定のプロパティをparameterにmappingします
        // 名前と位置二つの方法があるが、可読性に欠けるためメンテナンスの状況では非効率的から名前がおすすめ
    }

}
