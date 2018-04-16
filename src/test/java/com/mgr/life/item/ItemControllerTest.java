package com.mgr.life.item;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.mgr.life.item.ItemController.END_POINT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void itemGetTest() throws Exception {
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
                    .andExpect(jsonPath("$[" + i + "].price", is(1000 * (i + 1))));
        }
    }
}
