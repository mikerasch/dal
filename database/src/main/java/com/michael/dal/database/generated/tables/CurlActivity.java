/*
 * This file is generated by jOOQ.
 */
package com.michael.dal.database.generated.tables;


import com.michael.dal.database.generated.DefaultSchema;
import com.michael.dal.database.generated.Keys;
import com.michael.dal.database.generated.tables.records.CurlActivityRecord;

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


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class CurlActivity extends TableImpl<CurlActivityRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>CURL_ACTIVITY</code>
     */
    public static final CurlActivity CURL_ACTIVITY = new CurlActivity();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CurlActivityRecord> getRecordType() {
        return CurlActivityRecord.class;
    }

    /**
     * The column <code>CURL_ACTIVITY.ID</code>.
     */
    public final TableField<CurlActivityRecord, Integer> ID = createField(DSL.name("ID"), SQLDataType.INTEGER.identity(true), this, "");

    /**
     * The column <code>CURL_ACTIVITY.CREATED_AT</code>.
     */
    public final TableField<CurlActivityRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "");

    /**
     * The column <code>CURL_ACTIVITY.ENVIRONMENT</code>.
     */
    public final TableField<CurlActivityRecord, String> ENVIRONMENT = createField(DSL.name("ENVIRONMENT"), SQLDataType.VARCHAR(20).nullable(false), this, "");

    /**
     * The column <code>CURL_ACTIVITY.SERVICE_NAME</code>.
     */
    public final TableField<CurlActivityRecord, String> SERVICE_NAME = createField(DSL.name("SERVICE_NAME"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>CURL_ACTIVITY.COMMAND</code>.
     */
    public final TableField<CurlActivityRecord, String> COMMAND = createField(DSL.name("COMMAND"), SQLDataType.CLOB.nullable(false), this, "");

    private CurlActivity(Name alias, Table<CurlActivityRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private CurlActivity(Name alias, Table<CurlActivityRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>CURL_ACTIVITY</code> table reference
     */
    public CurlActivity(String alias) {
        this(DSL.name(alias), CURL_ACTIVITY);
    }

    /**
     * Create an aliased <code>CURL_ACTIVITY</code> table reference
     */
    public CurlActivity(Name alias) {
        this(alias, CURL_ACTIVITY);
    }

    /**
     * Create a <code>CURL_ACTIVITY</code> table reference
     */
    public CurlActivity() {
        this(DSL.name("CURL_ACTIVITY"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Identity<CurlActivityRecord, Integer> getIdentity() {
        return (Identity<CurlActivityRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<CurlActivityRecord> getPrimaryKey() {
        return Keys.CURL_ACTIVITY__PK_CURL_ACTIVITY;
    }

    @Override
    public CurlActivity as(String alias) {
        return new CurlActivity(DSL.name(alias), this);
    }

    @Override
    public CurlActivity as(Name alias) {
        return new CurlActivity(alias, this);
    }

    @Override
    public CurlActivity as(Table<?> alias) {
        return new CurlActivity(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public CurlActivity rename(String name) {
        return new CurlActivity(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CurlActivity rename(Name name) {
        return new CurlActivity(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public CurlActivity rename(Table<?> name) {
        return new CurlActivity(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CurlActivity where(Condition condition) {
        return new CurlActivity(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CurlActivity where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CurlActivity where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CurlActivity where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CurlActivity where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CurlActivity where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CurlActivity where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CurlActivity where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CurlActivity whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CurlActivity whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
