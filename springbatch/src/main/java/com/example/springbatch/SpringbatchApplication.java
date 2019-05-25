package com.example.springbatch;


import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class SpringbatchApplication {

    @Autowired
	Job myjob;

    @Autowired
	JobLauncher jobLauncher;






	public static void main(String[] args) {
		SpringApplication.run(SpringbatchApplication.class, args);

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("dest", "user.csv");
		JobParameters jobParameter = jobParametersBuilder.toJobParameters();












	}





}