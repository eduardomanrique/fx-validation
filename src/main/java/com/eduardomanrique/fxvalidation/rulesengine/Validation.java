package com.eduardomanrique.fxvalidation.rulesengine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Validation {

    @Getter
    private boolean valid = true;
    private List<ErrorMessage> errorMessages = new ArrayList<>();

    public final void addValidationError(String code, String msg) {
        valid = false;
        errorMessages.add(new ErrorMessage(code, msg));
    }

    public final List<ErrorMessage> getErrorMessages() {
        return Collections.unmodifiableList(errorMessages);
    }

    @AllArgsConstructor
    public class ErrorMessage {
        @Getter
        private String code;
        @Getter
        private String message;
    }
}
