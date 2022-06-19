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
            // 초기 데이터 입력할 수 있음.
        }

    }
}
