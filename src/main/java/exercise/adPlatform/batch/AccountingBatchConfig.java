package exercise.adPlatform.batch;


import exercise.adPlatform.domain.Accounting;
import exercise.adPlatform.domain.dto.AccountingDTO;
import exercise.adPlatform.repository.AccountingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * AccountingBatchConfig: 하나의 Step으로 이루어진 Job을 실행함
 *
 * Step
 * 1. accountingReader(Read): 쿼리를 통해 결과를 DTO로 가져옴
 * 2. accountingProcessor(Process): DTO를 이용해 실제 Accounting 엔티티 반환
 * 3. accountingWriter(Write): 반환된 Accounting 엔티티를 DB에 저장
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class AccountingBatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AccountingRepository accountingRepository;
    private final DataSource dataSource;

    @Bean
    @Qualifier("accountingJob")
    public Job accountingJob(Step accountingCreateStep) {
        log.info("## accounting Job Start");
        return jobBuilderFactory.get("accountingJob")
                .incrementer(new RunIdIncrementer())
                .start(accountingCreateStep)
                .build();
    }

    @JobScope
    @Bean
    public Step accountingCreateStep(ItemReader accountingReader, ItemProcessor accountingProcessor, ItemWriter accountingWriter) {
        log.info("## accounting Step Start");
        return stepBuilderFactory.get("accountingCreateStep")
                .<AccountingDTO, Accounting>chunk(10)
                .reader(accountingReader)
                .processor(accountingProcessor)
                .writer(accountingWriter)
                .build();
    }

    @StepScope
    @Bean
    public JdbcPagingItemReader<AccountingDTO> accountingReader() throws Exception {
        log.info("## Accounting Query Requested ##");
        return new JdbcPagingItemReaderBuilder<AccountingDTO>()
                .pageSize(10)
                .fetchSize(10)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(AccountingDTO.class))
                .queryProvider(createQueryProvider())
                .name("accountingReader")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<AccountingDTO, Accounting> accountingProcessor() {
        log.info("accountingDTO Binding Start");
        return new ItemProcessor<AccountingDTO, Accounting>() {
            @Override
            public Accounting process(AccountingDTO accountingDTO) throws Exception {
                log.info("clickDate: {}, CompanyId: {}, CompanyName: {}, itemId: {}, itemName: {}, clickCount: {}, totalBidding: {}", accountingDTO.getClickDate(),
                accountingDTO.getCompanyId(),
                accountingDTO.getCompanyName(),
                accountingDTO.getItemId(),
                accountingDTO.getItemName(),
                accountingDTO.getClickCount(),
                accountingDTO.getTotalBidding());

                return Accounting.getInstance(accountingDTO);
            }
        };
    }

    @StepScope
    @Bean
    public ItemWriter<Accounting> accountingWriter() {
        return new ItemWriter<Accounting>() {
            @Override
            public void write(List<? extends Accounting> accountings) throws Exception {
                for (Accounting accounting : accountings) {
                    log.info("write Accounting: {}", accounting);
                    accountingRepository.save(accounting);
                }
                log.info("## Accounting Terminate");
            }
        };
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);

        queryProvider.setSelectClause(selectClause);
        queryProvider.setFromClause(fromClause);
        queryProvider.setWhereClause(whereClause);
        queryProvider.setGroupClause(groupbyClause);

        Map<String, Order> sortKey = new HashMap<>(1);
        sortKey.put("advertisement_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKey);

        return queryProvider.getObject();
    }

    private final static String selectClause = "SELECT SYSDATE()-1 as click_date, " +
            "c.company_id as company_id, " +
            "c.company_name as company_name, " +
            "ab.advertisement_id as advertisement_id, " +
            "i.item_id as item_id, " +
            "i.item_name as item_name, " +
            "count(*) as click_count, " +
            "sum(ab.bidding_price) as total_bidding";
    private final static String fromClause = "FROM advertisement_billing ab, advertisement a, company c, item i";
    private final static String whereClause =
            "WHERE SYSDATE()-1 <= ab.click_time and ab.click_time < SYSDATE() " +
                    "and ab.advertisement_id = a.advertisement_id " +
                    "and a.company_id = c.company_id " +
                    "and a.item_id = i.item_id";
    private final static String groupbyClause = "GROUP BY ab.advertisement_id";
}
