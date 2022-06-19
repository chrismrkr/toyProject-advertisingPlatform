package exercise.adPlatform.api;


import exercise.adPlatform.service.AdvertisementBillingService;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/*
 * AdvertisementBillingAPI: CBC 광고 클릭시 발생하는 HTTP를 처리하는 컨트롤러
 *
 * == 메소드 ==
 * 1. clickAdvertisement: 광고 클릭시 전송되는 HTTP를 처리함
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdvertisementBillingApi {
    private final AdvertisementBillingService advertisementBillingService;

    @PostMapping("/advertisementBilling")
    @ApiOperation(value = "광고 과금")
    public Object clickAdvertisement(@RequestBody AdvertisementBillingRequest advertisementBillingRequest) {
        Long advertiseBillId = advertisementBillingService
                .makeAdvertiseBill(advertisementBillingRequest.getId(), advertisementBillingRequest.getClickTime());
        return new AdvertisementBillingResponse(advertiseBillId);
    }

    @Getter
    private static class AdvertisementBillingResponse {
        private Long advertisementBillingId;
        public AdvertisementBillingResponse(Long id) {
            this.advertisementBillingId = id;
        }
    }

    @Getter
    private static class AdvertisementBillingRequest {
        private Long id;
        private LocalDateTime clickTime;
        protected AdvertisementBillingRequest() {}
    }
}
