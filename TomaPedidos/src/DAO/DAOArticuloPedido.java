/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import Negocio.ArticuloPedido;
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
public class DAOArticuloPedido {
    
    private ObjectContainer db;

    public DAOArticuloPedido(ObjectContainer db) {
        this.db = db;
    }
    
    public boolean AgregarArticuloPedido(ArticuloPedido articuloPedido){
        boolean flag = true;
        try{
            //Graba el ArticuloPedido recibido por parametro
            db.store(articuloPedido);
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

    public List<ArticuloPedido> GetAll(){
       List<ArticuloPedido> lstArticulosPedido = new ArrayList();
       try{
            //Trae todos los objetos del tipo ArticuloPedido
            ObjectSet<ArticuloPedido> result = db.query(ArticuloPedido.class); 
            //Carga una lista del tipo ArticuloPedido 
            for(ArticuloPedido a : result){
                lstArticulosPedido.add(a);
            }
       }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOArticuloPedido", ex.getMessage());
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return lstArticulosPedido;
    }
    
    public boolean AgregarArticuloPedido(List<ArticuloPedido> lstArticuloPedido){
        boolean flag = true;
        try{
            for(ArticuloPedido a : lstArticuloPedido){
                if(!AgregarArticuloPedido(a)){
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
    
        public List<ArticuloPedido> ImportarArticuloPedido(){
        List<ArticuloPedido> lstArticuloPedido = new ArrayList();
        DAOArticulo daoArticulo = new DAOArticulo(this.db);
        DAOPedido daoPedido = new DAOPedido(this.db);
        BufferedReader br = null;
	String line = "";
        String error = "";
        
        try {
		br = new BufferedReader(new FileReader(Utiles.IMPORT_FILE_PATH_ARTICULOPEDIDO));
		while ((line = br.readLine()) != null) {
                        
			String[] condicion = line.split(Utiles.CSV_SPLIT_BY);
                        // Crea un objeto y lo agrega a la lista
                        lstArticuloPedido.add(new ArticuloPedido(daoPedido.GetByCodigo(Integer.parseInt(condicion[0])), 
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
        return lstArticuloPedido;
    }
}
