package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/** This is a simple adapter for Date. It uses a formatting strategy to change the date. */
public class DateTypeAdapter extends TypeAdapter<Date> {
  public static final Class<? extends Date> DEFAULT = Date.class;
  public static final Class<? extends Date> SQL_DATE = java.sql.Date.class;
  public static final Class<? extends Date> SQL_TIMESTAMP = java.sql.Timestamp.class;

  private final DateFormattingStrategy strategy;
  private final Class<? extends Date> dateType;

  public DateTypeAdapter(DateFormattingStrategy strategy, Class<? extends Date> dateType) {
    this.strategy = strategy;
    this.dateType = dateType;
  }

  @Override
  public void write(JsonWriter out, Date value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    out.value(strategy.format(value));
  }

  @Override
  public Date read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    String dateStr = in.nextString();

    try {
      Date date = strategy.parse(dateStr);
      if (strategy instanceof SqlDateStrategy && java.sql.Date.class.isAssignableFrom(dateType)) {
        return ((SqlDateStrategy) strategy).convertToSqlType(date, dateType);
      }

      return date;
    } catch (ParseException e) {
      throw new IOException("Failed to parse date: " + dateStr, e);
    }
  }
}
