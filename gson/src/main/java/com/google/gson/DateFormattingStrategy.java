package com.google.gson;

import java.text.ParseException;
import java.util.Date;

/** A simple way to format dates in Gson. */
public interface DateFormattingStrategy {
  /**
   * Convert a date to text.
   *
   * @param date the date to change
   * @return the text version of the date
   */
  String format(Date date);

  /**
   * Change text to a date.
   *
   * @param dateStr the text to change
   * @return the date
   * @throws ParseException if the text is not correct
   */
  Date parse(String dateStr) throws ParseException;

  /**
   * Check if this strategy supports the date type.
   *
   * @param dateType the date type to check
   * @return true if supported
   */
  boolean canHandle(Class<?> dateType);

  /**
   * Create a type adapter factory for the date.
   *
   * @param dateType the date type to use
   * @return the type adapter factory
   */
  TypeAdapterFactory createAdapterFactory(Class<? extends Date> dateType);
}
