/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import Negocio.Cliente;
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

public class DAOCliente {
    
    private ObjectContainer db;

    public DAOCliente(ObjectContainer db) {
        this.db = db;
    }
    
    public boolean AgregarCliente(Cliente cliente){
        boolean flag = true;
        try{
            //Graba el cliente recibido por parametro
            db.store(cliente);
            //Persistir los cambios
            db.commit();
        }
        catch(Exception ex){
            //Volver al estado anterior
            db.rollback();
            flag = false;
            //Graba log del error
            DAOErrorLog.AgregarErrorLog("AgregarCliente", "DAOCliente", ex.getMessage());
        }
        //Devuelve TRUE en caso de exito y FALSE en caso contrario
        return flag;
    }
    
    public boolean AgregarCliente(List<Cliente> lstCliente){
        boolean flag = true;
        
        try{
            for(Cliente c : lstCliente){
                if(!AgregarCliente(c)){
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
    
    public Cliente GetByCodigo(String codigo){
        
       try{
            ObjectSet result = db
                    .queryByExample(new Cliente(codigo));
            Cliente found = (Cliente) result.next();
            return found;
       }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOCliente", ex.getMessage());
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return null;
    }

    public List<Cliente> GetAll(){
       List<Cliente> lstClientes = new ArrayList();
       try{
            //Trae todos los objetos del tipo Cliente
            ObjectSet<Cliente> result = db.query(Cliente.class); 
            //Carga una lista del tipo Articulo 
            for(Cliente a : result){
                lstClientes.add(a);
            }
       }
       catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOCliente", ex.getMessage());
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return lstClientes;
    }
    
    public List<Cliente> ImportarClientes(){
        List<Cliente> lstClientes = new ArrayList();
        DAOCondicionPago daoCondicionPago = new DAOCondicionPago(this.db);
        BufferedReader br = null;
	String line = "";
        String error = "";
        
        try {
		br = new BufferedReader(new FileReader(Utiles.IMPORT_FILE_PATH_CLIENTES));
		while ((line = br.readLine()) != null) {
                        
			String[] cliente = line.split(Utiles.CSV_SPLIT_BY);
                        // Crea un objeto articulo y lo agrega a la lista
                        lstClientes.add(new Cliente(cliente[0], 
                                                    cliente[1],
                                                    cliente[2],
                                                    daoCondicionPago.GetByCodigo(Integer.parseInt(cliente[3]))));
 
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
                    DAOErrorLog.AgregarErrorLog("ImportarCliente", "DAOCliente", error);
                }
                
	}
        return lstClientes;
    }
    
}
