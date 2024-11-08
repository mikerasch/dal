package com.michael.dal.database;

import java.sql.SQLException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

public class Database {
  private final DSLContext dslContext;

  public Database() throws SQLException {
    SQLiteConfig config = new SQLiteConfig();
    config.enforceForeignKeys(true);

    SQLiteDataSource dataSource = new SQLiteDataSource(config);
    String jdbcUrl = "jdbc:sqlite:database.db";
    dataSource.setUrl(jdbcUrl);

    dslContext = DSL.using(dataSource.getConnection(), SQLDialect.SQLITE);
  }

  public DSLContext getDslContext() {
    return dslContext;
  }
}
