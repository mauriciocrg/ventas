package com.ventas.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ventas.backend.core.Config;
import com.ventas.backend.core.HibernateConfiguration;



public class ContextListener implements ServletContextListener {

	private ServletContext context = null;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		context = event.getServletContext();
		
		Config.baseDirectory = context.getRealPath("");
		Config.getInstance();
		
		HibernateConfiguration.getInstance();
		
		System.out.println("Initialized context!!");
	}

}
