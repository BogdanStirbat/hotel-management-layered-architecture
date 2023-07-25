package com.bstirbat.hotelmanagement.layeredarchitecture;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class HotelManagementApplicationTests {

	@Container
	@ServiceConnection
	static final MySQLContainer testSqlContainer = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("hotelmanagement")
			.withUsername("app_user")
			.withPassword("app_password");

	@Test
	void contextLoads() {
	}

}
