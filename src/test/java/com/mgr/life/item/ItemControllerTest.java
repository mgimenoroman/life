package com.mgr.life.item;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mgr.life.item.ItemController.END_POINT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {

    private MockMvc mvc;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void itemGetTest() throws Exception {

        List<Item> data = new ArrayList<>();

        for (int x = 0; x < 2; x++) {
            data.add(new Item((long) x + 1, "Item " + x, "Type " + x, new BigDecimal(1000.99 * (x + 1))));
        }

        when(itemRepository.findAll()).thenReturn(data);

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .get(END_POINT)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));

        for (int i = 0; i < 2; i++) {
            resultActions
                    .andExpect(jsonPath("$[" + i + "].id", is(i + 1)))
                    .andExpect(jsonPath("$[" + i + "].name", is("Item " + i)))
                    .andExpect(jsonPath("$[" + i + "].type", is("Type " + i)))
                    .andExpect(jsonPath("$[" + i + "].price", is(new BigDecimal(1000.99 * (i + 1)))));
        }
    }

    @Test
    public void itemPostTest() throws Exception {

        when(itemRepository.save(Mockito.any()))
                .thenReturn(new Item((long) 1, "Test Item", "Test Type", new BigDecimal(3000.50)));

        mvc.perform(
                MockMvcRequestBuilders
                        .post(END_POINT)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"name\":\"Test Item\",\"type\":\"Test Type\",\"price\":3000.50}")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"Test Item\",\"type\":\"Test Type\",\"price\":3000.50}"));
    }
}
