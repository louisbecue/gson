package com.google.gson;

import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;

/** Date formatting strategy based on a date pattern. */
public class PatternBasedDateStrategy implements DateFormattingStrategy {
  private final String pattern;
  private final ThreadLocal<SimpleDateFormat> formatThreadLocal;

  public PatternBasedDateStrategy(String pattern) {
    this.pattern = Objects.requireNonNull(pattern);
    this.formatThreadLocal =
        ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern, Locale.US));
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
          return (TypeAdapter<T>) new DateTypeAdapter(PatternBasedDateStrategy.this, dateType);
        }
        return null;
      }
    };
  }

  @Override
  public String toString() {
    return "PatternBasedDateStrategy[pattern=" + pattern + "]";
  }
}
