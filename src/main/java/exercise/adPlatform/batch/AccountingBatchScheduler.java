package exercise.adPlatform.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

/*
 * 전날 발생한 광고 과금을 정산하기 위한 스케줄러: 매일 0시에 발생
 */
@Component
@RequiredArgsConstructor
public class AccountingBatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job accountingJob;

   // @Scheduled(fixedDelay = 500)
    @Scheduled(cron = "0 0 0 * * *") // 매일 0시에 전날 클릭된 광고 정산
    public void runAccountingSchedule() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requested Time", new JobParameter(System.currentTimeMillis()))
        );
        jobLauncher.run(accountingJob, jobParameters);
    }
}
