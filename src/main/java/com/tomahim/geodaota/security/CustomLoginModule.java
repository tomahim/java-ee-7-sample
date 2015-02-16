package com.tomahim.geodaota.security;

import java.security.Principal;

import javax.security.auth.login.LoginException;

import org.jboss.security.auth.spi.DatabaseServerLoginModule;
import org.jboss.security.auth.spi.UsersRolesLoginModule;

public class CustomLoginModule extends DatabaseServerLoginModule {
    private CustomPrincipal principal;
 
    @Override
    public boolean login() throws LoginException {
        boolean login = super.login();
        if (login) {
            principal = new CustomPrincipal(getUsername());
        }
        return login;
    }
 
    @Override
    protected Principal getIdentity() {
        return principal != null ? principal : super.getIdentity();
    }
}