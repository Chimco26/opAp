package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

public interface GetAllRecipeCallback {

    void onGetAllRecipeSuccess(Object response);

    void onGetAllRecipeFailed(ErrorObjectInterface reason);
}
