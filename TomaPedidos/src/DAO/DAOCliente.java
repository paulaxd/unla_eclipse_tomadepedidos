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
import com.db4o.ext.Db4oException;

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
    
    public boolean AgregarCliente(Cliente cliente) throws Exception{
        boolean flag = true;
        try{
            //Graba el cliente recibido por parametro
            db.store(cliente);
        } catch(Db4oException ex){
        	DAOErrorLog.AgregarErrorLog("AgregarCliente", "DAOCliente", "Error de db4o: " + ex.getMessage());
        	throw new Db4oException("Error de db4o al Agregar Cliente", ex);
        } catch(Exception ex){
            flag = false;
            //Graba log del error
            DAOErrorLog.AgregarErrorLog("AgregarCliente", "DAOCliente", ex.getMessage());
            throw new Exception("Error inesperado al Agregar Cliente", ex);
        }
        //Devuelve TRUE en caso de exito y FALSE en caso contrario
        return flag;
    }
    
    public boolean AgregarCliente(List<Cliente> lstCliente) throws Exception{
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
            throw new Exception(ex.getMessage(), ex);
        }
        
        return flag;
    }
    
    public Cliente GetByCodigo(String codigo) throws Exception{
        
       try{
            ObjectSet result = db.queryByExample(new Cliente(codigo));
            Cliente found = (Cliente) result.next();
            return found;
       } catch(Db4oException ex){
	       	DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOCliente", "Error de db4o: " + ex.getMessage());
	       	throw new Db4oException("Error de db4o al Traer Cliente por Codigo", ex);
       } catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetByCodigo", "DAOCliente", ex.getMessage());
           throw new Exception("Error inesperado al Traer Cliente por Codigo", ex);
       }
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
       } catch(Db4oException ex){
	       	DAOErrorLog.AgregarErrorLog("GetAll", "DAOCliente", "Error de db4o: " + ex.getMessage());
	       	throw new Db4oException("Error de db4o al Traer Clientes", ex);
       } catch(Exception ex){
           //Graba un log de errores en la DB
           DAOErrorLog.AgregarErrorLog("GetAll", "DAOCliente", ex.getMessage());
       }
       //Devuelvo la lista cargada (o vacía en caso de excepcion)
       return lstClientes;
    }
    
    public List<Cliente> ImportarClientes() throws Exception{
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
		} catch(Db4oException ex){
			error = "Error de db4o al importar datos de Cliente: " + ex.getMessage();
		} catch(Exception e){
            error = "Error al leer el archivo: " + e.getMessage();
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
                throw new Exception(error);
            }    
        }
        return lstClientes;
    }
    
}
