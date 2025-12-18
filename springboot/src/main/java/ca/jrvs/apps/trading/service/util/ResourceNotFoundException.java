package ca.jrvs.apps.trading.service.util;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
