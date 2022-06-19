package exercise.adPlatform.domain;

import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
/*
 * Company: 업체 데이터를 담는 엔티티
 * Attributes: (id, companyName, serialNumber, telNumber, address, Contracts(N))
 */
@Entity
@Getter
public class Company {
    private static AtomicLong key = new AtomicLong(1000000000);

    @Id
    @Column(name = "company_id")
    private Long id;

    private String companyName;
    private String serialNumber;
    private String telNumber;
    private String address;

    @OneToMany(mappedBy = "company")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "company") // 회사별로 계약기간 중복이 있는지 확인하기
    private List<Contract> contracts = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<Advertisement> advertisements = new ArrayList<>();


    public void updateCompany(Company company) {
        this.serialNumber = company.serialNumber;
        this.telNumber = company.telNumber;
        this.address = company.address;
    }

    protected Company() {}
    private Company(String companyName, String serialNumber, String telNumber, String address) {
        this.id = key.getAndIncrement();
        this.companyName = companyName;
        this.serialNumber = serialNumber;
        this.telNumber = telNumber;
        this.address = address;
    }
    public static Company getInstance(String name, String serialNumber, String telNumber, String address) {
        return new Company(name, serialNumber, telNumber, address);
    }

}
