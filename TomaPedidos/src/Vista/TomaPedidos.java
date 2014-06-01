/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import DAO.DAOArticulo;
import DAO.DAOArticuloPedido;
import DAO.DAOCliente;
import DAO.DAOCondicionPago;
import DAO.DAOEstado;
import DAO.DAOGeneric;
import DAO.DAOPedido;
import Negocio.Articulo;
import Negocio.ArticuloPedido;
import Negocio.Cliente;
import Negocio.CondicionPago;
import Negocio.Estado;
import Negocio.Pedido;
import com.db4o.ObjectContainer;
import java.util.List;

/**
 *
 * @author m_pau_000
 */
public class TomaPedidos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//BORRA LA DB PARA NO DUPLICAR
        DAOGeneric.BorrarDB();
        //ABRE LA DB
        ObjectContainer db = DAOGeneric.AbrirDB();
        
        DAOArticulo daoArticulo = new DAOArticulo(db);
        List<Articulo> lst = daoArticulo.ImportarArticulos();
        daoArticulo.AgregarArticulo(lst);
        
        System.out.println("\n********     ARTICULOS     *********\n");
        
        for(Articulo a : daoArticulo.GetAll()){
            System.out.println(a.toString());
        }
        
        DAOCondicionPago daoCondicionPago = new DAOCondicionPago(db);
        List<CondicionPago> lstCondicion = daoCondicionPago.ImportarCondicionPago();
        daoCondicionPago.AgregarCondicionPago(lstCondicion);
        
        System.out.println("\n********     CONDICION DE PAGO     *********\n");
        
        for(CondicionPago c : daoCondicionPago.GetAll()){
            System.out.println(c.toString());
        }
        
        DAOCliente daoCliente = new DAOCliente(db);
        List<Cliente> lstCliente = daoCliente.ImportarClientes();
        daoCliente.AgregarCliente(lstCliente);
        
        System.out.println("\n********     CLIENTES     *********\n");
        
        for(Cliente c : daoCliente.GetAll()){
            System.out.println(c.toString());
        }
        
        DAOEstado daoEstado = new DAOEstado(db);
        List<Estado> lstEstado = daoEstado.ImportarEstado();
        daoEstado.AgregarEstado(lstEstado);
        
        System.out.println("\n********     ESTADOS DE PEDIDO     *********\n");
        
        for(Estado e : daoEstado.GetAll()){
            System.out.println(e.toString());
        }
        
        DAOPedido daoPedido = new DAOPedido(db);
        List<Pedido> lstPedido = daoPedido.ImportarPedidos();
        daoPedido.AgregarPedido(lstPedido);
        
        System.out.println("\n********     PEDIDOS     *********\n");
        
        for(Pedido p : daoPedido.GetAll()){
            System.out.println(p.toString());
        }
        
        DAOArticuloPedido daoArticuloPedido = new DAOArticuloPedido(db);
        List<ArticuloPedido> lstArticuloPedido = daoArticuloPedido.ImportarArticuloPedido();
        daoArticuloPedido.AgregarArticuloPedido(lstArticuloPedido);
        
        System.out.println("\n********     ARTICULOS POR PEDIDO     *********\n");
        
        for(ArticuloPedido ap : daoArticuloPedido.GetAll()){
            System.out.println(ap.toString());
        }
        
        //CIERRA LA DB
        DAOGeneric.CerrarDB(db);
        
    }
    
}
