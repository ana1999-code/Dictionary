package com.example.dictionary.application.batch.listener;

import com.example.dictionary.application.batch.data.FailedWordInfo;
import com.example.dictionary.application.batch.data.WordInfo;
import com.example.dictionary.application.util.ValidationErrorMessageUtil;
import com.example.dictionary.domain.entity.Word;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class WordProcessorListener implements ItemProcessListener<WordInfo, Word> {

    private final static Logger LOGGER = LoggerFactory.getLogger(WordProcessorListener.class);

    @Qualifier("successWordValidatingItemWriter")
    private final FlatFileItemWriter<WordInfo> successItemWriter;

    @Qualifier("failedWordValidatingItemWriter")
    private final FlatFileItemWriter<FailedWordInfo> failedItemWriter;

    public WordProcessorListener(FlatFileItemWriter<WordInfo> successItemWriter,
                                 FlatFileItemWriter<FailedWordInfo> failedItemWriter) {
        this.successItemWriter = successItemWriter;
        this.failedItemWriter = failedItemWriter;
    }


    @Override
    public void beforeProcess(WordInfo item) {
        LOGGER.info("WordInfo to be processed {}", item);
    }

    @Override
    public void afterProcess(WordInfo item, Word result) {
        LOGGER.info("Successfully processed word info {}", item);
        try {
            successItemWriter.open(new ExecutionContext());
            successItemWriter.write(Chunk.of(item));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onProcessError(WordInfo item, Exception exception) {
        FailedWordInfo failedWordInfo = new FailedWordInfo();
        failedWordInfo.setName(item.getName());
        failedWordInfo.setCategory(item.getCategory());
        failedWordInfo.setDefinition(item.getDefinition());
        failedWordInfo.setExample(item.getExample());

        if (exception instanceof ValidationException) {
            String errorMessage = ValidationErrorMessageUtil
                    .extractValidationErrorMessage((ValidationException) exception);
            failedWordInfo.setError(errorMessage);
        } else {
            failedWordInfo.setError(exception.getMessage());
        }

        LOGGER.error("Failed word info {}", failedWordInfo);

        try {
            failedItemWriter.open(new ExecutionContext());
            failedItemWriter.write(Chunk.of(failedWordInfo));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @PreDestroy
    public void close() {
        successItemWriter.close();
        failedItemWriter.close();
    }
}
