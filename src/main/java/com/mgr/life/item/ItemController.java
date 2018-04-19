package com.mgr.life.item;

import com.mgr.life.CRUDController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mgr.life.item.ItemController.END_POINT;

@RestController()
@RequestMapping(path = END_POINT)
@Api(value = "item", description = "Actions regarding items.")
public class ItemController extends CRUDController<Item> {

    static final String END_POINT = "/item";

    @Autowired
    private ItemRepository itemRepository;

    @Override
    protected CrudRepository<Item, Long> repository() {
        return itemRepository;
    }

    @Override
    protected String endPoint() {
        return END_POINT;
    }
}