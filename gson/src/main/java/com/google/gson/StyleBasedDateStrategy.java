package com.google.gson;

import com.google.gson.reflect.TypeToken;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/** A strategy for formatting dates using specific styles for date and time. */
public class StyleBasedDateStrategy implements DateFormattingStrategy {
  private final int dateStyle;
  private final int timeStyle;
  private final ThreadLocal<DateFormat> formatThreadLocal;

  public StyleBasedDateStrategy(int dateStyle, int timeStyle) {
    this.dateStyle = dateStyle;
    this.timeStyle = timeStyle;
    this.formatThreadLocal =
        ThreadLocal.withInitial(
            () -> DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US));
  }

  @Override
  public String format(Date date) {
    return formatThreadLocal.get().format(date);
  }

  @Override
  public Date parse(String dateStr) throws ParseException {
    return formatThreadLocal.get().parse(dateStr);
  }

  @Override
  public boolean canHandle(Class<?> dateType) {
    return Date.class.isAssignableFrom(dateType) && !isSqlDateType(dateType);
  }

  private boolean isSqlDateType(Class<?> dateType) {
    return java.sql.Date.class.isAssignableFrom(dateType)
        || java.sql.Timestamp.class.isAssignableFrom(dateType)
        || java.sql.Time.class.isAssignableFrom(dateType);
  }

  @Override
  public TypeAdapterFactory createAdapterFactory(Class<? extends Date> dateType) {
    return new TypeAdapterFactory() {
      @SuppressWarnings("unchecked")
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getRawType() == dateType) {
          return (TypeAdapter<T>) new DateTypeAdapter(StyleBasedDateStrategy.this, dateType);
        }
        return null;
      }
    };
  }
}
