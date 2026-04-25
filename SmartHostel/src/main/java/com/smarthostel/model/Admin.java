package com.smarthostel.model;

/*
 * Same logic as original model.Admin:
 *  - hardcoded username "admin" / password "123"
 *  - login(u, p) returns true only on exact match.
 *
 * Not a JPA entity because the original Admin class never persisted.
 */
public class Admin {

    private String username = "admin";
    private String password = "123";

    public boolean login(String u, String p) {
        return username.equals(u) && password.equals(p);
    }
}
