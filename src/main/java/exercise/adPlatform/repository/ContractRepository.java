package exercise.adPlatform.repository;


import exercise.adPlatform.domain.Contract;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/*
 * 계약 데이터를 관리하는 엔티티
 * == 메소드 ==
 * 1. save: 계약 저장
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class ContractRepository {
    private final EntityManager em;

    // save: 계약 저장
    public void save(Contract contract) {
        em.persist(contract);
    }
}
