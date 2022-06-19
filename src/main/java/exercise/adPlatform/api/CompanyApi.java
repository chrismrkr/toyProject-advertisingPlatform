package exercise.adPlatform.api;


import exercise.adPlatform.domain.Company;
import exercise.adPlatform.domain.Item;
import exercise.adPlatform.service.CompanyService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/*
 * CompanyAPI: 업체 관련 비즈니스 로직 HTTP 요청을 처리하는 컨트롤러
 *
 * == 메소드 ==
 * 1. registerCompany: 업체 등록 HTTP POST 처리. 상품 테이블에 존재하는 업체만 등록 가능하다.
 * 2. registerContract: 계약 등록 HTTP POST 처리. 기존 계약기간과 중복되어서는 안된다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CompanyApi {
    private final CompanyService companyService;

    @PostMapping("/company") // 업체 등록
    @ApiOperation(value = "업체 등록", notes = "사업자번호와 전화번호가 숫자로 10자리일 때만 등록 가능합니다.")
    public Object registerCompany(@Valid @RequestBody CompanyRequest companyRequest, BindingResult bindingResult) {
        if(notNumber(companyRequest.getSerialNumber()) || notNumber(companyRequest.getTelNumber())) {
            /* 사업자번호와 휴대전화번호가 숫자인지 검증 */
            bindingResult.reject("notNumber", new Object[]{companyRequest.getSerialNumber(), companyRequest.getTelNumber()},
                                "Both Serial Number and Tel Number must be numeral.");
        }

        if(bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        }

        Long join = companyService.join(Company.getInstance(
                companyRequest.getCompanyName(),
                companyRequest.getSerialNumber(),
                companyRequest.getTelNumber(),
                companyRequest.getAddress())
        );
        return new CompanyResponse(join);
    }

    @PostMapping("/item")
    @ApiOperation(value = "상품 등록", notes = "등록된 업체가 새로운 상품을 등록합니다.")
    public Object registerItem(@Valid @RequestBody ItemRequest itemRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        }

        Item newItem = Item.getInstance(itemRequest.getItemName(), itemRequest.getPrice(), itemRequest.getStock());
        Long newItemId = companyService.addItem(itemRequest.getCompanyId(), newItem);
        return new ItemResponse(newItemId);
    }

    @GetMapping("/items/{id}")
    @ApiOperation(value = "상품 조회", notes = "업체가 등록한 모든 상품을 조회합니다.")
    public Object getAllItems(@PathVariable("id") Long companyId) {
        List<Item> allItem = companyService.findAllItem(companyId);

        List<ItemResponseDto> itemResponseDtos = allItem.stream()
                .map(i -> new ItemResponseDto(i))
                .collect(Collectors.toList());
        return itemResponseDtos;
    }

    @PostMapping("/contract")
    @ApiOperation(value = "계약 등록", notes = "등록이 완료된 업체가 기존 계약기간과 중복되지 않도록 새로운 계약(시작일부터 +365일)을 생성할 수 있습니다. ")
    public Object registerContract(@RequestBody ContractRequest contractRequest, BindingResult bindingResult) {
        Long companyId = contractRequest.getSessionKey();

        try {
            Long newContractId = companyService.makeContract(companyId, contractRequest.getStartDate());
            return new ContractResponse(newContractId);
        }
        catch(IllegalStateException e) {
            bindingResult.reject("duplicatedPeriod", new Object[]{contractRequest.getStartDate(),
                    contractRequest.getStartDate().plusDays(365)}, "계약기간이 기존과 중복됩니다.");
            return bindingResult.getAllErrors();
        }
    }

    private boolean notNumber(String number) {
        for (int i = 0; i < number.length(); i++) {
            if ('0' <= number.charAt(i) && number.charAt(i) <= '9') continue;
            else return true;
        }
        return false;
    }

    @Getter
    private static class ItemResponseDto {
        private Long itemId;
        private String itemName;
        private int price;
        private int stock;
        public ItemResponseDto(Item item) {
            this.itemId = item.getId();
            this.itemName = item.getItemName();
            this.price = item.getPrice();
            this.stock = item.getStock();
        }
    }

    @Getter
    private static class ItemRequest {
        @NotNull
        private Long companyId;
        @NotBlank
        private String itemName;
        @NotNull
        private int price;
        @NotNull
        private int stock;
    }

    @Getter
    @AllArgsConstructor
    private static class ItemResponse {
        private Long itemId;
    }
    @Getter
    private static class ContractRequest {
        private Long sessionKey;
        private LocalDate startDate;
        protected ContractRequest() {}
    }

    @Getter
    private static class ContractResponse {
        private Long contractId;
        public ContractResponse(Long id) {
            this.contractId = id;
        }
    }

    @Getter
    private static class CompanyRequest {
        @NotBlank
        private String companyName;
        @Size(min=10, max=10, message = "시리얼 넘버는 10자리만 가능합니다")
        private String serialNumber;
        @NotBlank
        private String telNumber;
        @NotBlank
        private String address;
        protected CompanyRequest() {}
    }

    @Getter
    private static class CompanyResponse {
        private Long companyId;
        public CompanyResponse(Long id) {
            this.companyId = id;
        }
    }
}
