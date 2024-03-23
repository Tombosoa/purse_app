package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.Client;
import com.example.PurseApp.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/new")
    public Client newClient(@RequestBody Client toSave){
        return clientRepository.save(toSave);
    }

    @PutMapping("/update")
    public Client updateClient(@RequestBody Client toUpdate){
        return clientRepository.update(toUpdate);
    }

    @DeleteMapping("/delete")
    public Client deleteClient(@RequestBody Client toDelete){
        return  clientRepository.delete(toDelete);
    }

    @GetMapping("/{id}")
    public Client getOne(@PathVariable String id){
        return clientRepository.getOne(id);
    }
}
