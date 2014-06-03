/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import Negocio.Articulo;
import Negocio.ItemPedido;
import Negocio.Pedido;
import Utiles.Utiles;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.Db4oException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DAOPedido {
    
    private ObjectContainer db;

    public DAOPedido(ObjectContainer db) {
        this.db = db;
    }
    
    public boolean AgregarPedido(Pedido pedido) throws Exception{
        boolean flag = true;
        try{
            //Graba el Pedido recibido por parametro
            db.store(pedido);
            //Persistir los cambios
            db.commit();
        }catch(Db4oException ex){
        	DAOErrorLog.AgregarErrorLog("AgregarPedido", "DAOPedido", "Error de db4o: " + ex.getMessage());
        	throw new Db4oException("Error de db4o al Agregar Pedido", ex);
        }catch(Exception ex){
            //Volver al estado anterior
            db.rollback();
            flag = false;
            //Graba log del error
            DAOErrorLog.AgregarErrorLog("AgregarPedido", "DAOPedido", ex.getMessage());
        	throw new Exception("Error inesperado al Agregar Pedido", ex);

        }
        //Devuelve TRUE en caso de exito y FALSE en caso contrario
        return flag;
    }
    
    public List<Pedido> GetAll() throws Exception{
       List<Pedido> lstPedido = new ArrayList();
       try{
            //Trae todos los objetos del tipo Pedido
            ObjectSet<Pedido> result = db.query(Pedido.class); 
            //Carga una lista del tipo Pedido 
            for(Pedido a : result){
                lstPedido.add(a);
            }
       }catch(Db4oException ex){
       		DAOErrorLog.AgregarErrorLog("GetAll", "DAOPedido", "Error de db4o: " + ex.getMessage());
       		throw new Db4oException("Error de db4o al obtener todos los pedidos", ex);
       }catch(Exception ex){
    	   	//Graba un log de errores en la DB
           	DAOErrorLog.AgregarErrorLog("GetAll", "DAOPedido", ex.getMessage());
           	throw new Exception("Error inesperado al obtener todos los pedidos", ex);
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return lstPedido;
    }
    
    public Pedido GetByCodigo(int codigo) throws Exception{
        Pedido resultado = null;
        try{
            //Trae todos los objetos del tipo Pedido
            ObjectSet result = db.queryByExample(new Pedido(codigo));
            Pedido encontrado = (Pedido)result.next();
            return encontrado;
        }catch(Db4oException ex){
       		DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOPedido", "Error de db4o: " + ex.getMessage());
       		throw new Db4oException("Error de db4o al obtener un pedido por codigo", ex);
        }catch(Exception ex){
        	//Graba un log de errores en la DB
        	DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOPedido", ex.getMessage());
      		throw new Exception("Error inesperado al obtener un pedido por codigo", ex);
        }
        
    }
    
    public  boolean AgregarPedido(List<Pedido> lstPedido) throws Exception{
        boolean flag = true;
        
        try{
            for(Pedido a : lstPedido){
                if(!AgregarPedido(a)){
                    flag = false;
                    break;
                }
            }
        }
        catch(Exception ex){
            flag = false;
            throw new Exception(ex.getMessage(),ex);
        }
        
        return flag;
    }
    
    public  List<Pedido> ImportarPedidos() throws Exception{
        List<Pedido> lstPedidos = new ArrayList();
        DAOCliente daoCliente = new DAOCliente(this.db);
        DAOEstado daoEstado = new DAOEstado(this.db);
        
        BufferedReader br = null;
        String line = "";
        String error = "";
        
        try {
		br = new BufferedReader(new FileReader(Utiles.IMPORT_FILE_PATH_PEDIDOS));
		while ((line = br.readLine()) != null) {
                        
			String[] pedido = line.split(Utiles.CSV_SPLIT_BY);
                        // Crea un objeto y lo agrega a la lista
                        lstPedidos.add(new Pedido(Integer.parseInt(pedido[0]), 
                                                  daoEstado.GetByCodigo(Integer.parseInt(pedido[1])),
                                                  Date.valueOf(pedido[2]),
                                                  daoCliente.GetByCodigo(pedido[3])));
		}
 
		}catch (FileNotFoundException e) {
			error = "No se encontro el archivo: " + e.getStackTrace();
		}catch (IOException e) {
			error = "Error al leer el archivo: " + e.getStackTrace();
		}catch(Db4oException ex){
	        error = "Error de d44o al importar datos de Pedido: " + ex.getStackTrace();
	    }catch(Exception e){
	            error = "Error al leer el archivo: " + e.getStackTrace();
	    }
	    finally {
			if (br != null) {
				try {
					br.close();	
				}catch (IOException e) {
					error = "Error al cerrar el archivo: " + e.getStackTrace().toString();
				}	
			}
		    if(error.trim() != ""){
		    	DAOErrorLog.AgregarErrorLog("ImportarPedidos", "DAOPedido", error);
		    	throw new Exception(error);
		    }                        
		}
        return lstPedidos;
    }
}
