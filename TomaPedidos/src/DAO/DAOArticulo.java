package DAO;

import Negocio.Articulo;
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

/**
 *
 * @author m_pau_000
 */
public class DAOArticulo {
    
    private ObjectContainer db;

    public DAOArticulo(ObjectContainer db) {
        this.db = db;
    }
    
    public boolean AgregarArticulo(Articulo articulo){
        boolean flag = true;
        try{
            //Graba el articulo recibido por parametro
            db.store(articulo);
            //Persistir los cambios
            db.commit();
        }
        catch(Exception ex){
            //Volver al estado anterior
            db.rollback();
            flag = false;
            //Graba log del error
            DAOErrorLog.AgregarErrorLog("AgregarArticulo", "DAOArticulo", ex.getMessage());
        }

        //Devuelve TRUE en caso de exito y FALSE en caso contrario
        return flag;
    }
    
    public boolean AgregarArticulo(List<Articulo> lstArticulo){
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
        }
        
        return flag;
    }
    
    public List<Articulo> GetAll(){
       List<Articulo> lstArticulos = new ArrayList();
       try{
            //Trae todos los objetos del tipo Articulo
            ObjectSet<Articulo> result = db.query(Articulo.class); 
            //Carga una lista del tipo Articulo 
            for(Articulo a : result){
                lstArticulos.add(a);
            }
       }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOArticulo", ex.getMessage());
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
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOArticulo", ex.getMessage());
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return null;
    }
    
    /**
     * Lee el CSV articulos y genera una lista de objetos Articulo en memoria.
     * @return Lista de articulos importados.
     */
    public List<Articulo> ImportarArticulos(){
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
                    DAOErrorLog.AgregarErrorLog("ImportarArticulos", "DAOArticulo", error);
                }
                
	}
        return lstArticulos;
    }
    
}
