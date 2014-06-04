package DAO;

import Negocio.Articulo;
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

/**
 *
 * @author m_pau_000
 */
public class DAOArticulo {
    
    private ObjectContainer db;

    public DAOArticulo(ObjectContainer db) {
        this.db = db;
    }
    
    public boolean AgregarArticulo(Articulo articulo) throws Exception{
        boolean flag = true;
        try{
            //Graba el articulo recibido por parametro
            db.store(articulo);
            //Persistir los cambios
            db.commit();
        }
        catch(Db4oException ex){
        	DAOErrorLog.AgregarErrorLog("AgregarArticulo", "DAOArticulo", "Error de db4o: " + ex.getMessage());
        	throw new Db4oException("Error de db4o al Agregar Articulo", ex);
        }
        catch(Exception ex){
            //Volver al estado anterior
            db.rollback();
            flag = false;
            //Graba log del error
            DAOErrorLog.AgregarErrorLog("AgregarArticulo", "DAOArticulo", ex.getMessage());
            throw new Exception("Error inesperado al Agregar Articulo", ex);
        }

        //Devuelve TRUE en caso de exito y FALSE en caso contrario
        return flag;
    }
    
    public boolean AgregarArticulo(List<Articulo> lstArticulo) throws Exception{
        boolean flag = true;
        
        try{
            for(Articulo a : lstArticulo){
                if(!AgregarArticulo(a)){
                    flag = false;
                    break;
                }
            }
        }
        catch(Exception ex){
            flag = false;
            throw new Exception(ex.getMessage(), ex);
        }
        
        return flag;
    }
    
    public List<Articulo> GetAll() throws Exception{
       List<Articulo> lstArticulos = new ArrayList();
       try{
            //Trae todos los objetos del tipo Articulo
            ObjectSet<Articulo> result = db.query(Articulo.class); 
            //Carga una lista del tipo Articulo 
            for(Articulo a : result){
                lstArticulos.add(a);
            }
       }
       catch(Db4oException ex){
	       	DAOErrorLog.AgregarErrorLog("GetAll", "DAOArticulo", "Error de db4o: " + ex.getMessage());
	       	throw new Db4oException("Error de db4o al Traer Articulos", ex);
       }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOArticulo", ex.getMessage());
           throw new Exception("Error inesperado al Traer Articulos", ex);
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return lstArticulos;
    }
    
    public Articulo GetByCodigo(int codigo){
        
       try{
            ObjectSet result = db
                    .queryByExample(new Articulo(codigo));
            Articulo found = (Articulo) result.next();
            return found;
       }
       catch(Db4oException ex){
	       	DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOArticulo", "Error de db4o: " + ex.getMessage());
	       	throw new Db4oException("Error de db4o al Traer Articulo por codigo", ex);
      }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOArticulo", ex.getMessage());
           throw new Db4oException("Error inesperado al Traer Articulo por codigo", ex);
       }
    }
    
    /**
     * Lee el CSV articulos y genera una lista de objetos Articulo en memoria.
     * @return Lista de articulos importados.
     * @throws Exception 
     */
    public List<Articulo> ImportarArticulos() throws Exception{
        List<Articulo> lstArticulos = new ArrayList();
        
        BufferedReader br = null;
        String line = "";
        String error = "";
        
        try {
			br = new BufferedReader(new FileReader(Utiles.IMPORT_FILE_PATH_ARTICULOS));
			while ((line = br.readLine()) != null) {
	                        
				String[] articulo = line.split(Utiles.CSV_SPLIT_BY);
	                        // Crea un objeto articulo y lo agrega a la lista
	                        lstArticulos.add(new Articulo(Integer.parseInt(articulo[0]), 
	                                                      Double.valueOf(articulo[1]),
	                                                      articulo[2],
	                                                      Integer.parseInt(articulo[3]),
	                                                      articulo[4],
	                                                      articulo[5],
	                                                      Date.valueOf(articulo[6]),
	                                                      Date.valueOf(articulo[7])));
		}
 
		} catch (FileNotFoundException e) {
			error = "No se encontro el archivo Articulo: " + e.getStackTrace();
		} catch (IOException e) {
			error = "Error al leer el archivo Articulo: " + e.getStackTrace();
		} catch(Db4oException ex){
			error = "Error de db4o al importar datos de Articulo: " + ex.getStackTrace();
		} catch(Exception e){
            error = "Error inesperado al leer el archivo Articulo: " + e.getStackTrace();
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
	            DAOErrorLog.AgregarErrorLog("ImportarArticulos", "DAOArticulo", error);
	            throw new Exception(error);
	        }
	                
		}
        return lstArticulos;
    }
    
}
