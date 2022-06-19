package exercise.adPlatform.repository;


import exercise.adPlatform.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/*
 * 상품 데이터를 관리하는 Repository
 * == 메소드 ==
 * 1. save: 상품 저장
 * 2. findItemList(companyName): 특정 업체(companyName)가 등록한 상품 조회
 * 3. findById: 상품 id를 통해 조회
 * 4. findAll : 상품 전체 조회
 */
@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    // save: 상품 저장
    public void save(Item item) {
        em.persist(item);
    }

    // findItemList(companyName): 특정 업체(companyName)가 등록한 상품 조회
    public List<Item> findItemList(String companyName) {
        return em.createQuery("select i from Item i where i.companyName = :companyName", Item.class)
                .setParameter("companyName", companyName)
                .getResultList();
    }

    public void delete(Long id) {
        Item item = em.find(Item.class, id);
        em.remove(item);
    }
    // findById: 상품 id를 통해 조회
    public Item findById(Long id) {
        return em.find(Item.class, id);
    }

    // findAll : 상품 전체 조회
    public List<Item> findAll() {
        return em.createQuery("select i from item i")
                .getResultList();
    }
}
