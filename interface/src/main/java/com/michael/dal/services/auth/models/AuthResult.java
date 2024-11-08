package com.michael.dal.services.auth.models;

public record AuthResult(Integer statusCode, String token, String environment) {}
