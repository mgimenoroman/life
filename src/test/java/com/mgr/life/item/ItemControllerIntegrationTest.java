package com.mgr.life.item;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

import static com.mgr.life.item.ItemController.END_POINT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerIntegrationTest {

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
        ResponseEntity<List<Item>> response = template.exchange(base.toString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Item>>() {
                });
        assertThat(response.getBody(), notNullValue());

        for (int i = 0; i < 2; i++) {
            assertThat(response.getBody().get(i).getId(), equalTo((long) i + 1));
            assertThat(response.getBody().get(i).getName(), equalTo("Item " + i));
            assertThat(response.getBody().get(i).getType(), equalTo("Type " + i));
            assertThat(response.getBody().get(i).getPrice(), equalTo(new BigDecimal(1000 * (i + 1))));
        }
    }
}
