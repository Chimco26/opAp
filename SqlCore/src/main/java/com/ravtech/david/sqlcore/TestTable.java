package com.ravtech.david.sqlcore;

import org.litepal.crud.DataSupport;

/**
 * Created by david vardi on Ravtech.
 */

public class TestTable extends DataSupport {

    private String name ;
    private int id ;

    public TestTable(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
