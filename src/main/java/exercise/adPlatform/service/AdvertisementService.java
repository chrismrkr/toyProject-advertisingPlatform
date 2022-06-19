package exercise.adPlatform.service;


import exercise.adPlatform.domain.Advertisement;
import exercise.adPlatform.domain.Company;
import exercise.adPlatform.domain.Contract;
import exercise.adPlatform.domain.Item;
import exercise.adPlatform.repository.AdvertisementRepository;
import exercise.adPlatform.repository.CompanyRepository;
import exercise.adPlatform.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* AdvertisementService: 광고 엔티티와 관련된 비즈니스 로직을 수행하는 Service
 *
 * == 메소드 ==
 * 1. makeAdvertisement: 업체가 새로운 상품 광고를 등록
 * 1.1 checkAdvertisementCondition: 업체가 광고하는 상품을 등록했는지 검증
 *
 * 2. displayTop3Advertisement: 전체 광고 중 입찰가격이 높은 순서로 3개 조회
 * 2.1 sortAdvertisement: 전체 광고를 입찰가격으로 내림차순 정렬
 *
 * 3. findById: id를 통해 광고 검색
 *
 * 4. findAll: 전체 광고 검색
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final ItemRepository itemRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public Long makeAdvertisement(Long companyId, Long itemId, int biddingPrice) {
        Item item = itemRepository.findById(itemId);
        Company company = companyRepository.findById(companyId);

        // 검증 로직: 회사는 계약된 상태 && 아이템은 회사가 등록한 것이어야 함
        checkAdvertisementCondition(company, item);

        Advertisement advertisement = Advertisement.getInstance(biddingPrice);
        advertisement.setCompanyAndItem(company, item);
        advertisementRepository.save(advertisement);

        return advertisement.getId();
    }

    public List<Advertisement> displayTop3Advertisement(int displayNumber) {
        List<Advertisement> displayAds = advertisementRepository.findDisplayAdsWithFetchJoin(displayNumber);
        for (Advertisement advertisement : displayAds) {
            log.info("{}, {}, {}", advertisement.getId(), advertisement.getBiddingPrice(), advertisement.getItem().getStock());
        }

        return displayAds;
    }

    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }

    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id);
    }

    private void checkAdvertisementCondition(Company company, Item item) {
        List<Contract> contracts = company.getContracts();

        Long itemCompanyId = item.getCompany().getId();
        Long companyId = company.getId();

        log.info("{}, {}, {}", contracts.size(), itemCompanyId, companyId);

        if((contracts.isEmpty()) && (!itemCompanyId.equals(companyId))) {
            log.info("계약이 존재하지 않는 업체이고, 광고상품은 업체가 등록한 것이 아닙니다.");
            throw new IllegalStateException("CXAX");
        }
        else if((!contracts.isEmpty()) && (!itemCompanyId.equals(companyId))) {
            log.info("계약이 존재하는 업체이지만, 광고상품은 업체가 등록한 것이 아닙니다.");
            throw new IllegalStateException("COAX");
        }
        else if((contracts.isEmpty()) && (!itemCompanyId.equals(companyId))) {
            log.info("광고상품은 업체가 등록한 것이지만, 계약이 존재하지 않는 업체입니다.");
            throw new IllegalStateException("CXAO");
        }
        else {
            log.info("등록할 수 있는 광고입니다.");
        }
    }
}
