/*
 * This file is generated by jOOQ.
 */
package com.michael.dal.database.generated.tables.records;

import com.michael.dal.database.generated.tables.RoutineChecks;
import java.time.LocalDateTime;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

/** This class is generated by jOOQ. */
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class RoutineChecksRecord extends UpdatableRecordImpl<RoutineChecksRecord> {

  private static final long serialVersionUID = 1L;

  /** Setter for <code>ROUTINE_CHECKS.ID</code>. */
  public void setId(Integer value) {
    set(0, value);
  }

  /** Getter for <code>ROUTINE_CHECKS.ID</code>. */
  public Integer getId() {
    return (Integer) get(0);
  }

  /** Setter for <code>ROUTINE_CHECKS.CREATED_AT</code>. */
  public void setCreatedAt(LocalDateTime value) {
    set(1, value);
  }

  /** Getter for <code>ROUTINE_CHECKS.CREATED_AT</code>. */
  public LocalDateTime getCreatedAt() {
    return (LocalDateTime) get(1);
  }

  /** Setter for <code>ROUTINE_CHECKS.RUN_AT</code>. */
  public void setRunAt(LocalDateTime value) {
    set(2, value);
  }

  /** Getter for <code>ROUTINE_CHECKS.RUN_AT</code>. */
  public LocalDateTime getRunAt() {
    return (LocalDateTime) get(2);
  }

  /** Setter for <code>ROUTINE_CHECKS.STATUS</code>. */
  public void setStatus(String value) {
    set(3, value);
  }

  /** Getter for <code>ROUTINE_CHECKS.STATUS</code>. */
  public String getStatus() {
    return (String) get(3);
  }

  /** Setter for <code>ROUTINE_CHECKS.PROGRAM</code>. */
  public void setProgram(String value) {
    set(4, value);
  }

  /** Getter for <code>ROUTINE_CHECKS.PROGRAM</code>. */
  public String getProgram() {
    return (String) get(4);
  }

  // -------------------------------------------------------------------------
  // Primary key information
  // -------------------------------------------------------------------------

  @Override
  public Record1<Integer> key() {
    return (Record1) super.key();
  }

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /** Create a detached RoutineChecksRecord */
  public RoutineChecksRecord() {
    super(RoutineChecks.ROUTINE_CHECKS);
  }

  /** Create a detached, initialised RoutineChecksRecord */
  public RoutineChecksRecord(
      Integer id, LocalDateTime createdAt, LocalDateTime runAt, String status, String program) {
    super(RoutineChecks.ROUTINE_CHECKS);

    setId(id);
    setCreatedAt(createdAt);
    setRunAt(runAt);
    setStatus(status);
    setProgram(program);
    resetChangedOnNotNull();
  }
}
