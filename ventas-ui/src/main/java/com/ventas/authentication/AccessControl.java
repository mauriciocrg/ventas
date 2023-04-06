package com.ventas.authentication;

import java.io.Serializable;

import com.ventas.backend.data.Usuario;

/**
 * Simple interface for authentication and authorization checks.
 */
public interface AccessControl extends Serializable {

    public boolean signIn(String username, String password);

    public boolean isUserSignedIn();

    public boolean isUserInRole(String role);

    public String getPrincipalName();
    
    public Usuario getCurrentUser();
}
