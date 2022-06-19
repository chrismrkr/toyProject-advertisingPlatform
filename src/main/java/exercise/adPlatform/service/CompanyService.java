package exercise.adPlatform.service;


import exercise.adPlatform.domain.Company;
import exercise.adPlatform.domain.Contract;
import exercise.adPlatform.domain.Item;
import exercise.adPlatform.repository.CompanyRepository;
import exercise.adPlatform.repository.ContractRepository;
import exercise.adPlatform.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/*
 * CompanyService: Company 엔티티와 관련된 비즈니스 로직을 수행하는 Service
 *
 * == 메소드 ==
 * 1. join: 새로운 업체 생성
 * 1.1 existingCompany: 새로운 업체 생성시 상품 리스트에 존재하는 업체인지 검증
 *
 * 2. update: 기존 업체 수정
 * 3. findCompany: id를 통해 업체를 검색
 * 4. findCompanies: 업체 전체 검색
 *
 * 5. makeContract: 업체가 새로운 계약 엔티티를 생성
 * 5-1. checkContractPeriod: 새로운 계약이 기존의 계약기간과 겹치는지 검증
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {
    private final ItemRepository itemRepository;
    private final CompanyRepository companyRepository;
    private final ContractRepository contractRepository;


    @Transactional // join: 업체 생성
    public Long join(Company company) {
        companyRepository.save(company);
        return company.getId();
    }

    @Transactional // update: 업체 수정
    public void update(Long id, Company newCompany) { // 업체 수정(업체명 제외)
        Company company = companyRepository.findById(id);
        company.updateCompany(newCompany);
    }

    @Transactional // delete: 업체 삭제
    public void delete(Long id) {
        companyRepository.delete(id);
    }

    @Transactional // 상품 추가
    public Long addItem(Long companyId, Item item) {
        Company company = companyRepository.findById(companyId);
        item.setCompany(company);
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional // 상품 수정
    public void updateItem(Long companyId, Long itemId, Item newItem) {
        Company company = companyRepository.findById(companyId);
        Item item = itemRepository.findById(itemId);
        checkItemBelongCompany(company, item);
        item.update(newItem);
    }

    @Transactional // 상품 삭제
    public void deleteItem(Long companyId, Long itemId) {
        Company company = companyRepository.findById(companyId);
        Item item = itemRepository.findById(itemId);
        checkItemBelongCompany(company, item);
        itemRepository.delete(itemId);
    }

    // 업체가 등록한 모든 상품 검색
    public List<Item> findAllItem(Long companyId) {
        Company company = companyRepository.findById(companyId);
        return company.getItems();
    }

    // findCompanies: 업체 전체 검색
    public List<Company> findCompanies() {
        return companyRepository.findAll();
    }

    // findCompany: id를 통해 업체를 검색
    public Company findCompany(Long companyId) {
        return companyRepository.findById(companyId);
    }

    @Transactional // makeContract: 업체가 새로운 계약 엔티티를 생성
    public Long makeContract(Long companyId, LocalDate date) {
        Company company = companyRepository.findById(companyId);
        Contract contract = Contract.getInstance(date);
        if(checkContractPeriod(company, contract)) { // 계약기간 중복 확인
            contract.setCompany(company);
            contractRepository.save(contract);
            return contract.getId();
        }

        log.info("계약기간 중복 발생");
        throw new IllegalStateException("계약기간이 중복됩니다.");
    }

    // checkContractPeriod: 새로운 계약이 기존의 계약기간과 겹치는지 검증
    private boolean checkContractPeriod(Company company, Contract newContract) {
        List<Contract> contracts = company.getContracts();
        for(Contract contract : contracts) { // 겹치는 구간을 확인하는 로직
            LocalDate currentStartDate = contract.getStartDate();
            LocalDate currentEndDate = contract.getEndDate();
            /* 날짜가 겹치지 않는 경우
                1. currentStartDate > newContract.endDate
                2. currentEndDate < newContract.startDate
            */
            if(!((newContract.getEndDate().isBefore(currentStartDate))
                    || (newContract.getStartDate().isAfter(currentEndDate)))) { // 날짜가 하루라도 겹치는 경우
               return false;
            }
        }
        return true;
    }

    private void checkItemBelongCompany(Company company, Item targetItem) {
        List<Item> items = company.getItems();
        for (Item item : items) {
            if(item.getId().equals(targetItem.getId())) {
                return;
            }
        }
        throw new IllegalStateException("업체가 등록한 상품이 아닙니다.");
    }

}
