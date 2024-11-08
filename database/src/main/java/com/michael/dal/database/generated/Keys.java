/*
 * This file is generated by jOOQ.
 */
package com.michael.dal.database.generated;


import com.michael.dal.database.generated.tables.CurlActivity;
import com.michael.dal.database.generated.tables.FlywaySchemaHistory;
import com.michael.dal.database.generated.tables.records.CurlActivityRecord;
import com.michael.dal.database.generated.tables.records.FlywaySchemaHistoryRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<CurlActivityRecord> CURL_ACTIVITY__PK_CURL_ACTIVITY = Internal.createUniqueKey(CurlActivity.CURL_ACTIVITY, DSL.name("pk_CURL_ACTIVITY"), new TableField[] { CurlActivity.CURL_ACTIVITY.ID }, true);
    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY__PK_FLYWAY_SCHEMA_HISTORY = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("pk_flyway_schema_history"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
}