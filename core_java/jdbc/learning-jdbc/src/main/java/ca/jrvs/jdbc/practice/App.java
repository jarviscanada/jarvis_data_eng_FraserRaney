package ca.jrvs.jdbc.practice;

import ca.jrvs.jdbc.practice.data.dao.ServiceDao;
import ca.jrvs.jdbc.practice.data.entity.Service;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

      ServiceDao serviceDao = new ServiceDao();
      List<Service> services = serviceDao.getAll();
      System.out.println("**** SERVICES ****");
      System.out.println("\n*** GET_ALL ***");
      services.forEach(System.out::println);    }
}
