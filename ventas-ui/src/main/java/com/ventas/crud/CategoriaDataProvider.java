package com.ventas.crud;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractDataProvider;
import com.vaadin.data.provider.Query;
import com.ventas.backend.DataService;
import com.ventas.backend.data.Categoria;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManageCategoria;
import com.ventas.backend.manageData.ManageUsuario;

public class CategoriaDataProvider extends AbstractDataProvider<Categoria, String> {

	private String filterText = "";
	
	private ManageCategoria manageCategoria = new ManageCategoria();
	
	
	public void save(Categoria categoria) {
        boolean newCategoria = categoria.getId() == 0;
        
        if (newCategoria) {
        	manageCategoria.saveCategoria(categoria);
            refreshAll();
        } else {
        	manageCategoria.updateCategoria(categoria);
            refreshItem(categoria);
        }
    }

    /**
     * Delete given product from the backing data service.
     * 
     * @param product
     *            the product to be deleted
     */
    public void delete(Categoria categoria) {
    	
    	/*TODO chequear que no tenga compras
    	 * 
    	 * */
    	manageCategoria.deleteCategoria(categoria.getId());
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
	public int size(Query<Categoria, String> query) {
		// TODO Auto-generated method stub
		return (int) fetch(query).count();
	}

	@Override
	public Stream<Categoria> fetch(Query<Categoria, String> query) {
		 if (filterText.isEmpty()) {
	            return manageCategoria.listCategoria().stream();
	        }
	     return manageCategoria.listCategoria().stream().filter(
	                categoria -> categoria.getNombre().toLowerCase().contains(filterText.toLowerCase())
	    		 			|| (categoria.getId()+"").equals(filterText));
	}
	
	private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }

}
