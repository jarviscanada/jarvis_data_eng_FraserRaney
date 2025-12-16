package ca.jrvs.apps.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class Application {

public static void main(String args[]) {
  SpringApplication.run(Application.class, args);

  System.out.println("Hello, world!");
}
}
