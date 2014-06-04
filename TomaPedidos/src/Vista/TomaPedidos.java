/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import DAO.DAOArticulo;
import DAO.DAOErrorLog;
import DAO.DAOItemPedido;
import DAO.DAOCliente;
import DAO.DAOCondicionPago;
import DAO.DAOEstado;
import DAO.DAOManager;
import DAO.DAOPedido;
import DAO.DAOSincronizacion;
import Negocio.Articulo;
import Negocio.ItemPedido;
import Negocio.Cliente;
import Negocio.CondicionPago;
import Negocio.Estado;
import Negocio.Pedido;
import Negocio.Sincronizacion;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author m_pau_000
 */
public class TomaPedidos {

    /**
     * @param args the command line arguments
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
    	DAOManager daoManager = null;
    	try{
	    	// Borra la DB para no duplicar.
	        DAOManager.BorrarDB();
	        // Crea y abre la DB.
	        daoManager = new DAOManager();
	        
	        DAOArticulo daoArticulo = new DAOArticulo(daoManager.GetObjectContainter());
	        List<Articulo> lst = daoArticulo.ImportarArticulos();
	        daoArticulo.AgregarArticulo(lst);
	        
	        System.out.println("\n********     ARTICULOS     *********\n");
	        
	        for(Articulo a : daoArticulo.GetAll()){
	            System.out.println(a.toString());
	        }
	        
	        DAOCondicionPago daoCondicionPago = new DAOCondicionPago(daoManager.GetObjectContainter());
	        List<CondicionPago> lstCondicion = daoCondicionPago.ImportarCondicionPago();
	        daoCondicionPago.AgregarCondicionPago(lstCondicion);
	        
	        System.out.println("\n********     CONDICION DE PAGO     *********\n");
	        
	        for(CondicionPago c : daoCondicionPago.GetAll()){
	            System.out.println(c.toString());
	        }
	        
	        DAOCliente daoCliente = new DAOCliente(daoManager.GetObjectContainter());
	        List<Cliente> lstCliente = daoCliente.ImportarClientes();
	        daoCliente.AgregarCliente(lstCliente);
	        
	        System.out.println("\n********     CLIENTES     *********\n");
	        
	        for(Cliente c : daoCliente.GetAll()){
	            System.out.println(c.toString());
	        }
	        
	        DAOEstado daoEstado = new DAOEstado(daoManager.GetObjectContainter());
	        List<Estado> lstEstado = daoEstado.ImportarEstado();
	        daoEstado.AgregarEstado(lstEstado);
	        
	        System.out.println("\n********     ESTADOS DE PEDIDO     *********\n");
	        
	        for(Estado e : daoEstado.GetAll()){
	            System.out.println(e.toString());
	        }
	        
	        DAOPedido daoPedido = new DAOPedido(daoManager.GetObjectContainter());
	        List<Pedido> lstPedido = daoPedido.ImportarPedidos();
	        daoPedido.AgregarPedido(lstPedido);
	        
	        System.out.println("\n********     PEDIDOS     *********\n");
	        
	        for(Pedido p : daoPedido.GetAll()){
	            System.out.println(p.toString());
	        }
	        
	        DAOItemPedido daoItemPedido = new DAOItemPedido(daoManager.GetObjectContainter());
	        List<ItemPedido> lstArticuloPedido = daoItemPedido.ImportarItemPedido();
	        daoItemPedido.AgregarItemPedido(lstArticuloPedido);
	        
	        System.out.println("\n********     ITEMS POR PEDIDO     *********\n");
	        
	        for(ItemPedido ap : daoItemPedido.GetAll()){
	            System.out.println(ap.toString());
	        }
	        
	        // Genero y grabo sincronizacion de prueba
	        
	        DAOSincronizacion daoSincronizacion = new DAOSincronizacion(daoManager.GetObjectContainter());
	    	java.util.Calendar cal = java.util.Calendar.getInstance();
	    	java.util.Date utilDate = cal.getTime();
	    	java.sql.Date sqlDate = new Date(utilDate.getTime());
	    	Sincronizacion sinc = new Sincronizacion(sqlDate, 123);
	    	daoSincronizacion.AgregarSincronizacion(sinc);
	        
	        System.out.println("\n********     SINCRONIZACION     *********\n");
	        
	        for(Sincronizacion s : daoSincronizacion.GetAll()){
	            System.out.println(s.toString());
	        }
	        
	        // GUARDA LOS CAMBIOS
	        daoManager.Commit();
	        
	        //CIERRA LA DB
	        daoManager.CerrarDB();
	        
    	} catch(Exception ex){
    		DAOErrorLog.AgregarErrorLog("Main", "TomaPedidos", ex.getCause().getStackTrace().toString());
    		System.out.println(ex.getMessage());
    		System.out.println("Para mas informaci�n: " + Utiles.Utiles.ERRORLOG_FILE_PATH);
    		if(daoManager != null){
    			daoManager.Rollback();
    		}else{
    			DAOErrorLog.AgregarErrorLog("Main", "TomaPedidos", "Error al abrir/crear la DB: " + ex.getCause().getStackTrace().toString());
    		}
    	}
    	
    	
    }
    
}
