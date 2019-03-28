package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface GetAllRecipeCallback {

    void onGetAllRecipeSuccess(Object response);

    void onGetAllRecipeFailed(ErrorObjectInterface reason);
}
