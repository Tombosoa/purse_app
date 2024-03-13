package com.example.PurseApp.Reflect;

import java.util.List;

public interface AutoCrudOperation<T> extends CrudOperation<T> {
    @Override
    List<T> findAll();
}
