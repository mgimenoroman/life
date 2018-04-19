package com.mgr.life;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class IntegrationTest<T extends RestEntity> {

    private final Class<T> classType;

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    public IntegrationTest(Class<T> classType) {
        this.classType = classType;
    }

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + endPoint());
    }

    protected abstract String endPoint();

    protected abstract CrudRepository<T, Long> repository();

    protected abstract T newRestEntity();

    protected abstract T modifyRestEntity(T toModify);

    @Test
    public void getAllIntegrationTest() {

        List<T> savedRestEntities = new ArrayList<>();

        for (int x = 0; x < 2; x++) {

            T restEntity = newRestEntity();

            restEntity.setId((long) x + 1);

            savedRestEntities.add(restEntity);
        }

        repository().saveAll(savedRestEntities);

        ResponseEntity<List<T>> response = template.exchange(base.toString(), GET, null,
                new ParameterizedTypeReference<List<T>>() {
                });

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), hasSize(2));

        for (int i = 0; i < 2; i++) {

            T restEntity = response.getBody().get(i);

            assertThat(restEntity.getId(), equalTo(savedRestEntities.get(i).getId()));
            assertThat(restEntity, equalTo(savedRestEntities.get(i)));
        }
    }

    @Test
    public void getOneIntegrationTest() {

        T savedRestEntity = repository().save(newRestEntity());

        ResponseEntity<T> response = template.exchange(base.toString() + "/{id}", GET, null,
                classType, savedRestEntity.getId());

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), notNullValue());

        assertThat(response.getBody().getId(), equalTo(savedRestEntity.getId()));
        assertThat(response.getBody(), equalTo(savedRestEntity));
    }

    @Test
    public void postSaveIntegrationTest() {

        T toSave = newRestEntity();

        ResponseEntity<T> response = template.postForEntity(base.toString(), toSave, classType);

        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody(), notNullValue());

        assertThat(response.getBody().getId(), equalTo(1L));
        assertThat(response.getBody(), equalTo(toSave));
    }

    @Test
    public void postDoesNotUpdateIntegrationTest() {

        T toUpdate = repository().save(newRestEntity());


        toUpdate = modifyRestEntity(toUpdate);

        ResponseEntity<T> response = template.postForEntity(base.toString(), toUpdate, classType);

        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), equalTo(2L));
        assertThat(response.getBody(), equalTo(toUpdate));
    }

    @Test
    public void putIntegrationTest() {

        T toUpdate = repository().save(newRestEntity());

        toUpdate = modifyRestEntity(toUpdate);

        ResponseEntity<T> response = template.exchange(base.toString(), PUT, new HttpEntity<>(toUpdate), classType);

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), equalTo(toUpdate.getId()));
        assertThat(response.getBody(), equalTo(toUpdate));
    }

    @Test
    public void putNoIdIntegrationTest() {

        T toUpdate = repository().save(newRestEntity());

        toUpdate = modifyRestEntity(toUpdate);
        toUpdate.setId(null);

        ResponseEntity<T> response = template.exchange(base.toString(), PUT, new HttpEntity<>(toUpdate), classType);

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        assertThat(response.getBody(), nullValue());
    }

    @Test
    public void deleteIntegrationTest() {

        T toDelete = repository().save(newRestEntity());

        ResponseEntity response = template.exchange(base.toString() + "/{id}", DELETE, null,
                Void.class, 1);

        assertThat(response.getStatusCode(), is(NO_CONTENT));
        assertThat(response.getBody(), nullValue());

        Optional<T> deleted = repository().findById(toDelete.getId());
        assertThat(deleted.isPresent(), is(false));
    }
}
