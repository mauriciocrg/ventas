package com.ventas.crud;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractDataProvider;
import com.vaadin.data.provider.Query;
import com.ventas.backend.DataService;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManageUsuario;

public class UsuarioDataProvider extends AbstractDataProvider<Usuario, String> {

	private String filterText = "";
	
	private ManageUsuario manageUsuario = new ManageUsuario();
	
	
	public void save(Usuario usuario) {
        boolean newUsuario = usuario.getId() == 0;
        
        if (newUsuario) {
        	manageUsuario.saveUsuario(usuario);
            refreshAll();
        } else {
        	manageUsuario.updateUsuario(usuario);
            refreshItem(usuario);
        }
    }

    /**
     * Delete given product from the backing data service.
     * 
     * @param product
     *            the product to be deleted
     */
    public void delete(Usuario usuario) {
    	
    	/*TODO chequear que no tenga compras
    	 * 
    	 * */
    	manageUsuario.deleteUsuario(usuario.getId());
        refreshAll();
    }
	
	@Override
	public boolean isInMemory() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "El filtro no puede ser vacio");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim();
        
        refreshAll();
    }

	@Override
	public int size(Query<Usuario, String> query) {
		// TODO Auto-generated method stub
		return (int) fetch(query).count();
	}

	@Override
	public Stream<Usuario> fetch(Query<Usuario, String> query) {
		 if (filterText.isEmpty()) {
	            return manageUsuario.listUsuario().stream();
	        }
	     return manageUsuario.listUsuario().stream().filter(
	                usuario -> usuario.getNombre().toLowerCase().contains(filterText.toLowerCase())
	                        || usuario.getDireccion().toLowerCase().contains(filterText.toLowerCase())
	                        || usuario.getEmail().toLowerCase().contains(filterText.toLowerCase())
	    		 			|| (usuario.getId()+"").equals(filterText));
	}
	
	private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }

}
