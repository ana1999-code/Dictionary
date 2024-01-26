package com.example.dictionary.ui.words.operation.remove.comment;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.ui.words.WordView;
import com.example.dictionary.ui.words.operation.remove.RemoveOperationTemplate;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.data.binder.Binder;

public class RemoveCommentOperation extends RemoveOperationTemplate {

    private final CommentDto commentDto;

    private Binder<CommentDto> commentDtoBinder;

    public RemoveCommentOperation(WordFacade wordFacade, String wordName, CommentDto commentDto, WordView wordView) {
        super(wordFacade, wordName, wordView);
        this.commentDto = commentDto;
    }

    @Override
    protected String getDescription() {
        return getTranslation("delete") + " " + getTranslation("word.comment");
    }

    @Override
    protected void createBinder() {
        commentDtoBinder = new Binder<>(CommentDto.class);
    }

    @Override
    protected void bind() {
        commentDtoBinder.setBean(commentDto);
        commentDtoBinder.bind(wordTextFieldForm.getDetail(), "text");
    }

    @Override
    protected H4 getConfirmationMessage() {
        return new H4(getTranslation("word.delete.message", getTranslation("word.comment"), commentDto.getText()));
    }

    @Override
    protected void executeRemoveOperation() {
        wordFacade.removeComment(wordName, commentDto.getId());
    }
}
