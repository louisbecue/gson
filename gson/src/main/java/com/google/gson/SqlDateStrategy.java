package com.google.gson;

import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.util.Date;

/** Strategy for handling SQL date types. */
public class SqlDateStrategy implements DateFormattingStrategy {
  private final DateFormattingStrategy delegate;

  public SqlDateStrategy(DateFormattingStrategy delegate) {
    this.delegate = delegate;
  }

  @Override
  public String format(Date date) {
    return delegate.format(date);
  }

  @Override
  public Date parse(String dateStr) throws ParseException {
    return delegate.parse(dateStr);
  }

  @Override
  public boolean canHandle(Class<?> dateType) {
    return java.sql.Date.class.isAssignableFrom(dateType)
        || java.sql.Timestamp.class.isAssignableFrom(dateType)
        || java.sql.Time.class.isAssignableFrom(dateType);
  }

  /** Converts a standard Date to the appropriate SQL date type. */
  public Date convertToSqlType(Date date, Class<?> targetType) {
    if (date == null) return null;

    if (java.sql.Date.class.isAssignableFrom(targetType)) {
      return new java.sql.Date(date.getTime());
    } else if (java.sql.Timestamp.class.isAssignableFrom(targetType)) {
      return new java.sql.Timestamp(date.getTime());
    } else if (java.sql.Time.class.isAssignableFrom(targetType)) {
      return new java.sql.Time(date.getTime());
    }

    return date;
  }

  @Override
  public TypeAdapterFactory createAdapterFactory(Class<? extends Date> dateType) {
    return new TypeAdapterFactory() {
      @SuppressWarnings("unchecked")
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getRawType() == dateType) {
          return (TypeAdapter<T>) new DateTypeAdapter(SqlDateStrategy.this, dateType);
        }
        return null;
      }
    };
  }
}
