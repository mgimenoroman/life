package com.mgr.life.item;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Iterable<Item> itemGet() {
        return itemRepository.findAll();
    }
}