/*
 * Creado el 22/02/2009
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generaci�n de c�digo&gt;C�digo y comentarios
 */
package com.ventas.backend.core;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.ventas.backend.data.Categoria;
import com.ventas.backend.data.Pedido;
import com.ventas.backend.data.PedidoItem;
import com.ventas.backend.data.Producto;
import com.ventas.backend.data.Usuario;



/**
 * @author Mauricio
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generaci�n de c�digo&gt;C�digo y comentarios
 */
public class HibernateConfiguration {
	
	private static Configuration cfg = null;
	private static SessionFactory sessions = null;
	
	private static HibernateConfiguration hibernateConfiguration = null;

	private HibernateConfiguration() {
		cfg = new Configuration();
		setProperties();
		setResources();
		sessions = cfg.buildSessionFactory();
	}
	
	private void setResources() {
		cfg.addClass(Usuario.class);
		cfg.addClass(Categoria.class);
		cfg.addClass(Producto.class);
		cfg.addClass(Pedido.class);
		cfg.addClass(PedidoItem.class);
	}
	
	private void setProperties() {
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
		cfg.setProperty("hibernate.connection.driver_class","com.mysql.jdbc.Driver");
		cfg.setProperty("hibernate.connection.url",Config.getInstance().getProperti(Config.MYSQL_PATH));
		cfg.setProperty("hibernate.connection.username",Config.getInstance().getProperti(Config.USER));
		cfg.setProperty("hibernate.connection.password",Cripto.getInstance().Desencriptar(Config.getInstance().getProperti(Config.PASSWORD)));	
		
		
		/*cfg.setProperty("show_sql","true");
		cfg.setProperty("format_sql","true");
		cfg.setProperty("use_sql_comments","true");*/
	}
	
	public static Session getSession() {
		return sessions.openSession();
	}
	
	public static HibernateConfiguration getInstance() {
		if(hibernateConfiguration == null) {
			hibernateConfiguration = new HibernateConfiguration();
		}
		return hibernateConfiguration;
	}
}
