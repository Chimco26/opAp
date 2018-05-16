package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

public interface GetAllRecipeCallback {

    void onGetAllRecipeSuccess();

    void onGetAllRecipeFailed(ErrorObjectInterface reason);
}
