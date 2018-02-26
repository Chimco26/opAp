package com.ravtech.david.sqlcore;

import android.content.Context;

import com.operators.shiftloginfra.Event;
import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio3.sqlite.queries.Query;
import com.ravtech.david.sqlcore.interfaces.SqlCursorListener;
import com.ravtech.david.sqlcore.interfaces.SqlGetListener;
import com.ravtech.david.sqlcore.interfaces.SqlPutListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by david on 17 ינואר 2018
 */

public class EventSqlActions {

    private StorIOSQLite storIOSQLite;


    public EventSqlActions(Context context) {
        storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new SqlHelper(context)).build();
    }

    public void getCursor(SqlCursorListener listener) {
        storIOSQLite.get().cursor()
                .withQuery(Query.builder().table(Properties.EVENT_TABLE_NAME).build())
                .prepare().asRxSingle().observeOn(Schedulers.computation())
                .subscribe((cursor, throwable)
                        -> listener.onGetCursor(cursor));
    }

    public void getListAll(SqlGetListener listener) {

        storIOSQLite.get().listOfObjects(Event.class).withQuery(Query.builder().table(Properties.EVENT_TABLE_NAME).build()).prepare().asRxSingle().observeOn(Schedulers.computation()).subscribe((events, throwable) -> listener.onGetList(events));
    }


    public void put(SqlPutListener listener, ArrayList<Event> event) {

        storIOSQLite.put().objects(event).prepare().asRxSingle().observeOn(Schedulers.computation()).subscribe(new SingleObserver<PutResults<Event>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(PutResults<Event> eventPutResults) {

                listener.onSuccess();

            }

            @Override
            public void onError(Throwable e) {

                listener.onError();

            }
        });
    }


}
