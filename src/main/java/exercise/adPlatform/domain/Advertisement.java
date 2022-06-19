package exercise.adPlatform.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/*
 * Advertisement: 광고 데이터를 담는 엔티티
 * Attributes: (id, company, item, advertisementBilling(N), biddingPrice)
 *             (기본키, 업체, 상품,   광고를 클릭한 데이터,          과금액    )
 *
 * == 메소드 ==
 * 1.   setCompanyAndItem: 광고 엔티티 생성 시,
 *      company(N->1)와 item(N->1)도 양방향 매핑을 위해 사용됨
 */
@Entity
@Getter
public class Advertisement { //
    private static AtomicLong key = new AtomicLong(1000000000);

    @Id
    @Column(name = "advertisement_id") // 10자리, 숫자여부 추가 검증 필요
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int biddingPrice;

    @OneToMany(mappedBy = "advertisement")
    private List<AdvertisementBilling> advertisementBilling = new ArrayList<>();


    protected Advertisement() {}
    private Advertisement(int biddingPrice) {
        this.id = key.getAndIncrement();
        this.biddingPrice = biddingPrice;
    }
    public static Advertisement getInstance(int biddingPrice) {
        return new Advertisement(biddingPrice);
    }

    // setCompanyAndItem: 양방향 매핑에 사용
    public void setCompanyAndItem(Company company, Item item) {
        this.company = company;
        this.item = item;
        company.getAdvertisements().add(this);
        item.getAdvertisements().add(this);
    }
}

