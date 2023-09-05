package com.bstirbat.hotelmanagement.layeredarchitecture;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/delete-all-db-records.sql",
    "classpath:/sql/create-default-users.sql"
})
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/delete-all-db-records.sql"
})
public abstract class AbstractIntegrationTest {

  private static final String IMAGE_NAME = "mysql:8.0";
  private static final String DATABASE_NAME = "hotelmanagement";
  private static final String USERNAME = "app_user";
  private static final String PASSWORD = "app_password";
  private static final String JDBC_URL_PARAMS = "?allowPublicKeyRetrieval=True&useSSL=false";

  public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse(IMAGE_NAME))
      .withUsername(USERNAME)
      .withPassword(PASSWORD)
      .withDatabaseName(DATABASE_NAME)
      .withReuse(true);

  @DynamicPropertySource
  public static void setUp(DynamicPropertyRegistry registry) {
    mysqlContainer.start();

    registry.add("spring.datasource.url", () -> mysqlContainer.getJdbcUrl() + JDBC_URL_PARAMS);
    registry.add("spring.datasource.username", () -> mysqlContainer.getUsername());
    registry.add("spring.datasource.password", () -> mysqlContainer.getPassword());
    registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
    registry.add("spring.sql.init.mode", () -> "always");
  }
}
