package com.michael.dal.services.discovery;

import com.michael.dal.services.discovery.models.ServiceInformation;
import java.util.List;

public interface ServiceLocator {
  List<ServiceInformation> fetchAllServices(String env);
}
