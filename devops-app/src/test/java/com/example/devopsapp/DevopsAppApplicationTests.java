package com.example.devopsapp;

import com.example.devopsapp.service.AppService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DevopsAppApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private AppService appService;

	@Test
	void contextLoads() {
		assertThat(appService).isNotNull();
	}

	@Test
	void healthEndpointReturnsUp() {
		String body = this.restTemplate.getForObject("http://localhost:" + port + "/health", String.class);
		assertThat(body).contains("UP");
	}

	@Test
	void helloEndpointReturnsGreeting() {
		String body = this.restTemplate.getForObject("http://localhost:" + port + "/hello?name=Test", String.class);
		assertThat(body).isEqualTo("Hello, Test!");
	}
}
