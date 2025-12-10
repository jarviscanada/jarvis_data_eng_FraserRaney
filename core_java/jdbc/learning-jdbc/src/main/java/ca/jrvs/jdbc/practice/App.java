package ca.jrvs.jdbc.practice;

import ca.jrvs.jdbc.practice.data.dao.CustomerDao;
import ca.jrvs.jdbc.practice.data.dao.ProductVendorDao;
import ca.jrvs.jdbc.practice.data.dao.ServiceDao;
import ca.jrvs.jdbc.practice.data.dao.SimpleProductDao;
import ca.jrvs.jdbc.practice.data.entity.Customer;
import ca.jrvs.jdbc.practice.data.entity.Product;
import ca.jrvs.jdbc.practice.data.entity.Service;
import ca.jrvs.jdbc.practice.data.entity.Vendor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Hello world!
 */
public class App {

  private static final Logger LOGGER = Logger.getLogger(App.class.getName());

  public static void main(String[] args) {

    ServiceDao serviceDao = new ServiceDao();
    List<Service> services = serviceDao.getAll();
    LOGGER.info("**** SERVICES ****");
    LOGGER.info("\n*** GET_ALL ***");
    services.forEach(m -> LOGGER.info(m.toString()));
    Optional<Service> service = serviceDao.getOne(services.get(0).getServiceId());
    LOGGER.info("\n*** GET ONE ***\n" + service.get());
    Service newService = new Service();
    newService.setName("FooBarBaz" + System.currentTimeMillis());
    newService.setPrice(new BigDecimal(4.35));
    newService = serviceDao.create(newService);
    LOGGER.info("\n*** CREATE ***\n" + newService);
    newService.setPrice(new BigDecimal(13.45));
    newService = serviceDao.update(newService);
    LOGGER.info("\n*** UPDATE ***\n" + newService);
    serviceDao.delete(newService.getServiceId());
    LOGGER.info("\n*** DELETE ***\n");

    CustomerDao customerDao = new CustomerDao();
    List<Customer> customers = customerDao.getAll();
    LOGGER.info("**** CUSTOMERS ****");
    LOGGER.info("\n*** GET_ALL ***");
    customers.forEach(m -> LOGGER.info(m.toString()));
    Optional<Customer> customer = customerDao.getOne(customers.get(0).getCustomerId());
    LOGGER.info("\n*** GET ONE ***\n" + customer.get());
    Customer newCustomer = new Customer();
    newCustomer.setFirstName("Foo");
    newCustomer.setLastName("Bar");
    newCustomer.setEmail(System.currentTimeMillis() + "@example.com");
    newCustomer.setPhone("1234567890");
    newCustomer.setAddress("123 Hello Way");
    newCustomer = customerDao.create(newCustomer);
    LOGGER.info("\n*** CREATE ***\n" + newCustomer);
    newCustomer.setLastName("Baz");
    newCustomer = customerDao.update(newCustomer);
    LOGGER.info("\n*** UPDATE ***\n" + newCustomer);
    customerDao.delete(newCustomer.getCustomerId());
    LOGGER.info("\n*** DELETE ***\n");

    ProductVendorDao productVendorDao = new ProductVendorDao();
    List<Product> products = productVendorDao.getAll();
    LOGGER.info("**** PRODUCTS ****");
    LOGGER.info("\n*** GET_ALL ***");
    products.forEach(m -> LOGGER.info(m.toString()));
    Optional<Product> product = productVendorDao.getOne(products.get(0).getProductId());
    LOGGER.info("\n*** GET ONE ***\n" + product.get());
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
    LOGGER.info("\n*** CREATE ***\n" + created);
    created.setPrice(new BigDecimal("29.99"));
    created.getVendor().setName("UpdatedVendorName");
    Product updated = productVendorDao.update(created);
    LOGGER.info("\n*** UPDATE ***\n" + updated);
    productVendorDao.delete(updated.getProductId());
    LOGGER.info("\n*** DELETE ***\n");

    LOGGER.info("\n\n*** SIMPLE PRODUCT ***");
    SimpleProductDao spdao = new SimpleProductDao();
    UUID productId = spdao.createProduct("foobarbaz" + System.currentTimeMillis(),
        new BigDecimal(45.67), "Jaloo");
    LOGGER.info(productId.toString());

    LOGGER.info("\n\n*** LIMIT ***");
    serviceDao.getAllLimit(2).forEach(m -> LOGGER.info(m.toString()));

    LOGGER.info("\n\n*** PAGED ***");
    for (int i = 1; i < 11; i++) {
      LOGGER.info("Page number: " + i);
      customerDao.getAllPaged(i, 10).forEach(m -> LOGGER.info(m.toString()));
    }
  }
}
