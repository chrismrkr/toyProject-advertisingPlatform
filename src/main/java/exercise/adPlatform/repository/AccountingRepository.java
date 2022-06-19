package exercise.adPlatform.repository;

import exercise.adPlatform.domain.Accounting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/*
 * 정산 데이터를 관리하는 Repository
 * == 메소드 ==
 *  1. save: 정산 데이터를 저장하는 메소드
 */
@Repository
@RequiredArgsConstructor
public class AccountingRepository  {
    private final EntityManager em;

    // save: 정산 데이터를 저장하는 메소드
    public void save(Accounting accounting) {
        em.persist(accounting);
    }
}
