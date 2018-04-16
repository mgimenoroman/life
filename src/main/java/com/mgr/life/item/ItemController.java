package com.mgr.life.item;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

        List<Item> data = new ArrayList<>();

        for (int x = 0; x < 2; x++) {
            data.add(new Item("Item " + x, "Type " + x, new BigDecimal(1000 * (x + 1))));
        }

        itemRepository.saveAll(data);

        return itemRepository.findAll();
    }
}
