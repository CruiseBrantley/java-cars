package com.javacars.javacars;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarLog implements Serializable
{
    private final String msg;
    private final String date;

    public CarLog(@JsonProperty("msg") String msg)
    {
        this.msg = msg;
        Date nonFormattedDate = new Date();
        String strDateFormat = "yyyy-MM-dd hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        date = dateFormat.format(nonFormattedDate);
    }
}
