package com.michael.dal.utils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class TimeAdding {
  private TimeAdding() {}

  public static LocalDateTime addDuration(LocalDateTime runTime, long duration, TimeUnit unit) {
    return switch (unit) {
      case DAYS -> runTime.plusDays(duration);
      case HOURS -> runTime.plusHours(duration);
      case MINUTES -> runTime.plusMinutes(duration);
      case SECONDS -> runTime.plusSeconds(duration);
      default -> throw new IllegalArgumentException("Unsupported TimeUnit: " + unit);
    };
  }
}
