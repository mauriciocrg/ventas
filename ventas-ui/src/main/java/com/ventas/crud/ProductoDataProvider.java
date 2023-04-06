package com.ventas.crud;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractDataProvider;
import com.vaadin.data.provider.Query;
import com.ventas.backend.DataService;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Producto;
import com.ventas.backend.manageData.ManageProducto;

public class ProductoDataProvider extends AbstractDataProvider<Producto, String> {

	
	/** Text filter that can be changed separately. */
    private String filterText = "";
    
    private ManageProducto manageProducto = new ManageProducto();

    /**
     * Store given product to the backing data service.
     * 
     * @param product
     *            the updated or new product
     */
    public void save(Producto producto) {
        boolean newProducto = producto.getId() == 0;
        
        manageProducto.saveOrUpdateProducto(producto);
        if (newProducto) {
            refreshAll();
        } else {
            refreshItem(producto);
        }
    }
    
    public void update(Producto producto) {
        manageProducto.updateProducto(producto);
        refreshAll(); 
    }

    /**
     * Delete given product from the backing data service.
     * 
     * @param product
     *            the product to be deleted
     */
    public void delete(Producto producto) {
    	
    	/*TODO chequear que no este en algun pedido
    	 * */
        manageProducto.deleteProducto(producto.getId());
        refreshAll();
    }
    
    /**
     * Sets the filter to use for the this data provider and refreshes data.
     * <p>
     * Filter is compared for product name, availability and category.
     * 
     * @param filterText
     *           the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "El filtro no puede ser vacio");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim();
        
        refreshAll();
    }
	
	@Override
	public boolean isInMemory() {
		return true;
	}

	@Override
	public int size(Query<Producto, String> query) {
		return (int) fetch(query).count();
	}

	@Override
	public Stream<Producto> fetch(Query<Producto, String> query) {
		 if (filterText.isEmpty()) {
	            return manageProducto.listProducto().stream();
	        }
	     return manageProducto.listProducto().stream().filter(
	                producto -> producto.getNombre().toLowerCase().contains(filterText.toLowerCase())
	                        || producto.getDescripcion().toLowerCase().contains(filterText.toLowerCase())
	                        || (producto.getId()+"").equals(filterText)
	    		 			|| (producto.getCategoria()!=null?producto.getCategoria().getNombre().toLowerCase().contains(filterText.toLowerCase()):false));
	}

	private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }
}
