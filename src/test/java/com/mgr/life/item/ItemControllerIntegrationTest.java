package com.mgr.life.item;

import com.mgr.life.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

import static com.mgr.life.item.ItemController.END_POINT;

public class ItemControllerIntegrationTest extends IntegrationTest<Item> {

    @Autowired
    private ItemRepository itemRepository;

    public ItemControllerIntegrationTest() {
        super(Item.class);
    }

    @Override
    protected String endPoint() {
        return END_POINT;
    }

    @Override
    protected CrudRepository<Item, Long> repository() {
        return itemRepository;
    }

    @Override
    protected Item newRestEntity() {
        return new Item("Item Name", "Item Type", new BigDecimal(5000));
    }

    @Override
    protected Item modifyRestEntity(Item toModify) {
        return toModify.setName("Test Item modified").setType("Test Type modified").setPrice(new BigDecimal(3000));
    }
}
