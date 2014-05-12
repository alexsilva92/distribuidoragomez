/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gomez.webservice;


import com.gomez.bd.controller.DistribuidorJpaController;
import com.gomez.bd.controller.PedidoClienteJpaController;
import com.gomez.bd.controller.StockJpaController;
import com.gomez.bd.controller.UsuarioJpaController;
import com.gomez.bd.controller.exceptions.NonexistentEntityException;
import com.gomez.bd.controller.exceptions.RollbackFailureException;
import com.gomez.bd.modelo.Distribuidor;
import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Alex
 */
@WebService(serviceName = "GestorBD")
public class GestorBD {
    @PersistenceContext(unitName = "WebServiceGomezPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    private UsuarioJpaController usuarioController;
    private PedidoClienteJpaController pedidoClienteController;
    private DistribuidorJpaController distribuidorController;
    private StockJpaController stockController;

    private UsuarioJpaController getUsuarioController(){
        if(usuarioController == null){
            usuarioController = new UsuarioJpaController(utx,
                    em.getEntityManagerFactory());
        }
        return usuarioController;
    }
    
    private PedidoClienteJpaController getPedidoClienteController(){
        if(pedidoClienteController == null){
            pedidoClienteController = new PedidoClienteJpaController(utx,
                    em.getEntityManagerFactory());
        }
        return pedidoClienteController;
    }
    
    private DistribuidorJpaController getDistribuidorController(){
        if(distribuidorController == null){
            distribuidorController = new DistribuidorJpaController(utx,
                    em.getEntityManagerFactory());
        }
        return distribuidorController;
    }
    
    private StockJpaController getStockController(){
        if(stockController == null){
            stockController = new StockJpaController(utx,
                    em.getEntityManagerFactory());
        }
        return stockController;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "login")
    public boolean login(@WebParam(name = "login") String login, 
    @WebParam(name = "password") String password) {
        return getUsuarioController().login(login, password);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "pedidosPorEmpleado")
    public java.util.List<com.gomez.bd.modelo.PedidoCliente> 
    pedidosPorEmpleado(@WebParam(name = "empleado") final String empleado) {
        return getPedidoClienteController().getPedidosPorEmpleado(empleado);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getPedidoCliente")
    public PedidoCliente getPedidoCliente(@WebParam(name = "id") final int id) {
        return getPedidoClienteController().findPedidoCliente(id);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "setPedidoCliente")
    public Boolean setPedidoCliente(@WebParam(name = "pedido") 
    final PedidoCliente pedido) {
        try {
            getPedidoClienteController().edit(pedido);
            return true;
        } catch (NonexistentEntityException | RollbackFailureException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getCliente")
    public Usuario getCliente(@WebParam(name = "cliente") 
    final String cliente) {
        return getUsuarioController().findUsuario(cliente);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getClientes")
    public java.util.List<com.gomez.bd.modelo.Usuario> getClientes() {
        return getUsuarioController().findUsuarioEntities();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getDistribuidor")
    public Distribuidor getDistribuidor(@WebParam(name = "distribuidor") 
    final String distribuidor) {
        return getDistribuidorController().findDistribuidor(distribuidor);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getDistribuidores")
    public java.util.List<com.gomez.bd.modelo.Distribuidor> getDistribuidores(){
        return getDistribuidorController().findDistribuidorEntities();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getStocks")
    public java.util.List<com.gomez.bd.modelo.Stock> getStocks() {
        return getStockController().findStockEntities();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addUsuario")
    public Boolean addUsuario(@WebParam(name = "usuario") final Usuario usuario) {
        try {
            getUsuarioController().create(usuario);
            return true;
        } catch (RollbackFailureException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetPedidosCliente")
    public java.util.List<com.gomez.bd.modelo.PedidoCliente> GetPedidosCliente(@WebParam(name = "cliente") final String cliente) {
        return getPedidoClienteController().getPedidosPorCliente(cliente);
    }
    
    
     
}
