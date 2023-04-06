package com.ventas.authentication;

import com.ventas.backend.core.Cripto;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManageUsuario;

/**
 * Default mock implementation of {@link AccessControl}. This implementation
 * accepts any string as a password, and considers the user "admin" as the only
 * administrator.
 */
public class BasicAccessControl implements AccessControl {

	private ManageUsuario manageUsusario = new ManageUsuario();
	
    public boolean signIn(String username, String password) {
        if (username == null || username.isEmpty())
            return false;
        else {
        	Usuario usuario = manageUsusario.getUsuario(username);
        	if(usuario != null && password.equals(Cripto.getInstance().Desencriptar(usuario.getPassword()))) {
    			CurrentUser.set(usuario);
    	        return true;
        	}
        }
        return false;
    }

    public boolean isUserSignedIn() {
        return !(CurrentUser.get() == null);
    }

    public boolean isUserInRole(String role) {
        if ("admin".equals(role)) {
            // Only the "admin" user is in the "admin" role
            return CurrentUser.get().getAdmin() == 1;
        }

        return false;
    }

    public String getPrincipalName() {
        return CurrentUser.get().getNombre();
    }

	@Override
	public Usuario getCurrentUser() {
		return CurrentUser.get();
	}

}
