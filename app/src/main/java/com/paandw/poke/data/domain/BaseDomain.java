package com.paandw.poke.data.domain;

import retrofit2.Response;

public abstract class BaseDomain {
    protected final void handleResponse(IDomainCallback callback, Response response) {
        if (response.isSuccessful()) {
            callback.success(response.body());
        } else {
            callback.failure(checkErrorMessage(response.message()));
        }
    }

    protected final void handleException(IDomainCallback callback, Throwable t) {
        callback.failure(checkErrorMessage(t.getMessage()));
    }

    private String checkErrorMessage(String error) {
        if (error == null) {
            return "Network error. Please try again later.";
        } else {
            return error;
        }
    }
}
