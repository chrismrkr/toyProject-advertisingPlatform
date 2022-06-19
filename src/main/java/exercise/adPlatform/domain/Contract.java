package exercise.adPlatform.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

/*
 * Contract: 계약 데이터를 담는 엔티티
 * Attributes: (id, company, startDate, endDate)
 *
 * == 메소드 ==
 * 1. setCompany: 계약 엔티티 생성 시 업체와 연관관계를 매핑하기 위해 사용
 */
@Entity
@Getter
public class Contract {
    private static AtomicLong key = new AtomicLong(1000000000);

    @Id
    @Column(name="contract_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    // 계약기간: 시작일, 종료일(시작일+365), 기간이 중복되는지 검증필요
    private LocalDate startDate;
    private LocalDate endDate;

    protected Contract() {}
    private Contract(LocalDate startDate) {
        this.id = key.getAndIncrement();
        this.startDate = startDate;
        this.endDate = startDate.plusDays(365);
    }

    public static Contract getInstance(LocalDate startDate) {
        return new Contract(startDate);
    }

    // setCompany: 계약 엔티티 생성 시 업체와 연관관계를 매핑하기 위해 사용
    public void setCompany(Company company) {
        this.company = company;
        company.getContracts().add(this);
    }
}
