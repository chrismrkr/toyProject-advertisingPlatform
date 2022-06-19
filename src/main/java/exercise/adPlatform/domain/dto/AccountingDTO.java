package exercise.adPlatform.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/*
 * Accounting DTO: 정산 데이터를 가져오기 위한 DTO
 */
@Getter
@Setter
@AllArgsConstructor
public class AccountingDTO {
    private LocalDate clickDate;

    private Long advertisementId;

    private Long companyId;
    private String companyName;

    private Long itemId;
    private String itemName;

    private int clickCount;
    private Long totalBidding;
    protected AccountingDTO() {}
}
