package com.mgr.life.item;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mgr.life.item.ItemController.END_POINT;
import static java.math.BigDecimal.ROUND_HALF_EVEN;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

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
    public void itemGetAllIntegrationTest() {

        List<Item> savedItems = new ArrayList<>();

        for (int x = 0; x < 2; x++) {
            savedItems.add(new Item("Item " + x, "Type " + x, new BigDecimal(1000 * (x + 1))));
        }

        itemRepository.saveAll(savedItems);

        ResponseEntity<List<Item>> response = template.exchange(base.toString(), GET, null,
                new ParameterizedTypeReference<List<Item>>() {
                });

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), hasSize(2));

        for (int i = 0; i < 2; i++) {
            assertThat(response.getBody().get(i).getId(), equalTo(savedItems.get(i).getId()));
            assertThat(response.getBody().get(i).getName(), equalTo(savedItems.get(i).getName()));
            assertThat(response.getBody().get(i).getType(), equalTo(savedItems.get(i).getType()));
            // Add .00 to expected value to match database stored value that is returned with .00.
            assertThat(response.getBody().get(i).getPrice(), equalTo(savedItems.get(i).getPrice()
                    .setScale(2, ROUND_HALF_EVEN))
            );
        }
    }

    @Test
    public void itemGetOneIntegrationTest() {

        Item savedItem = itemRepository.save(new Item("Item 1", "Type 1", new BigDecimal(1000)));

        ResponseEntity<Item> response = template.exchange(base.toString() + "/{id}", GET, null,
                Item.class, savedItem.getId());

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), notNullValue());

        assertThat(response.getBody().getId(), equalTo(savedItem.getId()));
        assertThat(response.getBody().getName(), equalTo(savedItem.getName()));
        assertThat(response.getBody().getType(), equalTo(savedItem.getType()));
        // Add .00 to expected value to match database stored value that is returned with .00.
        assertThat(response.getBody().getPrice(), equalTo(savedItem.getPrice()
                .setScale(2, ROUND_HALF_EVEN))
        );
    }

    @Test
    public void itemPostSaveIntegrationTest() {

        Item toSave = new Item("Test Item", "Test Type", new BigDecimal(3000.50));

        ResponseEntity<Item> response = template.postForEntity(base.toString(), toSave, Item.class);

        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), equalTo(1L));
        assertThat(response.getBody().getName(), equalTo(toSave.getName()));
        assertThat(response.getBody().getType(), equalTo(toSave.getType()));
        assertThat(response.getBody().getPrice(), equalTo(toSave.getPrice()));
    }

    @Test
    public void itemPostDoesNotUpdateIntegrationTest() {

        Item toUpdate = itemRepository.save(new Item("Test Item", "Test Type", new BigDecimal(3000.50)));

        toUpdate.setName("Test Item modified").setType("Test Type modified").setPrice(new BigDecimal(5000));

        ResponseEntity<Item> response = template.postForEntity(base.toString(), toUpdate, Item.class);

        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), equalTo(2L));
        assertThat(response.getBody().getName(), equalTo(toUpdate.getName()));
        assertThat(response.getBody().getType(), equalTo(toUpdate.getType()));
        assertThat(response.getBody().getPrice(), equalTo(toUpdate.getPrice()));
    }

    @Test
    public void itemPutIntegrationTest() {

        Item toUpdate = itemRepository.save(new Item("Test Item", "Test Type", new BigDecimal(3000.50)));

        toUpdate.setName("Test Item modified").setType("Test Type modified").setPrice(new BigDecimal(5000));

        ResponseEntity<Item> response = template.exchange(base.toString(), PUT, new HttpEntity<>(toUpdate), Item.class);

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), equalTo(toUpdate.getId()));
        assertThat(response.getBody().getName(), equalTo(toUpdate.getName()));
        assertThat(response.getBody().getType(), equalTo(toUpdate.getType()));
        assertThat(response.getBody().getPrice(), equalTo(toUpdate.getPrice()));
    }

    @Test
    public void itemDeleteIntegrationTest() {

        Item toDelete = itemRepository.save(new Item("Test Item", "Test Type", new BigDecimal(3000.50)));

        ResponseEntity response = template.exchange(base.toString() + "/{id}", DELETE, null, Void.class, 1);

        assertThat(response.getStatusCode(), is(NO_CONTENT));
        assertThat(response.getBody(), nullValue());

        Optional<Item> deleted = itemRepository.findById(toDelete.getId());
        assertThat(deleted.isPresent(), is(false));
    }
}
