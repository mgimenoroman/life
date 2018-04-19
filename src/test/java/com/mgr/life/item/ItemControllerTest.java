package com.mgr.life.item;

import com.mgr.life.UnitTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static com.mgr.life.item.ItemController.END_POINT;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ItemControllerTest extends UnitTest<Item> {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @Override
    protected Object[] controllers() {
        return new Object[]{itemController};
    }

    @Override
    protected CrudRepository<Item, Long> repository() {
        return itemRepository;
    }

    @Override
    public String endPoint() {
        return END_POINT;
    }

    @Override
    protected Item newRestEntityWithId() {
        return new Item(1L, "Test Item", "Test Type", new BigDecimal(3000.50));
    }

    @Override
    protected Item modifiedRestEntityWithId() {
        return new Item(1L, "Test Item Modified", "Test Type Modified", new BigDecimal(5000));
    }

    @Override
    protected String newRestEntityJsonWithId() {
        return "{\"id\":1,\"name\":\"Test Item\",\"type\":\"Test Type\",\"price\":3000.50}";
    }

    @Override
    protected String newRestEntityJson() {
        return "{\"name\":\"Test Item\",\"type\":\"Test Type\",\"price\":3000.50}";
    }

    @Override
    protected String modifiedRestEntityJsonWithId() {
        return "{\"id\":1,\"name\":\"Test Item Modified\",\"type\":\"Test Type Modified\",\"price\":5000}";
    }

    @Override
    protected void assertRestEntitiesProperties(List<Item> savedRestEntities, ResultActions resultActions)
            throws Exception {
        for (int i = 0; i < savedRestEntities.size(); i++) {

            assertRestEntityId(i, savedRestEntities, resultActions);

            resultActions
                    .andExpect(jsonPath("$[" + i + "].name", is(savedRestEntities.get(i).getName())))
                    .andExpect(jsonPath("$[" + i + "].type", is(savedRestEntities.get(i).getType())));

            assertBigDecimalField(resultActions, "$[" + i + "].price", savedRestEntities.get(i).getPrice());
        }
    }
}
