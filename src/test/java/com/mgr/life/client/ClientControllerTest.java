package com.mgr.life.client;

import com.mgr.life.UnitTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.mgr.life.client.ClientController.END_POINT;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ClientControllerTest extends UnitTest<Client> {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientController clientController;

    @Override
    protected Object[] controllers() {
        return new Object[]{clientController};
    }

    @Override
    protected CrudRepository<Client, Long> repository() {
        return clientRepository;
    }

    @Override
    public String endPoint() {
        return END_POINT;
    }

    @Override
    protected Client newRestEntityWithId() {
        return new Client(1L, "Client Name 1", "Client Surname 1",
                "test1@email.com", "Client Street Name 1", "ABC123 1",
                "Client City 1", "Client Country 1", 1234, 10, 666555666L);
    }

    @Override
    protected Client modifiedRestEntityWithId() {
        return new Client(1L, "Client Name Modified 1", "Client Surname Modified 1",
                "testmodified1@email.com", "Client Street Name Modified 1", "DEF456 1",
                "Client City Modified 1", "Client Country Modified 1", 5678, 20, 777888777L);
    }

    @Override
    protected String newRestEntityJsonWithId() {
        return "{\"id\":1,\"name\":\"Client Name 1\",\"surname\":\"Client Surname 1\"," +
                "\"email\":\"test1@email.com\",\"streetName\":\"Client Street Name 1\"," +
                "\"streetNumber\":\"ABC123 1\",\"city\":\"Client City 1\"," +
                "\"country\":\"Client Country 1\",\"zipCode\":1234,\"phonePrefix\":10," +
                "\"phone\":666555666}";
    }

    @Override
    protected String newRestEntityJson() {
        return "{\"name\":\"Client Name 1\",\"surname\":\"Client Surname 1\"," +
                "\"email\":\"test1@email.com\",\"streetName\":\"Client Street Name 1\"," +
                "\"streetNumber\":\"ABC123 1\",\"city\":\"Client City 1\"," +
                "\"country\":\"Client Country 1\",\"zipCode\":1234,\"phonePrefix\":10," +
                "\"phone\":666555666}";
    }

    @Override
    protected String modifiedRestEntityJsonWithId() {
        return "{\"id\":1,\"name\":\"Client Name Modified 1\",\"surname\":\"Client Surname Modified 1\"," +
                "\"email\":\"testmodified1@email.com\",\"streetName\":\"Client Street Name Modified 1\"," +
                "\"streetNumber\":\"DEF456 1\",\"city\":\"Client City Modified 1\"," +
                "\"country\":\"Client Country Modified 1\",\"zipCode\":5678,\"phonePrefix\":20," +
                "\"phone\":777888777}";
    }

    @Override
    protected String modifiedRestEntityJson() {
        return "{\"name\":\"Client Name Modified 1\",\"surname\":\"Client Surname Modified 1\"," +
                "\"email\":\"testmodified1@email.com\",\"streetName\":\"Client Street Name Modified 1\"," +
                "\"streetNumber\":\"DEF456 1\",\"city\":\"Client City Modified 1\"," +
                "\"country\":\"Client Country Modified 1\",\"zipCode\":5678,\"phonePrefix\":20," +
                "\"phone\":777888777}";
    }

    @Override
    protected void assertRestEntitiesProperties(List<Client> savedRestEntities, ResultActions resultActions)
            throws Exception {

        for (int i = 0; i < savedRestEntities.size(); i++) {

            assertRestEntityId(i, savedRestEntities, resultActions);

            resultActions
                    .andExpect(jsonPath("$[" + i + "].name", is(savedRestEntities.get(i).getName())))
                    .andExpect(jsonPath("$[" + i + "].surname", is(savedRestEntities.get(i).getSurname())))
                    .andExpect(jsonPath("$[" + i + "].email", is(savedRestEntities.get(i).getEmail())))
                    .andExpect(jsonPath("$[" + i + "].streetName", is(savedRestEntities.get(i).getStreetName())))
                    .andExpect(jsonPath("$[" + i + "].streetNumber", is(savedRestEntities.get(i).getStreetNumber())))
                    .andExpect(jsonPath("$[" + i + "].city", is(savedRestEntities.get(i).getCity())))
                    .andExpect(jsonPath("$[" + i + "].country", is(savedRestEntities.get(i).getCountry())))
                    .andExpect(jsonPath("$[" + i + "].zipCode", is(savedRestEntities.get(i).getZipCode())))
                    .andExpect(jsonPath("$[" + i + "].phonePrefix", is(savedRestEntities.get(i).getPhonePrefix())));

            assertLongField(resultActions, "$[" + i + "].phone", savedRestEntities.get(i).getPhone());
        }
    }
}
