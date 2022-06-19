package exercise.adPlatform.service;


import exercise.adPlatform.domain.Advertisement;
import exercise.adPlatform.domain.AdvertisementBilling;
import exercise.adPlatform.repository.AdvertisementBillingRepository;
import exercise.adPlatform.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/*
 * AdvertisementBillingService: CBC 광고 클릭시 발생하는 비즈니스 로직을 담당하는 Service
 *
 * == 메소드 ==
 * 1. makeAdvertiseBill: 클릭된 광고에 대한 광고 과금 엔티티를 생성함
 */
@Service
@RequiredArgsConstructor
public class AdvertisementBillingService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementBillingRepository advertisementBillingRepository;

    @Transactional
    public Long makeAdvertiseBill(Long advertisementId, LocalDateTime clickTime) {
        Advertisement advertisement = advertisementRepository.findById(advertisementId);
        AdvertisementBilling advertisementBilling = AdvertisementBilling.getInstance(clickTime);
        advertisementBilling.setAdvertisement(advertisement);

        advertisementBillingRepository.save(advertisementBilling);
        return advertisementBilling.getId();
    }

    public List<AdvertisementBilling> findAll() {
        return advertisementBillingRepository.findAll();
    }
}
