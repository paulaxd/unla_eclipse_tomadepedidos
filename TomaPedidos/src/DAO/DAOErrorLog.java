/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import Utiles.Utiles;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/***
 * Clase para la persistencia del LOG de errores.
 * @author m_pau_000
 */
public class DAOErrorLog {
    
    /***
     * TO-DO: Cambiar este método para que grabe los errores en un archivo de texto.
     * De esa forma si la bD tiene algun problema se puede registrar el error.
     * Método que agrega un Articulo
     * @param metodo El metodo donde se dio el error.
     * @param clase La clase donde se dio el error.
     * @param mensaje El mensaje de error.
     * @return True en caso de exito. False en caso de error.
     */
    public static boolean AgregarErrorLog(String metodo, String clase, String mensaje){
        Calendar calendario = new GregorianCalendar();
        boolean flag = true;
        try{
            File file = new File(Utiles.ERRORLOG_FILE_PATH);
            FileWriter fstream = null;
            if(!file.exists()){
                fstream = new FileWriter(Utiles.ERRORLOG_FILE_PATH);
            }
            else{
                fstream = new FileWriter(Utiles.ERRORLOG_FILE_PATH, true);
            }
            
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(calendario.getTime().toLocaleString() + " - Metodo: " + metodo + " - Clase: " + clase + " - Mensaje: " + mensaje);
            out.newLine();

            out.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }finally{
            
        }
        return flag;
    }
    
    public static List<String> GetAll(){
        BufferedReader br = null;
        List<String> errores = new ArrayList();
        try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader(Utiles.ERRORLOG_FILE_PATH));
                while ((sCurrentLine = br.readLine()) != null) {
                    errores.add(sCurrentLine);
                }

        } catch (IOException e) {
                e.printStackTrace();
        } finally {
                try {
                        if (br != null)br.close();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
        }
        return errores;
    }
}
