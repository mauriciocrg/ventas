package com.ventas.backend.manageData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ventas.backend.data.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ventas.backend.core.HibernateConfiguration;

public class ManagePedido {

	public void savePedido(Pedido pedido){
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			session.save(pedido);
			for(PedidoItem pitem : pedido.getItems()) {
				session.save(pitem);
			}
			tx.commit();
			session.close();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			if(session.isOpen())session.close();
		}
	}
	
	public void saveOrUpdatePedido(Pedido pedido){
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			session.saveOrUpdate(pedido);
			for(PedidoItem pitem : pedido.getItems()) {
				session.saveOrUpdate(pitem);
			}
			tx.commit();
			session.close();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			if(session.isOpen())session.close();
		}
	}
	
	public void saveOrUpdatePedidoItem(PedidoItem pedidoItem){
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			session.saveOrUpdate(pedidoItem);
			tx.commit();
			session.close();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			if(session.isOpen())session.close();
		}
	}
	
	public List <Pedido> listPedido() {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		List <Pedido> listPedido = new ArrayList();
		
		try{
			tx = session.beginTransaction();
			listPedido = (List <Pedido>)session.createQuery("FROM Pedido").list();
	        tx.commit();
	        session.close();
		}catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
		return listPedido;
	}
	
	public void updatePedido(Pedido pedido){
		Session session = HibernateConfiguration.getInstance().getSession();
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
			session.update(pedido); 
			for(PedidoItem pitem : pedido.getItems()) {
				session.update(pitem);
			}
	        tx.commit();
	        session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close();
	    }
	}
	   
	public void deletePedido(int id){
		Session session = HibernateConfiguration.getInstance().getSession();
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
	    	Pedido pedido = (Pedido)session.get(Pedido.class, id); 
	        session.delete(pedido);
	        for(PedidoItem pitem : pedido.getItems()) {
				session.delete(pitem);
			}
	        tx.commit();
	        session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	    	e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
	}
	
	public Pedido getPedido(int id){
		Session session = HibernateConfiguration.getInstance().getSession();
		Pedido Pedido = null;
	    Transaction tx = null;
	    try{
	    	tx = session.beginTransaction();
	    	Pedido =  (Pedido)session.get(Pedido.class, id);
	    	tx.commit();
	    	session.close();
	    }catch (HibernateException e) {
	    	if (tx!=null) tx.rollback();
	    	e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close(); 
	    }
	    return Pedido;
	}
	
	
	public List<FacturacionVendedor> listFacturacionPorVendedor(Date desde, Date hasta) {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		List <FacturacionVendedor> listFacturacion = new ArrayList();
		
		try{
			tx = session.beginTransaction();
			
			String query_1 = "SELECT U.nombre, SUM(PI.cantidad * PI.producto.costo_venta) FROM Pedido P,PedidoItem PI, Usuario U WHERE P.id = PI.pedido.id AND U.id = P.vendedor.id AND PI.disponible = 1 AND P.vendedor != null AND P.estado = "+Pedido.ESTADO_LISTO;
			String query_2 = " GROUP BY U.nombre ORDER BY U.nombre DESC";
			
			String searchFechasValue = "";
			
			int parameters = 0;
			
			if(desde != null && hasta != null) {
				searchFechasValue = searchFechasValue +
				" AND (P.fecha BETWEEN :desde AND :hasta) ";
				parameters = 1;
			} else
			if(desde != null && hasta == null) {
				searchFechasValue = searchFechasValue +  
				" AND P.fecha >= :desde ";
				parameters = 2;
			} else
			if(desde == null && hasta != null) {
				searchFechasValue = searchFechasValue + 
				" AND P.fecha <= :hasta ";
				parameters = 3;
			}
			 
			Query query = session.createQuery(query_1+searchFechasValue+query_2);
			
			switch (parameters) {
				case 1: 
					query.setParameter("desde", desde);
					query.setParameter("hasta", hasta);
				break;
				case 2: 
					query.setParameter("desde", desde);
				break;
				case 3: 
					query.setParameter("hasta", hasta);
				break;
			}
			
			List resultList = query.list();
	        tx.commit();
	        session.close();
	        
	        for (Object object : resultList) {
	        	
	            Object[] result = (Object[]) object;
	            
	            FacturacionVendedor facturacionVendedor = new FacturacionVendedor();
	            
	            facturacionVendedor.setNombreVendedor((String)result[0]);
	            facturacionVendedor.setImporte(((BigDecimal) result[1]).doubleValue());
	            
	            listFacturacion.add(facturacionVendedor);
	        }
		}catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close();
	    }
		return listFacturacion;
	}

	public List<FacturacionDia> listFacturacionDiaria(Date desde, Date hasta) {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;

		List <FacturacionDia> listFacturacion = new ArrayList();

		try{
			tx = session.beginTransaction();

			String query_1 = "SELECT DATE(P.fecha), SUM(PI.cantidad * PI.producto.costo_venta) FROM Pedido P,PedidoItem PI WHERE P.id = PI.pedido.id AND PI.disponible = 1 AND P.estado = "+Pedido.ESTADO_LISTO;
			String query_2 = " GROUP BY DATE(P.fecha) ORDER BY DATE(P.fecha) DESC";

			String searchFechasValue = "";

			int parameters = 0;

			if(desde != null && hasta != null) {
				searchFechasValue = searchFechasValue +
						" AND (P.fecha BETWEEN :desde AND :hasta) ";
				parameters = 1;
			} else
			if(desde != null && hasta == null) {
				searchFechasValue = searchFechasValue +
						" AND P.fecha >= :desde ";
				parameters = 2;
			} else
			if(desde == null && hasta != null) {
				searchFechasValue = searchFechasValue +
						" AND P.fecha <= :hasta ";
				parameters = 3;
			}

			Query query = session.createQuery(query_1+searchFechasValue+query_2);

			switch (parameters) {
				case 1:
					query.setParameter("desde", desde);
					query.setParameter("hasta", hasta);
					break;
				case 2:
					query.setParameter("desde", desde);
					break;
				case 3:
					query.setParameter("hasta", hasta);
					break;
			}

			List resultList = query.list();
			tx.commit();
			session.close();

			for (Object object : resultList) {

				Object[] result = (Object[]) object;

				FacturacionDia facturacionDia = new FacturacionDia();

				facturacionDia.setFecha((Date) result[0]);
				facturacionDia.setImporte(((BigDecimal) result[1]).doubleValue());

				listFacturacion.add(facturacionDia);
			}
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		}finally {
			if(session.isOpen())session.close();
		}
		return listFacturacion;
	}

	public List<FacturacionProducto> listFacturacionPorProducto(Date desde, Date hasta) {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		List <FacturacionProducto> listFacturacion = new ArrayList();
		
		try{
			tx = session.beginTransaction();
			
			
			//;
			
			String query_1 = "SELECT PI.producto.nombre ,SUM(PI.cantidad * PI.producto.costo_venta) FROM Pedido P,PedidoItem PI WHERE P.id = PI.pedido.id AND PI.disponible = 1 AND P.estado = "+Pedido.ESTADO_LISTO;
			String query_2 = " GROUP BY PI.producto.nombre ORDER BY SUM(PI.cantidad * PI.producto.costo_venta) DESC";
			
			String searchFechasValue = "";
			String searchProductoValue = "";
			
			int parameters = 0;
			boolean producto = false;
			
			if(desde != null && hasta != null) {
				searchFechasValue = searchFechasValue +
				" AND (P.fecha BETWEEN :desde AND :hasta) ";
				parameters = 1;
			} else
			if(desde != null && hasta == null) {
				searchFechasValue = searchFechasValue +  
				" AND P.fecha >= :desde ";
				parameters = 2;
			} else
			if(desde == null && hasta != null) {
				searchFechasValue = searchFechasValue + 
				" AND P.fecha <= :hasta ";
				parameters = 3;
			}
			
			
			 
			Query query = session.createQuery(query_1+searchFechasValue+searchProductoValue+query_2);
			
			switch (parameters) {
				case 1: 
					query.setParameter("desde", desde);
					query.setParameter("hasta", hasta);
				break;
				case 2: 
					query.setParameter("desde", desde);
				break;
				case 3: 
					query.setParameter("hasta", hasta);
				break;
			}
			
			List resultList = query.list();
	        tx.commit();
	        session.close();
	        
	        for (Object object : resultList) {
	        	
	            Object[] result = (Object[]) object;
	            
	            FacturacionProducto facturacionProducto = new FacturacionProducto();
	            
	            facturacionProducto.setNombreMenuItem((String) result[0]);
	            facturacionProducto.setImporte(((BigDecimal) result[1]).doubleValue());
	            
	            listFacturacion.add(facturacionProducto);
	        }
		}catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close();
	    }
		return listFacturacion;
	}
	
	public List<FacturacionCliente> listFacturacionPorCliente(Date desde, Date hasta) {
		Session session = HibernateConfiguration.getInstance().getSession();
		Transaction tx = null;
		
		List <FacturacionCliente> listFacturacion = new ArrayList();
		
		try{
			tx = session.beginTransaction();
			
			
			//;
			
			String query_1 = "SELECT P.usuario.nombre ,SUM(PI.cantidad * PI.producto.costo_venta) FROM Pedido P,PedidoItem PI WHERE P.id = PI.pedido.id AND PI.disponible = 1 AND P.estado = "+Pedido.ESTADO_LISTO;
			String query_2 = " GROUP BY P.usuario.nombre ORDER BY SUM(PI.cantidad * PI.producto.costo_venta) DESC";
			
			String searchFechasValue = "";
			String searchProductoValue = "";
			
			int parameters = 0;
			boolean producto = false;
			
			if(desde != null && hasta != null) {
				searchFechasValue = searchFechasValue +
				" AND (P.fecha BETWEEN :desde AND :hasta) ";
				parameters = 1;
			} else
			if(desde != null && hasta == null) {
				searchFechasValue = searchFechasValue +  
				" AND P.fecha >= :desde ";
				parameters = 2;
			} else
			if(desde == null && hasta != null) {
				searchFechasValue = searchFechasValue + 
				" AND P.fecha <= :hasta ";
				parameters = 3;
			}
			
			
			 
			Query query = session.createQuery(query_1+searchFechasValue+searchProductoValue+query_2);
			
			switch (parameters) {
				case 1: 
					query.setParameter("desde", desde);
					query.setParameter("hasta", hasta);
				break;
				case 2: 
					query.setParameter("desde", desde);
				break;
				case 3: 
					query.setParameter("hasta", hasta);
				break;
			}
			
			List resultList = query.list();
	        tx.commit();
	        session.close();
	        
	        for (Object object : resultList) {
	        	
	            Object[] result = (Object[]) object;
	            
	            FacturacionCliente facturacionCliente = new FacturacionCliente();
	            
	            facturacionCliente.setNombreCliente((String) result[0]);
	            facturacionCliente.setImporte(((BigDecimal) result[1]).doubleValue());
	            
	            listFacturacion.add(facturacionCliente);
	        }
		}catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	    	if(session.isOpen())session.close();
	    }
		return listFacturacion;
	}
}
