package com.gusar.datingapp.interfaces;


import com.gusar.datingapp.model.ModelPerson;
import java.util.List;

public interface MyPersonsCallback {
    void onResult(List<ModelPerson> persons);
}
