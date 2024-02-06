package com.example.dictionary.application.batch;

import com.example.dictionary.application.batch.data.FailedWordInfo;
import com.example.dictionary.application.batch.data.WordInfo;
import com.example.dictionary.application.batch.listener.WordProcessorListener;
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
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.support.AbstractFileItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;

@Configuration
public class BatchImportConfigForWords {

    private static final String[] WORD_INFO_FIELDS = new String[]{
            "name", "category", "definition", "example"
    };

    private static final String[] FAILED_WORD_INFO_FIELDS = new String[]{
            "name", "category", "definition", "example", "error"
    };

    public static final String SUCCESS = "SUCCESS_";

    public static final String FAILURE = "FAILURE_";

    public static final String CSV = "csv";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean("wordInfoItemReader")
    @StepScope
    public ResourceAwareItemReaderItemStream<WordInfo> wordInfoItemReader(
            @Value("#{jobParameters['filePath']}") String path,
            @Value("#{jobParameters['fileType']}") String fileType
    ) {
        return fileType.equalsIgnoreCase(CSV) ?
                getCsvWordInfoItemReader(path) :
                getJsonWordInfoItemReader(path);
    }

    private static FlatFileItemReader<WordInfo> getCsvWordInfoItemReader(String path) {
        return new FlatFileItemReaderBuilder<WordInfo>()
                .name("wordInfoItemReader")
                .resource(new FileSystemResource(path))
                .linesToSkip(1)
                .delimited()
                .names(WORD_INFO_FIELDS)
                .fieldSetMapper(new WordInfoSetMapper())
                .build();
    }

    private static JsonItemReader<WordInfo> getJsonWordInfoItemReader(String path) {
        return new JsonItemReaderBuilder<WordInfo>()
                .name("wordInfoItemReader")
                .resource(new FileSystemResource(path))
                .jsonObjectReader(new JacksonJsonObjectReader<>(WordInfo.class))
                .build();
    }

    @Bean("wordItemWriter")
    public ItemWriter<Word> wordItemWriter() {
        return new JpaItemWriterBuilder<Word>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

    @Bean("successWordValidatingItemWriter")
    @StepScope
    public AbstractFileItemWriter<WordInfo> successWordValidatingItemWriter(
            @Value("#{jobParameters['fileLocation']}") String fileLocation,
            @Value("#{jobParameters['fileName']}") String fileName,
            @Value("#{jobParameters['fileType']}") String fileType
    ) {
        return fileType.equalsIgnoreCase(CSV) ?
                csvWordValidatingItemWriter(fileLocation, fileName, SUCCESS, WORD_INFO_FIELDS) :
                jsonWordValidatingItemWriter(fileLocation, fileName, SUCCESS);
    }

    @Bean("failedWordValidatingItemWriter")
    @StepScope
    public AbstractFileItemWriter<FailedWordInfo> failedWordValidatingItemWriter(
            @Value("#{jobParameters['fileLocation']}") String fileLocation,
            @Value("#{jobParameters['fileName']}") String fileName,
            @Value("#{jobParameters['fileType']}") String fileType
    ) {
        return fileType.equalsIgnoreCase(CSV) ?
                csvWordValidatingItemWriter(fileLocation, fileName, FAILURE, FAILED_WORD_INFO_FIELDS) :
                jsonWordValidatingItemWriter(fileLocation, fileName, FAILURE);
    }

    private <T> FlatFileItemWriter<T> csvWordValidatingItemWriter(
            String fileLocation,
            String fileName,
            String result,
            String[] fields
    ) {
        FlatFileItemWriter<T> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setName(result + "csvWordValidatingItemWriter");

        String path = fileLocation + result + fileName;
        itemWriter.setResource(
                new FileSystemResource(path));

        DelimitedLineAggregator<T> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<T> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(fields);
        lineAggregator.setFieldExtractor(fieldExtractor);
        itemWriter.setLineAggregator(lineAggregator);

        itemWriter.setTransactional(false);
        return itemWriter;
    }

    private <T> JsonFileItemWriter<T> jsonWordValidatingItemWriter(
            String fileLocation,
            String fileName,
            String result
    ) {
        String path = fileLocation + result + fileName;
        return new JsonFileItemWriterBuilder<T>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(new FileSystemResource(path))
                .name(result + "jsonWordValidatingItemWriter")
                .build();
    }

    @Bean
    public ItemProcessor<WordInfo, WordInfo> wordInfoValidatingItemProcessor() {
        return new BeanValidatingItemProcessor<>();
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

    @Bean("importWordsFromFileToDbStep")
    public Step importWordsFromFileToDbStep(
            @Qualifier("wordInfoItemReader") ItemReader<WordInfo> reader,
            @Qualifier("wordItemWriter") ItemWriter<Word> writer,
            WordProcessorListener wordProcessorListener) {
        return new StepBuilder("importWordsFromFileToDbStep", jobRepository)
                .<WordInfo, Word>chunk(100, transactionManager)
                .reader(reader)
                .processor(compositeItemProcessor())
                .listener(wordProcessorListener)
                .faultTolerant()
                .skip(RuntimeException.class)
                .skip(ValidationException.class)
                .skipPolicy((throwable, count) -> true)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet openFilesLocationTasklet(
            @Value("#{jobParameters['filePath']}") String filePath
    ) {
        return (contribution, chunkContext) -> {
            File file = new File(filePath);
            Runtime.getRuntime().exec("explorer.exe " + file.getParent());
            return RepeatStatus.FINISHED;
        };
    }

    @Bean("openFilesLocationStep")
    public Step openFilesLocationStep(
            Tasklet openFilesLocationTasklet
    ) {
        return new StepBuilder("openFilesLocationStep", jobRepository)
                .tasklet(openFilesLocationTasklet, transactionManager)
                .build();
    }

    @Bean("importWordsFromFileToDbJob")
    public Job importWordsFromFileToDbJob(
            @Qualifier("importWordsFromFileToDbStep") Step importStep,
            @Qualifier("openFilesLocationStep") Step openFolderStep) {
        return new JobBuilder("importWordsFromFileToDbJob", jobRepository)
                .start(importStep)
                .next(openFolderStep)
                .build();
    }

    @Bean("openFileLocationJob")
    public Job openFileLocationJob(
            @Qualifier("openFilesLocationStep") Step openFolderStep
    ) {
        return new JobBuilder("openFileLocationJob", jobRepository)
                .start(openFolderStep)
                .build();
    }
}
