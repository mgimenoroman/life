package com.mgr.life.item;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mgr.life.item.ItemController.END_POINT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController()
@RequestMapping(path = END_POINT)
@Api(value = "item", description = "Actions regarding items.")
public class ItemController {

    static final String END_POINT = "/item";

    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(method = GET)
    @ApiOperation(value = "Find all Items", response = Item.class, responseContainer = "List")
    public Iterable<Item> itemGet() {
        return itemRepository.findAll();
    }

    @RequestMapping(method = POST)
    @ApiOperation(value = "Save or update an Item", response = Item.class)
    public ResponseEntity<Item> itemPost(@RequestBody Item item) {
        return new ResponseEntity<>(itemRepository.save(item), CREATED);
    }

    @RequestMapping(method = PUT)
    @ApiOperation(value = "Update an Item", response = Item.class)
    public Item itemPut(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @RequestMapping(path = "/{id}", method = DELETE)
    @ApiOperation(value = "Delete an Item")
    public ResponseEntity itemDelete(@PathVariable Long id) {
        itemRepository.deleteById(id);
        return new ResponseEntity(NO_CONTENT);
    }
}