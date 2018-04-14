package com.mgr.life.plan;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static com.mgr.life.plan.PlanController.END_POINT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlanControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + END_POINT);
    }

    @Test
    public void planGetTest() {
        ResponseEntity<Plan> response = template.getForEntity(base.toString(),
                Plan.class);
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getName(), equalTo("TestName"));
    }

    @Test
    public void planPostTest() {
        HttpEntity<Plan> entity = new HttpEntity<>(new Plan("TestName"));
        ResponseEntity<String> response = template.postForEntity(base.toString(), entity,
                String.class);
        assertThat(response.getBody(), equalTo("Name is TestName"));
    }
}
