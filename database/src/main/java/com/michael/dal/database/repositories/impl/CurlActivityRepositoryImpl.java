package com.michael.dal.database.repositories.impl;

import com.google.inject.Inject;
import com.michael.dal.annotations.Repository;
import com.michael.dal.database.Database;
import com.michael.dal.database.generated.tables.CurlActivity;
import com.michael.dal.database.generated.tables.records.CurlActivityRecord;
import com.michael.dal.database.repositories.CurlActivityRepository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CurlActivityRepositoryImpl implements CurlActivityRepository {
  private final Database database;

  @Inject
  public CurlActivityRepositoryImpl(final Database database) {
    this.database = database;
  }

  @Override
  public void saveCurlActivity(
      final String body, final String environment, final String serviceName) {
    CurlActivityRecord activity = database.getDslContext().newRecord(CurlActivity.CURL_ACTIVITY);
    activity.setCreatedAt(LocalDateTime.now());
    activity.setEnvironment(environment);
    activity.setCommand(body);
    activity.setServiceName(serviceName);
    activity.insert();
  }

  @Override
  public List<CurlActivityRecord> findAll() {
    return database.getDslContext().selectFrom(CurlActivity.CURL_ACTIVITY).fetch();
  }
}
