package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import com.example.springbatch.UserItemProcessor;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    EntityManagerFactory emf;


    public TaskExecutor myTaskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
    @Bean
    public FlatFileItemReader<User> flatFileItemReader(@Value("#{jobParameters[dest]}") String dest){
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        System.out.println(dest);
        reader.setResource(new ClassPathResource(dest));
        reader.setLinesToSkip(1);


        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("userId", "name","marks");

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(tokenizer);
        reader.setLineMapper(lineMapper);
        return  reader;
    }




    @Bean
    public JpaItemWriter<User>  jpaItemWriter(){
        JpaItemWriter<User> writer = new JpaItemWriter();
        writer.setEntityManagerFactory(emf);
        return writer;
    }


    /*@Bean
    public UserItemProcessor userItemProcessor() {
        return new UserItemProcessor();
    }*/
    @Bean
    public UserItemProcessor userProcessor(){
        return new UserItemProcessor();
    }

    @Bean
    public UserMailProcesser userMailProcessor(){
        return new UserMailProcesser();
    }



    @Bean
    @StepScope
    public Step step1() {

        return stepBuilderFactory.get("step1").<User, User>
                chunk(2).
                reader(flatFileItemReader(null)).
                processor(userProcessor()).
                writer(jpaItemWriter()).
                taskExecutor(myTaskExecutor()).
                build();
    }

    @Bean
    public Step step2() throws Exception {

        return stepBuilderFactory.get("step2").<User, User>
                chunk(10).
                reader(userMailSendIndicatorReader()).
                processor(userMailProcessor()).
                writer(jpaItemWriter()).build();
    }




    @Bean
    public ItemReader<User> userMailSendIndicatorReader() throws Exception {
        JpaPagingItemReader<User> reader = new JpaPagingItemReader<User>();
        reader.setEntityManagerFactory(emf);
        reader.setQueryString("SELECT u FROM User u");

        reader.setPageSize(5);
        reader.afterPropertiesSet();

        return reader;

    }



   @Bean
    public JobParameters getJobParameters() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("dest", "user.csv");
        //jobParametersBuilder.addDate("date", <date_from_cmd_line>);
        return jobParametersBuilder.toJobParameters();
    }

    @Bean
    public Job importUserJob()  throws Exception{
        return jobBuilderFactory.get("importUserJob").incrementer(
                new RunIdIncrementer()).flow(step1()).next(step2()).end().build();
    }


}
