package com.michael.dal.database.routines.common;

import java.util.concurrent.TimeUnit;

public record TimeInterval(TimeUnit timeUnit, int interval) {
  public TimeInterval {
    if (interval < 0) {
      throw new IllegalArgumentException("Interval must be greater than or equal to 0");
    }
  }
}
