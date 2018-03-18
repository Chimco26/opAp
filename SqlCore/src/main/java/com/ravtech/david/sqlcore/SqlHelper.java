package com.ravtech.david.sqlcore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by david on 17 ינואר 2018.
 */

public class SqlHelper extends SQLiteOpenHelper {

    private String TAG = SqlHelper.class.getSimpleName();

    SqlHelper(Context context) {

        super(context, Properties.EVENT_TABLE_NAME, null, Properties.VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG,"onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.d(TAG,"onUpgrade");
    }

}
