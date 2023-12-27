package com.example.dictionary.application.batch;

import com.example.dictionary.application.batch.data.WordInfo;
import com.example.dictionary.application.batch.processor.WordInfoToWordProcessor;
import com.example.dictionary.application.batch.processor.WordValidatingItemProcessor;
import com.example.dictionary.application.batch.setmapper.WordInfoSetMapper;
import com.example.dictionary.domain.entity.Word;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
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
    public ItemWriter<Word> wordItemWriter() {
        return new JpaItemWriterBuilder<Word>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

    @Bean
    public ItemProcessor<WordInfo, WordInfo> wordInfoValidatingItemProcessor() {
        BeanValidatingItemProcessor<WordInfo> itemProcessor = new BeanValidatingItemProcessor<>();
        itemProcessor.setFilter(true);
        return itemProcessor;
    }

    @Bean
    public ItemProcessor<WordInfo, Word> wordInfoToWordItemProcessor() {
        return new WordInfoToWordProcessor();
    }

    @Bean
    public ItemProcessor<Word, Word> wordValidatingItemProcessor() {
        return new WordValidatingItemProcessor();
    }

    @Bean
    public ItemProcessor<WordInfo, Word> compositeItemProcessor() {
        return new CompositeItemProcessorBuilder<WordInfo, Word>()
                .delegates(
                        wordInfoValidatingItemProcessor(),
                        wordInfoToWordItemProcessor(),
                        wordValidatingItemProcessor()
                )
                .build();
    }

    @Bean
    public Step importWordsFromCsvToDbStep(ItemReader<WordInfo> reader, ItemWriter<Word> writer) {
        return new StepBuilder("importWordsFromCsvToDbStep", jobRepository)
                .<WordInfo, Word>chunk(1, transactionManager)
                .reader(reader)
                .processor(compositeItemProcessor())
                .faultTolerant()
                .writer(writer)
                .build();
    }

    @Bean
    public Job importWordsFromCsvToDbJob(Step step) {
        return new JobBuilder("importWordsFromCsvToDbJob", jobRepository)
                .start(step)
                .build();
    }
}
