package com.mgr.life;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class UnitTest<T extends RestEntity> {

    private MockMvc mvc;

    protected abstract Object[] controllers();

    protected abstract CrudRepository<T, Long> repository();

    public abstract String endPoint();

    protected abstract T newRestEntityWithId();

    protected abstract T modifiedRestEntityWithId();

    protected abstract String newRestEntityJsonWithId();

    protected abstract String newRestEntityJson();

    protected abstract String modifiedRestEntityJsonWithId();

    protected abstract void assertRestEntitiesProperties(List<T> savedRestEntities, ResultActions resultActions)
            throws Exception;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controllers()).build();
    }

    @Test
    public void getAllTest() throws Exception {

        List<T> savedRestEntities = new ArrayList<>();

        for (int x = 0; x < 2; x++) {
            T restEntity = newRestEntityWithId();

            restEntity.setId((long) x + 1);

            savedRestEntities.add(restEntity);
        }

        when(repository().findAll()).thenReturn(savedRestEntities);

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .get(endPoint())
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(savedRestEntities.size())));

        assertRestEntitiesProperties(savedRestEntities, resultActions);
    }

    protected void assertRestEntityId(int i, List<T> savedRestEntities, ResultActions resultActions) throws Exception {
        assertLongField(resultActions, "$[" + i + "].id", savedRestEntities.get(i).getId());
    }

    protected void assertLongField(ResultActions resultActions, String jsonPath, Long value) throws Exception {
        // Get intValue of id to avoid comparing 1 vs 1L
        // FIXME: Review this Long shit
        resultActions.andExpect(jsonPath(jsonPath, is(value.intValue())));
    }

    protected void assertBigDecimalField(ResultActions resultActions, String jsonPath, BigDecimal value) throws Exception {
        // Get intValue of id to avoid comparing 1 vs "1"
        // FIXME: Review this BigDecimal shit
        resultActions.andExpect(jsonPath(jsonPath, is(value.doubleValue())));
    }

    @Test
    public void getOneTest() throws Exception {

        when(repository().findById(Mockito.eq(1L))).thenReturn(Optional.of(newRestEntityWithId()));

        mvc.perform(
                MockMvcRequestBuilders
                        .get(endPoint() + "/{id}", 1)
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(newRestEntityJsonWithId()));
    }

    @Test
    public void postTest() throws Exception {

        when(repository().save(Mockito.any())).thenReturn(newRestEntityWithId());
        mvc.perform(
                MockMvcRequestBuilders
                        .post(endPoint())
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(newRestEntityJson())
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endPoint() + "/1"))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(newRestEntityJsonWithId()));
    }

    @Test
    public void putTest() throws Exception {

        when(repository().save(Mockito.any())).thenReturn(modifiedRestEntityWithId());

        mvc.perform(
                MockMvcRequestBuilders
                        .put(endPoint())
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(modifiedRestEntityJsonWithId())
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(modifiedRestEntityJsonWithId()));
    }

    @Test
    public void deleteTest() throws Exception {

        mvc.perform(
                MockMvcRequestBuilders
                        .delete(endPoint() + "/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
