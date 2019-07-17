package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

public interface GetAllRecipeCallback {

    void onGetAllRecipeSuccess(Object response);

    void onGetAllRecipeFailed(StandardResponse reason);
}
