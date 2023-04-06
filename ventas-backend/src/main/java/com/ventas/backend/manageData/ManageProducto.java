package com.ventas.backend.manageData;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ventas.backend.core.HibernateConfiguration;
import com.ventas.backend.data.Producto;

public class ManageProducto {

	public void saveProducto(Producto producto){
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			session.save(producto); 
			tx.commit();
			session.close();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			if(session.isOpen())session.close();
		}
	}
	
	public void saveOrUpdateProducto(Producto usuario){
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			session.saveOrUpdate(usuario); 
			tx.commit();
			session.close();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			if(session.isOpen())session.close();
		}
	}
	
	public List <Producto> listProducto() {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		List <Producto> listProducto = new ArrayList();
		
		try{
			tx = session.beginTransaction();
			listProducto = (List <Producto>)session.createQuery("FROM Producto").list();
	        tx.commit();
	        session.close();
		}catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
		return listProducto;
	}
	
	public List <Producto> listProducto(String filter,int id_categoria) {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		List <Producto> listProducto = new ArrayList();
		
		try{
			tx = session.beginTransaction();
			
			String sql = "FROM Producto WHERE (descripcion LIKE ? OR nombre LIKE ?) AND disponible = 1 ";
			
			if(id_categoria != 0) {
				sql = sql + " AND id_categoria = "+id_categoria;
			}
			
			//System.out.println(sql);
			
			Query query = session.createQuery(sql);
			query.setString(0, "%"+filter+"%");
			query.setString(1, "%"+filter+"%");
			listProducto = (List <Producto>)query.list();
	        tx.commit();
	        session.close();
		}catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
		return listProducto;
	}
	
	public void updateProducto(Producto producto){
		Session session = HibernateConfiguration.getInstance().getSession();
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
			session.update(producto); 
	        tx.commit();
	        session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close();
	    }
	}
	   
	public void deleteProducto(int id){
		Session session = HibernateConfiguration.getInstance().getSession();
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
	    	Producto Producto = (Producto)session.get(Producto.class, id); 
	        session.delete(Producto); 
	        tx.commit();
	        session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	    	e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
	}
	
	public Producto getProducto(int id){
		Session session = HibernateConfiguration.getInstance().getSession();
		Producto Producto = null;
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
	    	Producto =  (Producto)session.get(Producto.class, id);
	    	tx.commit();
	    	session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	    	e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
	    return Producto;
	}
}
