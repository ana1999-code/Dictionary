package com.example.dictionary.application.batch;

import com.example.dictionary.application.batch.data.WordInfo;
import com.example.dictionary.application.batch.setmapper.WordInfoSetMapper;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchImportConfigForWords {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private static final String[] fieldsName = new String[]{
            "name", "category", "definition", "example"
    };


    @Bean
    @StepScope
    public FlatFileItemReader<WordInfo> wordInfoItemReader(@Value("#{jobParameters['filePath']}") String path) {
        return new FlatFileItemReaderBuilder<WordInfo>()
                .name("wordInfoItemReader")
                .resource(new FileSystemResource(path))
                .linesToSkip(1)
                .delimited()
                .names(fieldsName)
                .fieldSetMapper(new WordInfoSetMapper())
                .build();
    }

    @Bean
    public ItemWriter<WordInfo> infoItemWriter() {
        FlatFileItemWriter<WordInfo> itemWriter = new FlatFileItemWriter<>();

        itemWriter.setResource(new FileSystemResource("src/main/resources/shipped_orders_output.csv"));

        DelimitedLineAggregator<WordInfo> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<WordInfo> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(fieldsName);
        lineAggregator.setFieldExtractor(fieldExtractor);

        itemWriter.setLineAggregator(lineAggregator);
        return itemWriter;

    }

    @Bean
    public Step step() {
        return new StepBuilder("step", jobRepository)
                .<WordInfo, WordInfo>chunk(10, transactionManager)
                .reader(wordInfoItemReader(""))
                .writer(infoItemWriter())
                .build();
    }

    @Bean
    public Job job(){
        return new JobBuilder("job", jobRepository)
                .start(step())
                .build();
    }
}
