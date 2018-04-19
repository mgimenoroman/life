package com.mgr.life.client;

import com.mgr.life.CRUDController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mgr.life.client.ClientController.END_POINT;

@RestController()
@RequestMapping(path = END_POINT)
@Api(value = "client", description = "Actions regarding clients.")
public class ClientController extends CRUDController<Client> {

    static final String END_POINT = "/client";

    @Autowired
    private ClientRepository clientRepository;

    @Override
    protected CrudRepository<Client, Long> repository() {
        return clientRepository;
    }

    @Override
    protected String endPoint() {
        return END_POINT;
    }
}