package ca.jrvs.jdbc.practice;

import ca.jrvs.jdbc.practice.data.dao.CustomerDao;
import ca.jrvs.jdbc.practice.data.dao.ProductVendorDao;
import ca.jrvs.jdbc.practice.data.dao.ServiceDao;
import ca.jrvs.jdbc.practice.data.entity.Customer;
import ca.jrvs.jdbc.practice.data.entity.Product;
import ca.jrvs.jdbc.practice.data.entity.Service;
import ca.jrvs.jdbc.practice.data.entity.Vendor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {

    ServiceDao serviceDao = new ServiceDao();
    List<Service> services = serviceDao.getAll();
    System.out.println("**** SERVICES ****");
    System.out.println("\n*** GET_ALL ***");
    services.forEach(System.out::println);
    Optional<Service> service = serviceDao.getOne(services.get(0).getServiceId());
    System.out.println("\n*** GET ONE ***\n" + service.get());
    Service newService = new Service();
    newService.setName("FooBarBaz" + System.currentTimeMillis());
    newService.setPrice(new BigDecimal(4.35));
    newService = serviceDao.create(newService);
    System.out.println("\n*** CREATE ***\n" + newService);
    newService.setPrice(new BigDecimal(13.45));
    newService = serviceDao.update(newService);
    System.out.println("\n*** UPDATE ***\n" + newService);
    serviceDao.delete(newService.getServiceId());
    System.out.println("\n*** DELETE ***\n");

    CustomerDao customerDao = new CustomerDao();
    List<Customer> customers = customerDao.getAll();
    System.out.println("**** CUSTOMERS ****");
    System.out.println("\n*** GET_ALL ***");
    customers.forEach(System.out::println);
    Optional<Customer> customer = customerDao.getOne(customers.get(0).getCustomerId());
    System.out.println("\n*** GET ONE ***\n" + customer.get());
    Customer newCustomer = new Customer();
    newCustomer.setFirstName("Foo");
    newCustomer.setLastName("Bar");
    newCustomer.setEmail(System.currentTimeMillis() + "@example.com");
    newCustomer.setPhone("1234567890");
    newCustomer.setAddress("123 Hello Way");
    newCustomer = customerDao.create(newCustomer);
    System.out.println("\n*** CREATE ***\n" + newCustomer);
    newCustomer.setLastName("Baz");
    newCustomer = customerDao.update(newCustomer);
    System.out.println("\n*** UPDATE ***\n" + newCustomer);
    customerDao.delete(newCustomer.getCustomerId());
    System.out.println("\n*** DELETE ***\n");

    ProductVendorDao productVendorDao = new ProductVendorDao();
    List<Product> products = productVendorDao.getAll();
    System.out.println("**** PRODUCTS ****");
    System.out.println("\n*** GET_ALL ***");
    products.forEach(System.out::println);
    Optional<Product> product = productVendorDao.getOne(products.get(0).getProductId());
    System.out.println("\n*** GET ONE ***\n" + product.get());
    Vendor newVendor = new Vendor();
    newVendor.setName("TestVendor");
    newVendor.setContact("TestContact");
    newVendor.setEmail("vendor" + System.currentTimeMillis() + "@example.com");
    newVendor.setPhone("000-000-0000");
    newVendor.setAddress("123 Vendor St");
    Product newProduct = new Product();
    newProduct.setName("TestProduct_" + System.currentTimeMillis());
    newProduct.setPrice(new BigDecimal("19.99"));
    newProduct.setVendor(product.get().getVendor());
    Product created = productVendorDao.create(newProduct);
    System.out.println("\n*** CREATE ***\n" + created);
    created.setPrice(new BigDecimal("29.99"));
    created.getVendor().setName("UpdatedVendorName");
    Product updated = productVendorDao.update(created);
    System.out.println("\n*** UPDATE ***\n" + updated);
    productVendorDao.delete(updated.getProductId());
    System.out.println("\n*** DELETE ***\n");

  }
}
