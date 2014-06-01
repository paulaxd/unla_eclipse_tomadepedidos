/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import Negocio.Pedido;
import Utiles.Utiles;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
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
    
    public boolean AgregarPedido(Pedido pedido){
        boolean flag = true;
        try{
            //Graba el Pedido recibido por parametro
            db.store(pedido);
            //Persistir los cambios
            db.commit();
        }
        catch(Exception ex){
            //Volver al estado anterior
            db.rollback();
            flag = false;
            //Graba log del error
            DAOErrorLog.AgregarErrorLog("AgregarPedido", "DAOPedido", ex.getMessage());
        }
        //Devuelve TRUE en caso de exito y FALSE en caso contrario
        return flag;
    }

    public List<Pedido> GetAll(){
       List<Pedido> lstPedido = new ArrayList();
       try{
            //Trae todos los objetos del tipo Pedido
            ObjectSet<Pedido> result = db.query(Pedido.class); 
            //Carga una lista del tipo Pedido 
            for(Pedido a : result){
                lstPedido.add(a);
            }
       }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOPedido", ex.getMessage());
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return lstPedido;
    }
    
    public Pedido GetByCodigo(int codigo){
        Pedido resultado = null;
        try{
            //Trae todos los objetos del tipo Pedido
            ObjectSet result = db.queryByExample(new Pedido(codigo));
            Pedido encontrado = (Pedido)result.next();
            return encontrado;
        }
        catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOPedido", ex.getMessage());
        }
        return null;
    }
    
    public  boolean AgregarPedido(List<Pedido> lstPedido){
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
        }
        
        return flag;
    }
    
    public  List<Pedido> ImportarPedidos(){
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
 
	} catch (FileNotFoundException e) {
		error = "No se encontro el archivo: " + e.getStackTrace();
	} catch (IOException e) {
		error = "Error al leer el archivo: " + e.getStackTrace();
	}catch(Exception e){
            error = "Error al leer el archivo: " + e.getStackTrace();
        }
        finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				error = "Error al cerrar el archivo: " + e.getStackTrace().toString();
			}
		}
                
                if(error.trim() != ""){
                    DAOErrorLog.AgregarErrorLog("ImportarPedidos", "DAOPedido", error);
                }
                
	}
        return lstPedidos;
    }

}
