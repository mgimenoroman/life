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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.mgr.life.item.ItemController.END_POINT;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    }

    @Test
    public void itemGetIntegrationTest() {

        List<Item> data = new ArrayList<>();

        for (int x = 0; x < 2; x++) {
            data.add(new Item((long) x + 1, "Item " + x, "Type " + x, new BigDecimal(1000 * (x + 1))));
        }

        itemRepository.saveAll(data);

        ResponseEntity<List<Item>> response = template.exchange(base.toString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Item>>() {
                });
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), hasSize(2));

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

    @Test
    public void itemPostIntegrationTest() {

        ResponseEntity<Item> response = template.postForEntity(base.toString(),
                new Item("Test Item", "Test Type", new BigDecimal(3000.50)),
                Item.class);

        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), equalTo(1L));
        assertThat(response.getBody().getName(), equalTo("Test Item"));
        assertThat(response.getBody().getType(), equalTo("Test Type"));
        assertThat(response.getBody().getPrice(), equalTo(new BigDecimal(3000.50)));
    }
}