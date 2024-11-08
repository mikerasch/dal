package com.michael.dal.database.repositories;

import com.michael.dal.database.generated.tables.records.CurlActivityRecord;
import java.util.List;

public interface CurlActivityRepository {
  void saveCurlActivity(String body, String environment, String serviceName);

  List<CurlActivityRecord> findAll();
}
