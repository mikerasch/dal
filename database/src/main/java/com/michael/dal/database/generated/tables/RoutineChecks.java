/*
 * This file is generated by jOOQ.
 */
package com.michael.dal.database.generated.tables;

import com.michael.dal.database.generated.DefaultSchema;
import com.michael.dal.database.generated.Keys;
import com.michael.dal.database.generated.tables.records.RoutineChecksRecord;
import java.time.LocalDateTime;
import java.util.Collection;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

/** This class is generated by jOOQ. */
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class RoutineChecks extends TableImpl<RoutineChecksRecord> {

  private static final long serialVersionUID = 1L;

  /** The reference instance of <code>ROUTINE_CHECKS</code> */
  public static final RoutineChecks ROUTINE_CHECKS = new RoutineChecks();

  /** The class holding records for this type */
  @Override
  public Class<RoutineChecksRecord> getRecordType() {
    return RoutineChecksRecord.class;
  }

  /** The column <code>ROUTINE_CHECKS.ID</code>. */
  public final TableField<RoutineChecksRecord, Integer> ID =
      createField(DSL.name("ID"), SQLDataType.INTEGER.identity(true), this, "");

  /** The column <code>ROUTINE_CHECKS.CREATED_AT</code>. */
  public final TableField<RoutineChecksRecord, LocalDateTime> CREATED_AT =
      createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "");

  /** The column <code>ROUTINE_CHECKS.RUN_AT</code>. */
  public final TableField<RoutineChecksRecord, LocalDateTime> RUN_AT =
      createField(DSL.name("RUN_AT"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "");

  /** The column <code>ROUTINE_CHECKS.STATUS</code>. */
  public final TableField<RoutineChecksRecord, String> STATUS =
      createField(DSL.name("STATUS"), SQLDataType.VARCHAR(15).nullable(false), this, "");

  /** The column <code>ROUTINE_CHECKS.PROGRAM</code>. */
  public final TableField<RoutineChecksRecord, String> PROGRAM =
      createField(DSL.name("PROGRAM"), SQLDataType.VARCHAR(50).nullable(false), this, "");

  private RoutineChecks(Name alias, Table<RoutineChecksRecord> aliased) {
    this(alias, aliased, (Field<?>[]) null, null);
  }

  private RoutineChecks(
      Name alias, Table<RoutineChecksRecord> aliased, Field<?>[] parameters, Condition where) {
    super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
  }

  /** Create an aliased <code>ROUTINE_CHECKS</code> table reference */
  public RoutineChecks(String alias) {
    this(DSL.name(alias), ROUTINE_CHECKS);
  }

  /** Create an aliased <code>ROUTINE_CHECKS</code> table reference */
  public RoutineChecks(Name alias) {
    this(alias, ROUTINE_CHECKS);
  }

  /** Create a <code>ROUTINE_CHECKS</code> table reference */
  public RoutineChecks() {
    this(DSL.name("ROUTINE_CHECKS"), null);
  }

  @Override
  public Schema getSchema() {
    return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
  }

  @Override
  public Identity<RoutineChecksRecord, Integer> getIdentity() {
    return (Identity<RoutineChecksRecord, Integer>) super.getIdentity();
  }

  @Override
  public UniqueKey<RoutineChecksRecord> getPrimaryKey() {
    return Keys.ROUTINE_CHECKS__PK_ROUTINE_CHECKS;
  }

  @Override
  public RoutineChecks as(String alias) {
    return new RoutineChecks(DSL.name(alias), this);
  }

  @Override
  public RoutineChecks as(Name alias) {
    return new RoutineChecks(alias, this);
  }

  @Override
  public RoutineChecks as(Table<?> alias) {
    return new RoutineChecks(alias.getQualifiedName(), this);
  }

  /** Rename this table */
  @Override
  public RoutineChecks rename(String name) {
    return new RoutineChecks(DSL.name(name), null);
  }

  /** Rename this table */
  @Override
  public RoutineChecks rename(Name name) {
    return new RoutineChecks(name, null);
  }

  /** Rename this table */
  @Override
  public RoutineChecks rename(Table<?> name) {
    return new RoutineChecks(name.getQualifiedName(), null);
  }

  /** Create an inline derived table from this table */
  @Override
  public RoutineChecks where(Condition condition) {
    return new RoutineChecks(getQualifiedName(), aliased() ? this : null, null, condition);
  }

  /** Create an inline derived table from this table */
  @Override
  public RoutineChecks where(Collection<? extends Condition> conditions) {
    return where(DSL.and(conditions));
  }

  /** Create an inline derived table from this table */
  @Override
  public RoutineChecks where(Condition... conditions) {
    return where(DSL.and(conditions));
  }

  /** Create an inline derived table from this table */
  @Override
  public RoutineChecks where(Field<Boolean> condition) {
    return where(DSL.condition(condition));
  }

  /** Create an inline derived table from this table */
  @Override
  @PlainSQL
  public RoutineChecks where(SQL condition) {
    return where(DSL.condition(condition));
  }

  /** Create an inline derived table from this table */
  @Override
  @PlainSQL
  public RoutineChecks where(@Stringly.SQL String condition) {
    return where(DSL.condition(condition));
  }

  /** Create an inline derived table from this table */
  @Override
  @PlainSQL
  public RoutineChecks where(@Stringly.SQL String condition, Object... binds) {
    return where(DSL.condition(condition, binds));
  }

  /** Create an inline derived table from this table */
  @Override
  @PlainSQL
  public RoutineChecks where(@Stringly.SQL String condition, QueryPart... parts) {
    return where(DSL.condition(condition, parts));
  }

  /** Create an inline derived table from this table */
  @Override
  public RoutineChecks whereExists(Select<?> select) {
    return where(DSL.exists(select));
  }

  /** Create an inline derived table from this table */
  @Override
  public RoutineChecks whereNotExists(Select<?> select) {
    return where(DSL.notExists(select));
  }
}
