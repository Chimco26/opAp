package com.operatorsapp;

import com.operatorsapp.utils.TimeUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeTest {

    @Test
    public void getTimeFromMinute() throws Exception {
        assertEquals("2d 4hr 33min", TimeUtils.getTimeFromMinute(3153));
    }
}
