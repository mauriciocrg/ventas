package com.ventas.backend.manageData;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ventas.backend.core.HibernateConfiguration;
import com.ventas.backend.data.Categoria;


public class ManageCategoria {

	public void saveCategoria(Categoria categoria){
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			session.save(categoria); 
			tx.commit();
			session.close();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			if(session.isOpen())session.close();
		}
	}
	
	public void saveOrUpdateCategoria(Categoria categoria){
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			session.saveOrUpdate(categoria); 
			tx.commit();
			session.close();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			if(session.isOpen())session.close();
		}
	}
	
	public List <Categoria> listCategoria() {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		List <Categoria> listCategoria = new ArrayList();
		
		try{
			tx = session.beginTransaction();
			listCategoria = (List <Categoria>)session.createQuery("FROM Categoria").list();
	        tx.commit();
	        session.close();
		}catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
		return listCategoria;
	}
	
	public void updateCategoria(Categoria categoria){
		Session session = HibernateConfiguration.getInstance().getSession();
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
			session.update(categoria); 
	        tx.commit();
	        session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close();
	    }
	}
	   
	public void deleteCategoria(int id){
		Session session = HibernateConfiguration.getInstance().getSession();
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
	    	Categoria categoria = (Categoria)session.get(Categoria.class, id); 
	        session.delete(categoria); 
	        tx.commit();
	        session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	    	e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
	}
	
	public Categoria getCategoria(int id){
		Session session = HibernateConfiguration.getInstance().getSession();
		Categoria categoria = null;
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
	    	categoria =  (Categoria)session.get(Categoria.class, id);
	    	tx.commit();
	    	session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	    	e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
	    return categoria;
	}
}
