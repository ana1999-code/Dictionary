package com.example.dictionary.ui.profile;

import com.example.dictionary.application.dto.UserDto;
import com.example.dictionary.application.facade.UserFacade;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.StreamResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.example.dictionary.ui.util.UiUtils.showNotification;
import static com.example.dictionary.ui.util.UiUtils.showSuccess;

public class ProfileFormBinder {

    private BeanValidationBinder<UserDto> binder;

    private final ProfileForm profileForm;

    private UserDto userProfile;

    private HorizontalLayout saveAndEditButtons;

    private VerticalLayout userCredentialsLayout;

    private Button edit;

    private final UserFacade userFacade;

    public ProfileFormBinder(ProfileForm profileForm, UserFacade userFacade) {
        this.profileForm = profileForm;
        this.userFacade = userFacade;
    }

    public void addBindingAndValidation() {
        binder = new BeanValidationBinder<>(UserDto.class);
        binder.bindInstanceFields(profileForm);

        userProfile = userFacade.getUserProfile();
        binder.readBean(userProfile);

        byte[] profileImage = userProfile.getProfileImage();
        if (profileImage != null) {
            setUserImage();
        }

        uploadImage();
        setupButtonFunctionality();
    }

    private void setupButtonFunctionality() {
        addFunctionalityForEditButton();
        addFunctionalityForCancelButton();
        addFunctionalityForSaveButton();
    }

    private void updateButtonsForEdit() {
        userCredentialsLayout = profileForm.getUserCredentialsLayout();
        userCredentialsLayout.remove(edit);
        saveAndEditButtons = new HorizontalLayout(profileForm.getSave(), profileForm.getCancel());
        userCredentialsLayout.add(saveAndEditButtons);
    }

    private void addFunctionalityForEditButton() {
        edit = profileForm.getEdit();
        edit.addClickListener(event -> {
            profileForm.setCredentialFieldsReadOnly(false);
            updateButtonsForEdit();
        });
    }

    private void addFunctionalityForSaveButton() {
        Button save = profileForm.getSave();
        save.addClickListener(event -> {
            try {
                UserDto userDto = new UserDto();
                binder.writeBean(userDto);
                boolean isChanged = userFacade.updateUser(userDto);

                if (isChanged) {
                    showSuccess("Successful Updated");
                }
                binder.readBean(userFacade.getUserProfile());
                updateButtonsAfterAction();
            } catch (ValidationException e) {
                showNotification(e.getMessage());
            }
        });
    }

    private void addFunctionalityForCancelButton() {
        Button cancel = profileForm.getCancel();
        cancel.addClickListener(event -> updateButtonsAfterAction());
    }

    private void updateButtonsAfterAction() {
        userCredentialsLayout.remove(saveAndEditButtons);
        userCredentialsLayout.add(edit);
        profileForm.setCredentialFieldsReadOnly(true);
    }

    private void uploadImage() {
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = profileForm.getUpload();
        upload.setReceiver(memoryBuffer);

        upload.addSucceededListener(event -> {
            InputStream inputStream = memoryBuffer.getInputStream();
            try {
                MultipartFile file = new MockMultipartFile("image", inputStream);
                UserDto uploaded = userFacade.uploadImage(file);
                userProfile.setProfileImage(uploaded.getProfileImage());
                setUserImage();

                upload.clearFileList();
            } catch (IOException e) {
                showNotification(e.getMessage());
            }
        });
    }

    private void setUserImage() {
        Image image = profileForm.getImage();
        StreamResource byteArrayInputStream =
                new StreamResource("image", () ->
                        new ByteArrayInputStream(userProfile.getProfileImage()));
        image.setSrc(byteArrayInputStream);
    }
}
