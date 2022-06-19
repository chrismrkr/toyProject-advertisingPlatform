package exercise.adPlatform.domain;

import exercise.adPlatform.domain.dto.AccountingDTO;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

/*
 * Accounting: 정산 데이터를 담는 엔티티
 * Attributes: (id, clickDate, companyId, companyName, itemId, itemName, clickCount, totalBidding)
 *             (기본키, 클릭날짜, 업체 Id,      업체명,     상품 Id,  상품명,    클릭횟수,     총과금액   )
 */
@Entity
@Getter
public class Accounting {
    @Id
    @GeneratedValue
    private Long id;

    private Long advertisementId;
    private LocalDate clickDate;

    private Long companyId;
    private String companyName;

    private Long itemId;
    private String itemName;

    private int clickCount;
    private Long totalBidding;

    protected Accounting() {}
    private Accounting(AccountingDTO accountingDTO) {
        this.advertisementId = accountingDTO.getAdvertisementId();
        this.clickDate = accountingDTO.getClickDate();
        this.companyId = accountingDTO.getCompanyId();
        this.companyName = accountingDTO.getCompanyName();
        this.itemId = accountingDTO.getItemId();
        this.itemName = accountingDTO.getItemName();
        this.clickCount = accountingDTO.getClickCount();
        this.totalBidding = accountingDTO.getTotalBidding();
    }

    public static Accounting getInstance(AccountingDTO accountingDTO) {
        return new Accounting(accountingDTO);
    }

}
