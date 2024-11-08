package com.michael.dal.services.home;

import com.michael.dal.vertx.models.CurlActivity;
import io.vertx.core.eventbus.EventBus;
import java.util.List;

public interface HomeService {
  void kickStartDiscoverAllServices(String env);

  List<String> autoComplete(String text, String environment, int limit);

  String getSwaggerUrl(String selectedItem, String env);

  EventBus getEventBus();

  String executeCurlCommand(String body, String env, String serviceName);

  List<CurlActivity> fetchAllCurlActivities();
}
