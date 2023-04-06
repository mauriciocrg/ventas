package com.ventas;

import com.ventas.about.AboutView;
import com.ventas.client.AboutClientView;
import com.ventas.client.CarritoView;
import com.ventas.client.CatalogoView;
import com.ventas.client.PerfilView;
import com.ventas.crud.CategoriaCrudView;
import com.ventas.crud.PedidosView;
import com.ventas.crud.ProductoCrudView;
import com.ventas.crud.SampleCrudView;
import com.ventas.crud.UsuarioCrudView;
import com.ventas.reports.ReportesView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
public class MainScreen extends HorizontalLayout {
    private Menu menu;

    public MainScreen(VentasUI ui) {

        setSpacing(false);
        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(ErrorView.class);
        menu = new Menu(navigator);
        
        
        if(VentasUI.get().getAccessControl().isUserInRole("admin")) {
	        menu.addView(new UsuarioCrudView(), UsuarioCrudView.VIEW_NAME, UsuarioCrudView.VIEW_NAME, VaadinIcons.USERS);
	        menu.addView(new CategoriaCrudView(), CategoriaCrudView.VIEW_NAME, CategoriaCrudView.VIEW_NAME, VaadinIcons.LIST_OL);
	        menu.addView(new ProductoCrudView(), ProductoCrudView.VIEW_NAME, ProductoCrudView.VIEW_NAME, VaadinIcons.STOCK);
	        menu.addView(new PedidosView(), PedidosView.VIEW_NAME, PedidosView.VIEW_NAME, VaadinIcons.CART);
	        //menu.addView(new SampleCrudView(), SampleCrudView.VIEW_NAME, SampleCrudView.VIEW_NAME, VaadinIcons.EDIT);
	        menu.addView(new ReportesView(), ReportesView.VIEW_NAME, ReportesView.VIEW_NAME, VaadinIcons.FILE_TABLE);
	        
	        CarritoView carritoView = new CarritoView();
        	
        	menu.addView(new CatalogoView(carritoView), CatalogoView.VIEW_NAME, CatalogoView.VIEW_NAME, VaadinIcons.GRID_BIG);
	        menu.addView(carritoView, CarritoView.VIEW_NAME, CarritoView.VIEW_NAME, VaadinIcons.CART);
	        
	        menu.addView(new AboutView(), AboutView.VIEW_NAME, AboutView.VIEW_NAME, VaadinIcons.INFO_CIRCLE);
        } else {
        	CarritoView carritoView = new CarritoView();
        	
        	menu.addView(new CatalogoView(carritoView), CatalogoView.VIEW_NAME, CatalogoView.VIEW_NAME, VaadinIcons.GRID_BIG);
	        menu.addView(carritoView, CarritoView.VIEW_NAME, CarritoView.VIEW_NAME, VaadinIcons.CART);
	        menu.addView(new PerfilView(), PerfilView.VIEW_NAME, PerfilView.VIEW_NAME, VaadinIcons.USER);
	        menu.addView(new AboutClientView(), AboutClientView.VIEW_NAME, AboutClientView.VIEW_NAME, VaadinIcons.INFO_CIRCLE);
        }

        navigator.addViewChangeListener(viewChangeListener);

        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
    }

    // notify the view menu about view changes so that it can display which view
    // is currently active
    ViewChangeListener viewChangeListener = new ViewChangeListener() {

        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };
}
