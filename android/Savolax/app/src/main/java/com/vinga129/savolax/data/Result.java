package com.vinga129.savolax.data;

import androidx.annotation.NonNull;
import java.util.List;
import java.util.Map;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result {
    // hide the private constructor to limit subclass types (Success, Error)
    private Result() {
    }

    @NonNull
    @Override
    @SuppressWarnings("rawtypes")
    public String toString() {
        if (this instanceof Result.Success) {
            Result.Success success = (Result.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return "Error[exception=" + error.getError() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends Result {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends Result {
        private Exception exception;
        private Map<String, List<String>> errorMap;
        private int errorCode;
        private String errorMessage;

        public Error(Exception exception) {
            this.exception = exception;
        }

        public Error(Map<String, List<String>> errorMap) {
            this.errorMap = errorMap;
        }

        public Error(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public Exception getException() {
            return this.exception;
        }

        public Map<String, List<String>> getErrorMap() {
            return errorMap;
        }

        public String getError() {
            return (errorCode != 0) ? errorCode + ": " + errorMessage : null;
        }
    }
}