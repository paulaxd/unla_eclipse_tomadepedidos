/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import Negocio.Estado;
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
public class DAOEstado {
    
    private ObjectContainer db;

    public DAOEstado(ObjectContainer db) {
        this.db = db;
    }
    
    public boolean AgregarEstado(Estado estado){
        boolean flag = true;
        try{
            //Graba el Estado recibido por parametro
            db.store(estado);
            //Persistir los cambios
            db.commit();
        }
        catch(Exception ex){
            //Volver al estado anterior
            db.rollback();
            flag = false;
            //Graba log del error
            DAOErrorLog.AgregarErrorLog("AgregarEstado", "DAOEstado", ex.getMessage());
        }
        //Devuelve TRUE en caso de exito y FALSE en caso contrario
        return flag;
    }

    public List<Estado> GetAll(){
       List<Estado> lstEstado = new ArrayList();
       try{
            //Trae todos los objetos del tipo Estado
            ObjectSet<Estado> result = db.query(Estado.class); 
            //Carga una lista del tipo Estado 
            for(Estado a : result){
                lstEstado.add(a);
            }
       }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOEstado", ex.getMessage());
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return lstEstado;
    }
    
    public Estado GetByCodigo(int codigo){
        Estado resultado = null;
        try{
            //Trae todos los objetos del tipo Estado
            ObjectSet result = db.queryByExample(new Estado(codigo));
            Estado encontrado = (Estado)result.next();
            return encontrado;
        }
        catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOEstado", ex.getMessage());
        }
        return null;
    }
    
    public  boolean AgregarEstado(List<Estado> lstEstado){
        boolean flag = true;
        
        try{
            for(Estado a : lstEstado){
                if(!AgregarEstado(a)){
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
    
    public  List<Estado> ImportarEstado(){
        List<Estado> lstEstado = new ArrayList();
        
        BufferedReader br = null;
	String line = "";
        String error = "";
        
        try {
		br = new BufferedReader(new FileReader(Utiles.IMPORT_FILE_PATH_ESTADOS));
		while ((line = br.readLine()) != null) {
                        
			String[] estado = line.split(Utiles.CSV_SPLIT_BY);
                        // Crea un objeto articulo y lo agrega a la lista
                        lstEstado.add(new Estado(Integer.parseInt(estado[0]), 
                                                      estado[1]));
 
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
                    DAOErrorLog.AgregarErrorLog("ImportarEstado", "DAOEstado", error);
                }
                
	}
        return lstEstado;
    }
}
