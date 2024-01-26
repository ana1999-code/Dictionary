package com.example.dictionary.ui.words.operation.add.comments;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.example.dictionary.ui.words.operation.add.AddOperationTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class AddCommentOperation extends AddOperationTemplate {

    private Binder<CommentDto> commentDtoBean = new Binder<>(CommentDto.class);

    public AddCommentOperation(WordFacade wordFacade, String wordName, WordView wordView) {
        super(wordFacade, wordName, wordView);
    }

    @Override
    protected String getDescription() {
        return getTranslation("add") + " " + getTranslation("word.comment");
    }

    @Override
    protected void createBinderAndSave(TextArea detail) throws ValidationException {
        CommentDto commentDto = new CommentDto(detail.getValue());
        commentDtoBean.bind(detail, "text");
        commentDtoBean.writeBean(commentDto);
        wordFacade.addComment(wordName, commentDto);
    }
}
