package exercise.adPlatform;


import exercise.adPlatform.domain.Company;
import exercise.adPlatform.domain.Item;
import exercise.adPlatform.repository.CompanyRepository;
import exercise.adPlatform.repository.ItemRepository;
import exercise.adPlatform.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.addInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    private static class InitService {
        private final CompanyRepository companyRepository;
        private final CompanyService companyService;

        public void addInit() {
            Company eMart = Company.getInstance("이마트", "0000000000", "01000000000", "ABC");
            Company SSG = Company.getInstance("신세계백화점", "0000000001", "01000000000", "ABC");
            Company samSung = Company.getInstance("삼성전자", "0000000002", "01000000000", "ABC");
            Company nike = Company.getInstance("나이키", "0000000003", "01000000000", "ABC");
            Company starBucks = Company.getInstance("스타벅스", "0000000004", "01000000000", "ABC");

            companyRepository.save(eMart);
            companyRepository.save(SSG);
            companyRepository.save(samSung);
            companyRepository.save(nike);
            companyRepository.save(starBucks);

            companyService.addItem(eMart.getId(), Item.getInstance("제품1", 600, 10)); // 1, 4, 6, 8, 9, 10
            companyService.addItem(SSG.getId(), Item.getInstance("제품2", 2800, 5)); // 2, 5, 7
            companyService.addItem(samSung.getId(), Item.getInstance("제품3", 1200000, 0)); // 3, 14
            companyService.addItem(eMart.getId(), Item.getInstance("제품4", 2600, 6));
            companyService.addItem(SSG.getId(), Item.getInstance("제품5", 1200, 4));
            companyService.addItem(eMart.getId(), Item.getInstance("제품6", 6400, 2));
            companyService.addItem(SSG.getId(), Item.getInstance("제품7", 2800, 1));
            companyService.addItem(eMart.getId(), Item.getInstance("제품8", 2500, 3));
            companyService.addItem(eMart.getId(), Item.getInstance("제품9", 4500, 0));
            companyService.addItem(eMart.getId(), Item.getInstance("제품10", 3000, 1));
            companyService.addItem(nike.getId(), Item.getInstance("제품11", 129000, 4));
            companyService.addItem(starBucks.getId(), Item.getInstance("제품12", 18500, 4));
            companyService.addItem(starBucks.getId(), Item.getInstance("제품13", 23000, 7));
            companyService.addItem(samSung.getId(), Item.getInstance("제품14", 480000, 2));
            companyService.addItem(nike.getId(), Item.getInstance("제품15", 25000, 5));
        }

    }
}
