/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import java.io.File;
import Utiles.Utiles;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;

/**
 * Clase con metodos genericos de la DB.
 * @author m_pau_000
 */
public class DAOGeneric {
    
    /***
     * Borra el archivo de la DB
     * @return True en caso de éxito. False en caso contrario.
     */
    public static boolean BorrarDB(){
        boolean flag = true;
        try{
            File file = new File(Utiles.DB_FILE_PATH);
            flag = file.delete();
        }
        catch(Exception ex){
            flag = false;
            DAOErrorLog.AgregarErrorLog("BorrarDB", "DAOGeneric", ex.getMessage());
        }
        return flag;
    }
    
    public static ObjectContainer AbrirDB(){
        try{
            return Db4o.openFile(Utiles.DB_FILE_PATH);
        }
        catch(Exception ex){
            DAOErrorLog.AgregarErrorLog("AbrirDB", "DAOGeneric", ex.getMessage());
        }
        return null;
    }
    
    public static void CerrarDB(ObjectContainer db){
        try{
            db.close();
        }
        catch(Exception ex){
            DAOErrorLog.AgregarErrorLog("CerrarDB", "DAOGeneric", ex.getMessage());
        }
    }
    
}
