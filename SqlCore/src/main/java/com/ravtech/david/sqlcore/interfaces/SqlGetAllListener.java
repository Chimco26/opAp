package com.ravtech.david.sqlcore.interfaces;


import com.ravtech.david.sqlcore.Event;

import java.util.List;

/**
 * Created by david on 17 ינואר 2018.
 */

public interface SqlGetAllListener {

    void onGetList(List<Event> events);

}
