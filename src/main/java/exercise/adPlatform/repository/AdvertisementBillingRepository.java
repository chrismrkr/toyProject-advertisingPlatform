package exercise.adPlatform.repository;


import exercise.adPlatform.domain.AdvertisementBilling;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/*
 * CBC 광고 과금 데이터를 저장하는 Repository
 * == 메소드 ==
 * 1. save: 과금 엔티티를 저장
 * 2. findById: 과금 엔티티의 Id(pk)를 통해 검색
 * 3. findAll: 저장된 과금 엔티티를 모두 조회
 */
@Repository
@RequiredArgsConstructor
public class AdvertisementBillingRepository {
    private final EntityManager em;

    // save: 과금 엔티티를 저장
    public void save(AdvertisementBilling advertisementBilling) {
        em.persist(advertisementBilling);
    }

    // findById: 과금 엔티티의 Id(pk)를 통해 검색
    public AdvertisementBilling findById(Long id) {
        return em.find(AdvertisementBilling.class, id);
    }

    // findAll: 저장된 과금 엔티티를 모두 조회
    public List<AdvertisementBilling> findAll() {
        return em.createQuery("select b from advertisement_billing b", AdvertisementBilling.class)
                .getResultList();
    }
}
