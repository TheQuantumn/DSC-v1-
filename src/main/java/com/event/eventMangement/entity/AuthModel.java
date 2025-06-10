package com.event.eventMangement.entity;

import lombok.Data;
import lombok.Setter;

@Data
public class AuthModel {
    @Setter
    private String email;
    private String pass ;

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String password) {
        this.pass = password;
    }
}
