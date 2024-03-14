package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.Client;
import com.example.PurseApp.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("client")
@RestController
public class ClientController {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/all")
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }
}
