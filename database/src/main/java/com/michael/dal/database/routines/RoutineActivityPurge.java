package com.michael.dal.database.routines;

import com.google.inject.Inject;
import com.michael.dal.database.Database;
import com.michael.dal.database.generated.tables.RoutineChecks;
import com.michael.dal.database.routines.common.Routine;
import com.michael.dal.database.routines.common.RoutineName;
import com.michael.dal.database.routines.common.Status;
import com.michael.dal.database.routines.common.TimeInterval;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class RoutineActivityPurge extends Routine {
  private final Database database;

  @Inject
  public RoutineActivityPurge(final Database database) {
    this.database = database;
  }

  @Override
  public RoutineName getRoutineName() {
    return RoutineName.ROUTINE_ACTIVITY_PURGE;
  }

  @Override
  public TimeInterval getInterval() {
    return new TimeInterval(TimeUnit.DAYS, 30);
  }

  @Override
  public Database getDatabase() {
    return database;
  }

  @Override
  public void run() {
    assert database != null;
    LocalDateTime purgeBefore = LocalDateTime.now().minusDays(30);
    database
        .getDslContext()
        .deleteFrom(RoutineChecks.ROUTINE_CHECKS)
        .where(RoutineChecks.ROUTINE_CHECKS.STATUS.eq(Status.COMPLETED.name()))
        .and(RoutineChecks.ROUTINE_CHECKS.CREATED_AT.lt(purgeBefore))
        .execute();
  }
}
