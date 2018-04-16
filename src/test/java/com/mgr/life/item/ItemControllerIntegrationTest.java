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
import java.util.ArrayList;
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

    @Autowired
    private ItemRepository itemRepository;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + END_POINT);

        List<Item> data = new ArrayList<>();

        for (int x = 0; x < 2; x++) {
            data.add(new Item((long) x + 1, "Item " + x, "Type " + x, new BigDecimal(1000 * (x + 1))));
        }

        itemRepository.saveAll(data);
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
            // Add .00 expected value to match database stored value that is returned with .00 always.
            assertThat(response.getBody().get(i).getPrice(), equalTo(
                    new BigDecimal(1000 * (i + 1)).setScale(2, BigDecimal.ROUND_HALF_EVEN))
            );
        }
    }
}
