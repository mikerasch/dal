package com.michael.dal.base.adapter.exceptions;

public class UnauthorizedException extends RuntimeException {
  /**
   * Exception handling unauthorized users. For example, if OAUTH or JWT token is invalid, this
   * exception will be thrown.
   */
  public UnauthorizedException(String message) {
    super(message);
  }
}
