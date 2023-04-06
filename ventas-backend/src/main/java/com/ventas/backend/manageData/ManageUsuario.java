package com.ventas.backend.manageData;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ventas.backend.core.HibernateConfiguration;
import com.ventas.backend.data.Usuario;


public class ManageUsuario {

	public void saveUsuario(Usuario usuario){
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			session.save(usuario); 
			tx.commit();
			session.close();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			if(session.isOpen())session.close();
		}
	}
	
	public void saveOrUpdateUsuario(Usuario usuario){
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
	
	public List <Usuario> listUsuario() {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		List <Usuario> listUsuario = new ArrayList();
		
		try{
			tx = session.beginTransaction();
			listUsuario = (List <Usuario>)session.createQuery("FROM Usuario").list();
	        tx.commit();
	        session.close();
		}catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
		return listUsuario;
	}
	
	public List <Usuario> listClientes() {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		List <Usuario> listUsuario = new ArrayList();
		
		try{
			tx = session.beginTransaction();
			listUsuario = (List <Usuario>)session.createQuery("FROM Usuario WHERE admin = 0 ").list();
	        tx.commit();
	        session.close();
		}catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
		return listUsuario;
	}

	public List <Usuario> listNonClientes() {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;

		List <Usuario> listUsuario = new ArrayList();

		try{
			tx = session.beginTransaction();
			listUsuario = (List <Usuario>)session.createQuery("FROM Usuario WHERE admin != 0 ").list();
			tx.commit();
			session.close();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		}finally {
			if(session.isOpen())session.close();
		}
		return listUsuario;
	}

	public void updateUsuario(Usuario usuario){
		Session session = HibernateConfiguration.getInstance().getSession();
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
			session.update(usuario); 
	        tx.commit();
	        session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close();
	    }
	}
	   
	public void deleteUsuario(int id){
		Session session = HibernateConfiguration.getInstance().getSession();
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
	    	Usuario Usuario = (Usuario)session.get(Usuario.class, id); 
	        session.delete(Usuario); 
	        tx.commit();
	        session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	    	e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
	}
	
	public Usuario getUsuario(int id){
		Session session = HibernateConfiguration.getInstance().getSession();
		Usuario Usuario = null;
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
	    	Usuario =  (Usuario)session.get(Usuario.class, id);
	    	tx.commit();
	    	session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	    	e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
	    return Usuario;
	}
	
	public Usuario getUsuario(String email){
		Session session = HibernateConfiguration.getInstance().getSession();
		Usuario usuario = null;
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
	    	Query query = session.createQuery("FROM Usuario u WHERE u.email = :x_email");
	    	query.setParameter("x_email", email);
	    	usuario =  (Usuario) query.uniqueResult();
	    	tx.commit();
	    	session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	    	e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
	    return usuario;
	}
}
