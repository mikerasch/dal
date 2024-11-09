package com.michael.dal.database.routines.common;

import com.michael.dal.database.Database;
import com.michael.dal.database.generated.tables.RoutineChecks;
import com.michael.dal.database.generated.tables.records.RoutineChecksRecord;
import com.michael.dal.utils.TimeAdding;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Routine {
  private static final Logger logger = LoggerFactory.getLogger(Routine.class);

  public abstract RoutineName getRoutineName();

  public abstract TimeInterval getInterval();

  public abstract Database getDatabase();

  public abstract void run();

  public void onStartup() {
    Database database = getDatabase();
    assert database != null;
    RoutineChecksRecord check = fetchPendingRecord(database);

    if (check != null) {
      return;
    }
    logger.info(
        "Routine {} was not found in the database, creating a new record", getRoutineName());
    createRecord(database);
  }

  private RoutineChecksRecord fetchPendingRecord(final Database database) {
    return database
        .getDslContext()
        .selectFrom(RoutineChecks.ROUTINE_CHECKS)
        .where(RoutineChecks.ROUTINE_CHECKS.PROGRAM.eq(getRoutineName().name()))
        .and(RoutineChecks.ROUTINE_CHECKS.STATUS.eq(Status.PENDING.name()))
        .fetchOne();
  }

  private void createRecord(final Database database) {
    TimeInterval interval = getInterval();
    LocalDateTime runAt =
        TimeAdding.addDuration(LocalDateTime.now(), interval.interval(), interval.timeUnit());
    database
        .getDslContext()
        .insertInto(RoutineChecks.ROUTINE_CHECKS)
        .set(RoutineChecks.ROUTINE_CHECKS.PROGRAM, getRoutineName().name())
        .set(RoutineChecks.ROUTINE_CHECKS.CREATED_AT, LocalDateTime.now())
        .set(RoutineChecks.ROUTINE_CHECKS.RUN_AT, runAt)
        .set(RoutineChecks.ROUTINE_CHECKS.STATUS, Status.PENDING.name())
        .execute();
  }
}
