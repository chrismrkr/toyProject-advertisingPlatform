package exercise.adPlatform.api;


import exercise.adPlatform.domain.Advertisement;
import exercise.adPlatform.service.AdvertisementService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.List;


/*
 * AdvertisementApiController: 광고 비즈니스 로직과 관련된 HTTP 요청을 처리해 반환하는 컨트롤러
 *
 * == 메소드 ==
 * 1. startAdvertisement: 업체의 광고 생성을 위한 HTTP POST 요청을 처리함
 * 1.1 getErrorCode: 광고가 생성될 수 없는 경우, 에러코드를 Response에 제공
 *
 * 2. displayTop3Advertisement: 상위 3개의 광고를 제공하는 HTTP GET 요청 처리함
 * 2.1 getAdvertisementDisplay: 광고 전시에 필요한 복합적인 속성을 가져온다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdvertisementApi {
    private final AdvertisementService advertisementService;

    @PostMapping("/advertisement")
    @ApiOperation(value = "광고 입찰", notes = "업체는 자신이 등록한 상품만 광고할 수 있습니다. 최대 입찰가 1,000,000원")
    public Object startAdvertisement(@RequestBody AdvertisementRequest advertisementRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.info("광고 입찰가가 상한(1,000,000원)을 초과함: {}", advertisementRequest.getBiddingPrice());
            return bindingResult.getAllErrors();
        }

        Long companyId = advertisementRequest.getSessionKey();
        Long itemId = advertisementRequest.getItemId();
        try {
            Long advertisementId = advertisementService.makeAdvertisement(companyId, itemId, advertisementRequest.getBiddingPrice());
            return new AdvertisementResponse(advertisementId);
        }
        catch (IllegalStateException e) {
            getErrorCode(e, bindingResult); /* 실패 에러 코드 추가 */
            return bindingResult.getAllErrors();
        }
    }

    @GetMapping("/advertisement")
    @ApiOperation(value = "광고 전시", notes = "입찰가가 높은 상위 3개 전시합니다.(재고가 없는 상품의 광고는 제외)")
    public Object displayTop3Advertisement() {
        return getAdvertisementDisplay(advertisementService.displayTop3Advertisement(3));
    }

    private void getErrorCode(IllegalStateException e, BindingResult bindingResult) {
        /*
         * CXAX: 계약이 존재하지 않는 업체이고, 광고상품은 업체가 등록한 것이 아닙니다.
         * CXAO: 광고상품은 업체가 등록한 것이지만, 계약이 존재하지 않는 업체입니다.
         * COAX: 계약이 존재하는 업체이지만, 광고상품은 업체가 등록한 것이 아닙니다.
         */
        if(e.getMessage().equals("CXAX")) {
            bindingResult.reject("NoContractNoItem", "계약이 존재하지 않는 업체이고, 광고상품은 업체가 등록한 것이 아닙니다.");
        }
        else if(e.getMessage().equals("CXAO")) {
            bindingResult.reject("NoContract", "광고상품은 업체가 등록한 것이지만, 계약이 존재하지 않는 업체입니다.");
        }
        else {
            bindingResult.reject("NoItem", "계약이 존재하는 업체이지만, 광고상품은 업체가 등록한 것이 아닙니다.");
        }
    }

    private List<AdvertisementDisplay> getAdvertisementDisplay(List<Advertisement> advertisementList) {
        List<AdvertisementDisplay> rets = new ArrayList<>();
        for(Advertisement advertisement : advertisementList) {
            rets.add(new AdvertisementDisplay(
                    advertisement.getId(),
                    advertisement.getCompany().getId(),
                    advertisement.getItem().getId(), // N+1 문제 피하기 위해 fetch join됨
                    advertisement.getItem().getItemName(),
                    advertisement.getItem().getPrice(),
                    advertisement.getBiddingPrice()));
        }
        return rets;
    }

    /*
     * 광고 전시 목록을 담은 위한 Http Response 객체
     */
    @Getter
    private static class AdvertisementDisplayResponse {
        private List<AdvertisementDisplay> advertisementDisplays;
        public AdvertisementDisplayResponse(List<AdvertisementDisplay> advertisementDisplays) {
            this.advertisementDisplays = advertisementDisplays;
        }
    }


    /*
     * 광고 전시에 필요한 Attributes을 담은 클래스
     */
    @Getter
    @AllArgsConstructor
    private static class AdvertisementDisplay {
        private Long advertisementId;
        private Long companyId;
        private Long itemId;
        private String itemName;
        private int itemPrice;
        private int biddingPrice;
        protected AdvertisementDisplay() {}
    }


    /*
     * 광고 생성을 위한 HTTP Request 객체
     */
    @Getter
    private static class AdvertisementRequest {
        private Long sessionKey;
        private Long itemId;

        @Max(value = 1000000, message = "최대 광고입찰가는 1,000,000원입니다.")
        private int biddingPrice;
        protected AdvertisementRequest() {}
    }


    /*
     * 광고 생성 결과를 제공하기 위한 HTTP Response 객체
     */
    @Getter
    private static class AdvertisementResponse {
        private Long advertisementId;
        public AdvertisementResponse(Long id) {
            this.advertisementId = id;
        }
    }
}
