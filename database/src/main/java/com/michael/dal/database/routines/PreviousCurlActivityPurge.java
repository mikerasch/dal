package com.michael.dal.database.routines;

import com.google.inject.Inject;
import com.michael.dal.database.Database;
import com.michael.dal.database.generated.tables.CurlActivity;
import com.michael.dal.database.generated.tables.RoutineChecks;
import com.michael.dal.database.routines.common.Routine;
import com.michael.dal.database.routines.common.RoutineName;
import com.michael.dal.database.routines.common.Status;
import com.michael.dal.database.routines.common.TimeInterval;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class PreviousCurlActivityPurge extends Routine {
  private final Database database;

  @Inject
  public PreviousCurlActivityPurge(final Database database) {
    this.database = database;
  }

  @Override
  public RoutineName getRoutineName() {
    return RoutineName.PREVIOUS_CURL_ACTIVITY;
  }

  @Override
  public TimeInterval getInterval() {
    return new TimeInterval(TimeUnit.DAYS, 1);
  }

  @Override
  public Database getDatabase() {
    return database;
  }

  @Override
  public void run() {
    assert database != null;
    LocalDateTime purgeBefore = LocalDateTime.now().minusDays(7);
    database
        .getDslContext()
        .deleteFrom(CurlActivity.CURL_ACTIVITY)
        .where(CurlActivity.CURL_ACTIVITY.CREATED_AT.lt(purgeBefore))
        .execute();
    closeRecord();
  }

  private void closeRecord() {
    database
        .getDslContext()
        .update(CurlActivity.CURL_ACTIVITY)
        .set(RoutineChecks.ROUTINE_CHECKS.STATUS, Status.COMPLETED.name())
        .where(RoutineChecks.ROUTINE_CHECKS.PROGRAM.eq(getRoutineName().name()))
        .and(RoutineChecks.ROUTINE_CHECKS.STATUS.eq(Status.PENDING.name()))
        .execute();
  }
}
