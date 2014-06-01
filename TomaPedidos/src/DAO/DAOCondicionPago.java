/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import Negocio.CondicionPago;
import Utiles.Utiles;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
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
public class DAOCondicionPago {
    
    private ObjectContainer db;

    public DAOCondicionPago(ObjectContainer db) {
        this.db = db;
    }

    public boolean AgregarCondicionPago(CondicionPago condicionPago){
        boolean flag = true;
        try{
            //Graba la condicion de pago recibida por parametro
            db.store(condicionPago);
            //Persistir los cambios
            db.commit();
        }
        catch(Exception ex){
            //Volver al estado anterior
            db.rollback();
            flag = false;
            //Graba log del error
            DAOErrorLog.AgregarErrorLog("AgregarCondicionPago", "DAOCondicionPago", ex.getMessage());
        }
        //Devuelve TRUE en caso de exito y FALSE en caso contrario
        return flag;
    }
    
    public boolean AgregarCondicionPago(List<CondicionPago> lstCondicionPago){
        boolean flag = true;
        
        try{
            for(CondicionPago c : lstCondicionPago){
                if(!AgregarCondicionPago(c)){
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

    public List<CondicionPago> GetAll(){
       List<CondicionPago> lstCondicionPago = new ArrayList();
       try{
            //Trae todos los objetos del tipo CondicionPago
            ObjectSet<CondicionPago> result = db.query(CondicionPago.class); 
            //Carga una lista del tipo CondicionPago 
            for(CondicionPago a : result){
                lstCondicionPago.add(a);
            }
       }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOCondicionPago", ex.getMessage());
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return lstCondicionPago;
    }
    
    public CondicionPago GetByCodigo(final int codigo){
        CondicionPago resultado = null;
        try{
            //Trae todos los objetos del tipo CondicionPago
//            ObjectSet result = db.queryByExample(new CondicionPago(codigo));
//            CondicionPago encontrado = (CondicionPago)result.next();
            
            List<CondicionPago> result = db.query(new Predicate<CondicionPago>() {
                public boolean match(CondicionPago c) {
                    return c.getCodigo() == codigo;
                }
            });
            return (CondicionPago)result.get(0);
        }
        catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOCondicionPago", ex.getMessage());
        }
        return null;
    }
    
    public List<CondicionPago> ImportarCondicionPago(){
        List<CondicionPago> lstCondicionesPago = new ArrayList();
        
        BufferedReader br = null;
	String line = "";
        String error = "";
        
        try {
		br = new BufferedReader(new FileReader(Utiles.IMPORT_FILE_PATH_CONDICIONESPAGO));
		while ((line = br.readLine()) != null) {
                        
			String[] condicion = line.split(Utiles.CSV_SPLIT_BY);
                        // Crea un objeto articulo y lo agrega a la lista
                        lstCondicionesPago.add(new CondicionPago(Integer.parseInt(condicion[0]), 
                                                      condicion[1]));
 
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
                    DAOErrorLog.AgregarErrorLog("ImportarCondicionPago", "DAOCondicionPago", error);
                }
                
	}
        return lstCondicionesPago;
    }
}
