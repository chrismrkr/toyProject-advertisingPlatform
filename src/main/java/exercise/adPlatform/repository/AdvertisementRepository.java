package exercise.adPlatform.repository;


import exercise.adPlatform.domain.Advertisement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/*
 * 광고 데이터를 관리하는 Repository
 * == 메소드 ==
 * 1. save: 광고 데이터를 저장
 * 2. findById: 광고 엔티티의 Id(Pk)를 통하여 검색
 * 3. findAll: 광고 엔티티 전체 조회
 * 4. findDisplayAdsWithFetchJoin: 광고와 (상품, 업체)를 fetch join하여 전체 조회
 */
@Repository
@RequiredArgsConstructor
public class AdvertisementRepository {
    private final EntityManager em;

    // save: 광고 데이터를 저장
    public void save(Advertisement advertisement) {
        em.persist(advertisement);
    }

    // findById: 광고 엔티티의 Id(Pk)를 통하여 검색
    public Advertisement findById(Long id) {
        return em.find(Advertisement.class, id);
    }

    // findAll: 광고 엔티티 전체 조회
    public List<Advertisement> findAll() {
        return em.createQuery("select a from Advertisement a", Advertisement.class)
                .getResultList();
    }

    // findAllWithFetchJoin: 광고와 (상품, 업체)를 fetch join하여 전체 조회
    public List<Advertisement> findDisplayAdsWithFetchJoin(int size) {
        String sql = "select a" +
                " from Advertisement a " +
                " join fetch a.company c" +
                " join fetch a.item i" +
                " where i.stock != 0" +
                " order by a.biddingPrice DESC";
        return em.createQuery(sql, Advertisement.class)
                .setFirstResult(0)
                .setMaxResults(size)
                .getResultList();
    }
}
