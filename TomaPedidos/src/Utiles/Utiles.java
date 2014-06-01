/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utiles;

import DAO.DAOErrorLog;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author m_pau_000
 */
public final class Utiles {
    //Ruta del archivo de la DB
    public static final String DB_FILE_PATH = System.getProperty("user.dir") + "/src/Data/TomaPedidosDB";
    //Ruta del archivo de importación
    public static final String IMPORT_FILE_PATH_ARTICULOS = System.getProperty("user.dir") + "/src/Data/Articulos.csv";
    public static final String IMPORT_FILE_PATH_CONDICIONESPAGO = System.getProperty("user.dir") + "/src/Data/CondicionesPago.csv";
    public static final String IMPORT_FILE_PATH_CLIENTES = System.getProperty("user.dir") + "/src/Data/Clientes.csv";
    public static final String IMPORT_FILE_PATH_PEDIDOS = System.getProperty("user.dir") + "/src/Data/Pedidos.csv";
    public static final String IMPORT_FILE_PATH_ESTADOS = System.getProperty("user.dir") + "/src/Data/Estados.csv";
    public static final String IMPORT_FILE_PATH_ARTICULOPEDIDO = System.getProperty("user.dir") + "/src/Data/ArticuloPedido.csv";
    //Ruta del log de errores
    public static final String ERRORLOG_FILE_PATH = System.getProperty("user.dir") + "/src/Data/ErrorLog.txt";
    //Caracter separador del CSV
    public static final String CSV_SPLIT_BY = ",";
    
    
    public static String CsvToJson(String filePath, String[] properties, String clase){
        BufferedReader br = null;
	String line = "";
        String error = "";
        String jsonString = "{\"" + clase + "\": [";
        
        try {
		br = new BufferedReader(new FileReader(filePath));
		while ((line = br.readLine()) != null) {
                        
			String[] valores = line.split(Utiles.CSV_SPLIT_BY);

                        jsonString += "{ ";
                        for(int i=0; i<valores.length; i++){
                            jsonString += "\"" + properties[i] + "\":\"" + valores[i] + "\"";
                            if((i+1) != valores.length){
                                jsonString += ", ";
                            }
                        }
                        jsonString += " }";
		}
                jsonString += " ]}";
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
        return jsonString;
    }
}
