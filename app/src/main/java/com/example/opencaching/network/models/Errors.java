package com.example.opencaching.network.models;

import java.util.ArrayList;

/**
 * Created by Volfram on 22.10.2017.
 */

public class Errors {
    private ArrayList<Error> errors = new ArrayList<>();

    public ArrayList<Error> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<Error> errors) {
        this.errors = errors;
    }
}
