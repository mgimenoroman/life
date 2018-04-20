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

        if (repository().existsById(id)) {
            return ResponseEntity.ok().body(repository().findById(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = POST)
    public ResponseEntity<T> post(@RequestBody T restEntity) throws URISyntaxException {

        // FIXME: Remove id property from Swagger Client model when post

        // Remove id if it's set to prevent update
        restEntity.setId(null);

        restEntity = repository().save(restEntity);

        return ResponseEntity.created(new URI(endPoint() + "/" + restEntity.getId())).body(restEntity);
    }

    @RequestMapping(method = PUT)
    public ResponseEntity<T> put(@RequestBody T restEntity) {

        if (restEntity.getId() == null) {
            // Return BAD_REQUEST http status if given RestEntity has no id
            return ResponseEntity.badRequest().build();
        } else if (!repository().existsById(restEntity.getId())) {
            // Return NOT_FOUND http status if given RestEntity does not exist
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(repository().save(restEntity));
    }

    @RequestMapping(path = "/{id}", method = DELETE)
    public ResponseEntity delete(@PathVariable Long id) {

        repository().deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
