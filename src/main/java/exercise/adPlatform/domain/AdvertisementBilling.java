package exercise.adPlatform.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/*
 * AdvertisementBilling: 광고 클릭 데이터를 담는 엔티티
 * Attributes: (id, advertisement, clickTime, biddingPrice)
 *           : (기본키,  광고,        클릭시간,    입찰가격    )
 *
 * == 메소드 ==
 * 1. setAdvertisement: 광고 클릭과 광고의 연관관계를 매핑하고, 입찰가격을 광고 클릭 엔티티에 설정함
 */
@Entity
@Getter
public class AdvertisementBilling {
    private static AtomicLong key = new AtomicLong(1000000000);

    @Id
    @Column(name = "advertisement_billing_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

    private LocalDateTime clickTime;
    private int biddingPrice;

    protected AdvertisementBilling() {}
    private AdvertisementBilling(LocalDateTime clickTime) {
        this.id = key.getAndIncrement();
        this.clickTime = clickTime;
    }
    public static AdvertisementBilling getInstance(LocalDateTime clickTime) {
        AdvertisementBilling advertisementBilling = new AdvertisementBilling(clickTime);
        return advertisementBilling;
    }

    // 광고 클릭과 광고의 연관관계를 매핑하고, 입찰가격을 광고 클릭 엔티티에 설정함
    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
        advertisement.getAdvertisementBilling().add(this);
        this.biddingPrice = advertisement.getBiddingPrice();
    }

}
