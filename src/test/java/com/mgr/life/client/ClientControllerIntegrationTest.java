package com.mgr.life.client;

import com.mgr.life.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import static com.mgr.life.client.ClientController.END_POINT;

public class ClientControllerIntegrationTest extends IntegrationTest<Client> {

    @Autowired
    private ClientRepository clientRepository;

    public ClientControllerIntegrationTest() {
        super(Client.class);
    }

    @Override
    protected String endPoint() {
        return END_POINT;
    }

    @Override
    protected CrudRepository<Client, Long> repository() {
        return clientRepository;
    }

    @Override
    protected Client newRestEntity() {
        return new Client("Client Name 1", "Client Surname 1",
                "test1@email.com", "Client Street Name 1", "ABC123 1",
                "Client City 1", "Client Country 1", 1234, 10, 666555666L);
    }

    @Override
    protected Client modifyRestEntity(Client toModify) {
        return toModify.setName("Modified name").setSurname("Modified Surname").setEmail("this@email.modified")
                .setCity("ModifiedCity").setCountry("The non existing country").setPhone(777999111L)
                .setPhonePrefix(55).setStreetName("Ohyeahstreet").setStreetNumber("65L").setZipCode(6540);
    }
}
