package com.mgr.life.item;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.mgr.life.item.ItemController.END_POINT;

@RestController()
@RequestMapping(path = END_POINT)
@Api(value = "item", description = "Actions regarding items.")
public class ItemController {

    static final String END_POINT = "/item";

    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Find all Items", response = Item.class, responseContainer = "List")
    public Iterable<Item> itemGet() {
        return itemRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Save or update an Item", response = Item.class)
    public Item itemPost(@RequestBody Item item) {
        return itemRepository.save(item);
    }
}