package com.example.PurseApp.Reflect;

import jakarta.el.PropertyNotFoundException;

import java.util.List;

public interface CrudOperation<T> {
    public List<T> findAll();
    public List<T> saveAll(List<T> toSave);
    public T save(T toSave);
    public T update(T toUpdate);
    T delete(T toDelete);
    T getOne(T one) throws PropertyNotFoundException;

}
