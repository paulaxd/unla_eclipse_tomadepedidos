/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import Utiles.Utiles;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ext.Db4oException;

/**
 * Clase con metodos genericos de la DB.
 * @author m_pau_000
 */
public class DAOManager {
    
	private ObjectContainer db;
	
	public DAOManager() throws Exception{
		this.db = this.AbrirDB();
	}
	
	public ObjectContainer GetObjectContainter(){
		return this.db;
	}
	
    /***
     * Borra el archivo de la DB
     * @return True en caso de exito. False en caso contrario.
     */
    public static boolean BorrarDB(){
        boolean flag = true;
        try{
            File file = new File(Utiles.DB_FILE_PATH);
            flag = file.delete();
        }
        catch(Exception ex){
            flag = false;
            DAOErrorLog.AgregarErrorLog("BorrarDB", "DAOManager", ex.getMessage());
        }
        return flag;
    }
    
    public ObjectContainer AbrirDB() throws Exception{
        try{
            return Db4o.openFile(Utiles.DB_FILE_PATH);
        }
        catch(Db4oException ex){
        	DAOErrorLog.AgregarErrorLog("AbrirDB", "DAOManager", "Error de db4o: " + ex.getMessage());
        	throw new Exception("Error de db4o al Abrir DB", ex);
        }
        catch(Exception ex){
            DAOErrorLog.AgregarErrorLog("AbrirDB", "DAOManager", "Error inesperado: " + ex.getMessage());
            throw new Exception("Error inesperado al Abrir DB", ex);
        }
    }
    
    public void CerrarDB() throws Exception{
        try{
            this.db.close();
        }
        catch(Db4oException ex){
        	DAOErrorLog.AgregarErrorLog("CerrarDB", "DAOManager", "Error de db4o: " + ex.getMessage());
        	throw new Exception("Error de db4o al Cerrar DB", ex);
        }
        catch(Exception ex){
            DAOErrorLog.AgregarErrorLog("CerrarDB", "DAOManager", "Error inesperado" + ex.getMessage());
            throw new Exception("Error inesperado al Cerrar DB", ex);
        }
    }
    
    public void Commit(){
    	try{
            this.db.commit();
        }
    	catch(Db4oException ex){
        	DAOErrorLog.AgregarErrorLog("Commit", "DAOManager", "Error de db4o: " + ex.getMessage());
        }
        catch(Exception ex){
            DAOErrorLog.AgregarErrorLog("Commit", "DAOManager", "Error inesperado: " + ex.getMessage());
        }
    }
    
    public void Rollback(){
    	try{
            this.db.rollback();
        }
    	catch(Db4oException ex){
        	DAOErrorLog.AgregarErrorLog("Rollback", "DAOManager", "Error de db4o: " + ex.getMessage());
        }
        catch(Exception ex){
            DAOErrorLog.AgregarErrorLog("Rollback", "DAOManager", "Error inesperado: " + ex.getMessage());
        }
    }
    
}
