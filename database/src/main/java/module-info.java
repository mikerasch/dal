module database {
  requires com.google.guice;
  requires java.sql;
  requires org.xerial.sqlitejdbc;
  requires org.jooq;
  requires annotations;

  exports com.michael.dal.database;
  exports com.michael.dal.database.generated;
  exports com.michael.dal.database.generated.tables;
  exports com.michael.dal.database.generated.tables.records;
  exports com.michael.dal.database.repositories;
  exports com.michael.dal.database.module;
  exports com.michael.dal.database.repositories.impl to
      com.google.guice;
}
