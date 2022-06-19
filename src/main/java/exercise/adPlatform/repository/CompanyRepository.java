package exercise.adPlatform.repository;

import exercise.adPlatform.domain.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/*
 * 업체 데이터를 관리하는 Repository
 * == 메소드 ==
 * 1. save: 업체 등록
 * 2. findById: 업체 id(pk)를 통해 검색
 * 3. findAll: 업체 전체 조회
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CompanyRepository {
    private final EntityManager em;

    // save: 업체 등록
    public void save(Company company) {
        em.persist(company);
    }

    // findById: 업체 id(pk)를 통해 검색
    public Company findById(Long id) {
        return em.find(Company.class, id);
    }


    public void delete(Long id) {
        Company company = em.find(Company.class, id);
        em.remove(company);
    }

    // findAll: 업체 전체 조회
    public List<Company> findAll() {
        return em.createQuery("select c from Company c", Company.class)
                .getResultList();
    }
}
