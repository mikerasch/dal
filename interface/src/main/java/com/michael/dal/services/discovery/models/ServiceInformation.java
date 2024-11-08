package com.michael.dal.services.discovery.models;

public record ServiceInformation(
    String serviceName, String serviceAddress, String servicePort, String contextPath) {}
