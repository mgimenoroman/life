package com.mgr.life;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public abstract class CRUDController<T extends RestEntity> {

    protected abstract CrudRepository<T, Long> repository();

    protected abstract String endPoint();

    @RequestMapping(method = GET)
    public ResponseEntity<Iterable<T>> get() {
        return ResponseEntity.ok().body(repository().findAll());
    }

    @RequestMapping(path = "{id}", method = GET)
    public ResponseEntity<Optional<T>> get(@PathVariable Long id) {
        return ResponseEntity.ok().body(repository().findById(id));
    }

    @RequestMapping(method = POST)
    public ResponseEntity<T> post(@RequestBody T entity) throws URISyntaxException {

        // FIXME: Remove id property from Swagger Client model when post

        // Remove id if it's set to prevent update
        entity.setId(null);

        entity = repository().save(entity);

        return ResponseEntity.created(new URI(endPoint() + "/" + entity.getId())).body(entity);
    }

    @RequestMapping(method = PUT)
    public ResponseEntity<T> put(@RequestBody T client) {

        // Return BAD_REQUEST http status if given Item has no id (not possible to update)
        if (client.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(repository().save(client));
    }

    @RequestMapping(path = "/{id}", method = DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        repository().deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
