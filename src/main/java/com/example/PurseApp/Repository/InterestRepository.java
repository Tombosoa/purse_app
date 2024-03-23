package com.example.PurseApp.Repository;

import jakarta.el.PropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InterestRepository implements CrudOperation{
    @Override
    public List findAll() {
        return null;
    }

    @Override
    public List saveAll(List toSave) {
        return null;
    }

    @Override
    public Object save(Object toSave) {
        return null;
    }

    @Override
    public Object update(Object toUpdate) {
        return null;
    }

    @Override
    public Object delete(Object toDelete) {
        return null;
    }

    @Override
    public Object getOne(Object one) throws PropertyNotFoundException {
        return null;
    }
}
