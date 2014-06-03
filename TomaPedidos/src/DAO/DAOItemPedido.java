/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import Negocio.ItemPedido;
import Utiles.Utiles;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maurogarcia
 */
public class DAOItemPedido {
    
    private ObjectContainer db;

    public DAOItemPedido(ObjectContainer db) {
        this.db = db;
    }
    
    public boolean AgregarItemPedido(ItemPedido itemPedido){
        boolean flag = true;
        try{
            //Graba el ArticuloPedido recibido por parametro
            db.store(itemPedido);
            //Persistir los cambios
            db.commit();
        }
        catch(Exception ex){
            //Volver al estado anterior
            db.rollback();
            flag = false;
            //Graba log del error
            DAOErrorLog.AgregarErrorLog("AgregarArticuloPedido", "DAOArticuloPedido", ex.getMessage());
        }
        //Devuelve TRUE en caso de exito y FALSE en caso contrario
        return flag;
    }

    public List<ItemPedido> GetAll(){
       List<ItemPedido> lstItemPedido = new ArrayList();
       try{
            //Trae todos los objetos del tipo ArticuloPedido
            ObjectSet<ItemPedido> result = db.query(ItemPedido.class); 
            //Carga una lista del tipo ArticuloPedido 
            for(ItemPedido a : result){
            	lstItemPedido.add(a);
            }
       }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOItemPedido", ex.getMessage());
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return lstItemPedido;
    }
    
    public boolean AgregarItemPedido(List<ItemPedido> lstItemPedido){
        boolean flag = true;
        DAOPedido daoPedido = new DAOPedido(this.db);
        try{
            for(ItemPedido a : lstItemPedido){
                if(!AgregarItemPedido(a)){
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
    
    public List<ItemPedido> ImportarItemPedido(){
	        
    	List<ItemPedido> lstItemPedido = new ArrayList();
        DAOArticulo daoArticulo = new DAOArticulo(this.db);
        DAOPedido daoPedido = new DAOPedido(this.db);
        BufferedReader br = null;
        String line = "";
        String error = "";
	        
        try {
			br = new BufferedReader(new FileReader(Utiles.IMPORT_FILE_PATH_ITEMPEDIDO));
			while ((line = br.readLine()) != null) {
	                        
				String[] condicion = line.split(Utiles.CSV_SPLIT_BY);
	                        // Crea un objeto y lo agrega a la lista
				lstItemPedido.add(new ItemPedido(daoPedido.GetByCodigo(Integer.parseInt(condicion[0])), 
                                                  daoArticulo.GetByCodigo(Integer.parseInt(condicion[1])),
                                                  condicion[3],
                                                  Integer.parseInt(condicion[3])));
	 
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
	                    DAOErrorLog.AgregarErrorLog("ImportarArticuloPedido", "DAOArticuloPedido", error);
	                }
	                
		}
        return lstItemPedido;
    }
}
