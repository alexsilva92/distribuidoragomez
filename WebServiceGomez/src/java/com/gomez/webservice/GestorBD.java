/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gomez.webservice;

import com.gomez.bd.bean.ProductoBean;
import com.gomez.bd.bean.StockBean;
import com.gomez.bd.bean.ClienteBean;
import com.gomez.bd.bean.DistribuidorBean;
import com.gomez.bd.bean.EmpleadoBean;
import com.gomez.bd.bean.PedidoClienteBean;
import com.gomez.bd.controller.ClienteJpaController;
import com.gomez.bd.controller.DistribuidorJpaController;
import com.gomez.bd.controller.EmpleadoJpaController;
import com.gomez.bd.controller.PedidoClienteJpaController;
import com.gomez.bd.controller.StockJpaController;
import com.gomez.bd.controller.TieneDistribuidorJpaController;
import com.gomez.bd.controller.exceptions.NonexistentEntityException;
import com.gomez.bd.controller.exceptions.RollbackFailureException;
import com.gomez.bd.modelo.Cliente;
import com.gomez.bd.modelo.Distribuidor;
import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.Stock;
import com.gomez.bd.modelo.TieneDistribuidor;
import com.gomez.bd.modelo.TienePedidoCliente;
import com.gomez.bd.modelo.TienePedidoClientePK;
import java.util.ArrayList;
import java.util.List;
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

    private ClienteJpaController clienteController;
    private PedidoClienteJpaController pedidoClienteController;
    private DistribuidorJpaController distribuidorController;
    private StockJpaController stockController;
    private EmpleadoJpaController empleadoController;
    private TieneDistribuidorJpaController tieneDistribuidorController;

    private ClienteJpaController getClienteController(){
        if(clienteController == null){
            clienteController = new ClienteJpaController(utx,
                    em.getEntityManagerFactory());
        }
        return clienteController;
    }
    
    private EmpleadoJpaController getEmpleadoController(){
        if(empleadoController == null){
            empleadoController = new EmpleadoJpaController(utx,
                    em.getEntityManagerFactory());
        }
        return empleadoController;
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
    
    private TieneDistribuidorJpaController getTieneDistribuidorController(){
        if(tieneDistribuidorController == null){
            tieneDistribuidorController = new TieneDistribuidorJpaController(utx,
                    em.getEntityManagerFactory());
        }
        return tieneDistribuidorController;
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
    @WebMethod(operationName = "loginCliente")
    public boolean loginCliente(@WebParam(name = "login") String login, 
    @WebParam(name = "password") String password) {
        return getClienteController().login(login, password);
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "loginEmpleado")
    public boolean loginEmpleado(@WebParam(name = "login") String login, 
    @WebParam(name = "password") String password) {
        return getEmpleadoController().login(login, password);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "pedidosPorEmpleado")
    public java.util.List<com.gomez.bd.bean.PedidoClienteBean> 
    pedidosPorEmpleado(@WebParam(name = "empleado") final String empleado) {
        List<PedidoClienteBean> pedidosBean = new ArrayList<PedidoClienteBean>();
        List<PedidoCliente> pedidos = getPedidoClienteController().getPedidosPorEmpleado(empleado);
        PedidoClienteBean pedidoBean;
        for(PedidoCliente pedido: pedidos){
            pedidoBean = getPedidoClienteBean(pedido);
            pedidosBean.add(pedidoBean);
        }
        
        return pedidosBean;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getPedidoCliente")
    public PedidoClienteBean getPedidoCliente(@WebParam(name = "id") final int id) {
        PedidoCliente pedido = getPedidoClienteController().findPedidoCliente(id);
        PedidoClienteBean pedidoBean = null;
        if(pedido != null){
            pedidoBean = getPedidoClienteBean(pedido);
        }
        return pedidoBean;
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
    public ClienteBean getCliente(@WebParam(name = "cliente") 
    final String cliente) {
        Cliente _cliente = getClienteController().findCliente(cliente);
        ClienteBean clienteBean = null;
        if(_cliente != null);{
            clienteBean = new ClienteBean();
            clienteBean.setDni(_cliente.getDni());
            clienteBean.setNombre(_cliente.getNombre());
            clienteBean.setApellidos(_cliente.getApellidos());
            clienteBean.setLogin(_cliente.getLogin());
            clienteBean.setRazonSocial(_cliente.getRazonSocial());
            clienteBean.setCp(_cliente.getCp());
            clienteBean.setDireccion(_cliente.getDireccion());
        }
        
        return clienteBean;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getClientes")
    public java.util.List<com.gomez.bd.bean.ClienteBean> getClientes() {
        List<Cliente> clientes = getClienteController().findClienteEntities();
        List<ClienteBean> clientesBean = new ArrayList<>();
        ClienteBean clienteBean;
        for(Cliente cliente: clientes){
            clienteBean = new ClienteBean();
            
            clienteBean.setDni(cliente.getDni());
            clienteBean.setNombre(cliente.getNombre());
            clienteBean.setApellidos(cliente.getApellidos());
            clienteBean.setLogin(cliente.getLogin());
            clienteBean.setRazonSocial(cliente.getRazonSocial());
            clienteBean.setCp(cliente.getCp());
            clienteBean.setDireccion(cliente.getDireccion());
            
            clientesBean.add(clienteBean);
        }
        return clientesBean;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getDistribuidor")
    public DistribuidorBean getDistribuidor(@WebParam(name = "distribuidor") 
    final String distribuidor) {
        Distribuidor _distribuidor = getDistribuidorController().findDistribuidor(distribuidor);
        DistribuidorBean distribuidorBean = null;
         
        if(distribuidor != null){
            distribuidorBean = new DistribuidorBean();
            distribuidorBean.setCifNif(_distribuidor.getCifNif());
            distribuidorBean.setRazonSocial(_distribuidor.getRazonSocial());
            distribuidorBean.setTelefono(_distribuidor.getTelefono());
        }
        
        return distribuidorBean;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getDistribuidores")
    public java.util.List<com.gomez.bd.bean.DistribuidorBean> getDistribuidores(){
        List<Distribuidor> distribuidores =
                getDistribuidorController().findDistribuidorEntities();
        List<DistribuidorBean> distribuidoresBean = new ArrayList<>();
        DistribuidorBean distribuidorBean;
        for(Distribuidor distribuidor: distribuidores){
            distribuidorBean = new DistribuidorBean();
            
            distribuidorBean.setCifNif(distribuidor.getCifNif());
            distribuidorBean.setRazonSocial(distribuidor.getRazonSocial());
            distribuidorBean.setTelefono(distribuidor.getTelefono());
            
            distribuidoresBean.add(distribuidorBean);
        }
        
        return distribuidoresBean;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getStocks")
    public java.util.List<com.gomez.bd.bean.StockBean> getStocks() {
        List<StockBean> stocks = new ArrayList<StockBean>();
        StockBean stockBean;
        for(Stock stock : getStockController().findStockEntities()){
            stockBean = getStockBean(stock);
            stocks.add(stockBean);
        }
        
        return stocks;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetPedidosCliente")
    public java.util.List<com.gomez.bd.bean.PedidoClienteBean> GetPedidosCliente(@WebParam(name = "cliente") final String cliente) {
        List<PedidoClienteBean> pedidosBean = new ArrayList<PedidoClienteBean>();
        List<PedidoCliente> pedidos = getPedidoClienteController().getPedidosPorCliente(cliente);
        PedidoClienteBean pedidoBean;
        for(PedidoCliente pedido: pedidos){
            pedidoBean = getPedidoClienteBean(pedido);
            pedidosBean.add(pedidoBean);
        }
        
        return pedidosBean;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addCliente")
    public Boolean addCliente(@WebParam(name = "usuario") ClienteBean _cliente) {
        try {
            Cliente cliente = new Cliente();
            cliente.setDni(_cliente.getDni());
            cliente.setNombre(_cliente.getNombre());
            cliente.setApellidos(_cliente.getApellidos());
            cliente.setPassword(_cliente.getPassword());
            cliente.setRazonSocial(_cliente.getRazonSocial());
            cliente.setLogin(_cliente.getLogin());
            
            getClienteController().create(cliente);
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
    @WebMethod(operationName = "GetStocksPorId")
    public java.util.List<com.gomez.bd.bean.StockBean> GetStocksPorId(@WebParam(name = "ids") java.util.List<String> ids) {
        List<StockBean> stocks = new ArrayList<>();
        Stock stock;
        StockBean stockBean;
        for(String id: ids){
            stock = getStockController().findStock(id);
            stockBean = getStockBean(stock);
            stocks.add(stockBean);
        }
        return stocks;
    }
    
    /**
     * Web service operation
     */
    //@WebMethod(operationName = "getProductosDistribuidor")
    /*public java.util.List<com.gomez.bd.bean.ProductoBean> getProductosDistribuidor(@WebParam(name = "distribuidor") String distribuidor) {
        List<TieneDistribuidor> productos = getTieneDistribuidorController().getProductos(distribuidor);
        List<ProductoBean> productosBean = new ArrayList<>();
        
        ProductoBean productoBean;
        for(TieneDistribuidor producto: productos){
            productoBean = new ProductoBean();
            
            productoBean.setNombre(producto.getProducto1().getNombre());
            productoBean.setCodigo(producto.getProducto1().getCodigo());
            productoBean.setPrecio(producto.getProducto1().getPrecio());
            productoBean.setMarca(producto.getProducto1().getMarca().getMarcaProducto());
            productoBean.setCategoria(producto.getProducto1().getMarca().getCategoria());
            productoBean.setImagen(producto.getProducto1().getImagen());
            
            productosBean.add(productoBean);
        }
        
        return productosBean;
    }*/
    
    private PedidoClienteBean getPedidoClienteBean(PedidoCliente pedido){
        PedidoClienteBean pedidoBean;
        ClienteBean clienteBean;
        EmpleadoBean empleadoBean;
        StockBean stockBean;
        ProductoBean productoBean;
        
        pedidoBean = new PedidoClienteBean();
            
        clienteBean = new ClienteBean();
        clienteBean.setDni(pedido.getCliente().getDni());
        clienteBean.setLogin(pedido.getCliente().getLogin());
        clienteBean.setRazonSocial(pedido.getCliente().getRazonSocial());
        clienteBean.setNombre(pedido.getCliente().getNombre());
        clienteBean.setApellidos(pedido.getCliente().getApellidos());

        empleadoBean = new EmpleadoBean();
        empleadoBean.setDni(pedido.getEmpleado().getDni());
        empleadoBean.setLogin(pedido.getEmpleado().getLogin());
        empleadoBean.setNombre(pedido.getEmpleado().getNombre());
        empleadoBean.setApellidos(pedido.getEmpleado().getApellidos());

        pedidoBean.setIdPedido(pedido.getIdPedido());
        pedidoBean.setCliente(clienteBean);
        pedidoBean.setEmpleado(empleadoBean);
        pedidoBean.setFechaLlegada(pedido.getFechaLllegada());
        pedidoBean.setEstado(pedido.getEstado().getEstado());

        List<StockBean> productos = new ArrayList<>();
        for(TienePedidoCliente stock: pedido.getTienePedidoClienteList()){
            stockBean = new StockBean();

            productoBean = new ProductoBean();
            productoBean.setCodigo(stock.getProducto1().getCodigo());
            productoBean.setNombre(stock.getProducto1().getNombre());
            productoBean.setMarca(stock.getProducto1().getMarca().getMarcaProducto());
            productoBean.setCategoria(stock.getProducto1().getMarca().getCategoria());
            productoBean.setImagen(stock.getProducto1().getImagen());
            productoBean.setPrecio(stock.getProducto1().getPrecio());

            stockBean.setProducto(productoBean);
            stockBean.setCantidad(stock.getCantidad());


            productos.add(stockBean);
        }

        pedidoBean.setProductos(productos);
        
        return pedidoBean;
    }
    

    private StockBean getStockBean(Stock stock){
        StockBean stockBean = new StockBean();
        ProductoBean productoBean = new ProductoBean();

        productoBean.setCodigo(stock.getProducto1().getCodigo());
        productoBean.setNombre(stock.getProducto1().getNombre());
        productoBean.setCategoria(stock.getProducto1().getMarca().getCategoria());
        productoBean.setMarca(stock.getProducto1().getMarca().getMarcaProducto());
        productoBean.setImagen(stock.getProducto1().getImagen());
        productoBean.setPrecio(stock.getProducto1().getPrecio());

        stockBean.setCantidad(stock.getCantidad());
        stockBean.setProducto(productoBean);
        
        return stockBean;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addPedidoCliente")
    public Boolean addPedidoCliente(@WebParam(name = "pedidoBean") PedidoClienteBean pedidoBean) {
        PedidoCliente pedido;
        Cliente cliente ;
        TienePedidoCliente tienePedido;
        
        pedido = new PedidoCliente();
        cliente = new Cliente();
        cliente.setDni(pedidoBean.getCliente().getDni());
        
        pedido.setCliente(cliente);
        pedido.setFechaLllegada(pedidoBean.getFechaLlegada());

        List<TienePedidoCliente> productos = new ArrayList<>();
        for(StockBean stock: pedidoBean.getProductos()){
            tienePedido = new TienePedidoCliente();
            tienePedido.setTienePedidoClientePK(new TienePedidoClientePK());
            tienePedido.getTienePedidoClientePK().
                    setProducto(stock.getProducto().getCodigo());
            tienePedido.setCantidad(stock.getCantidad());


            productos.add(tienePedido);
        }

        pedido.setTienePedidoClienteList(productos);
        try {
            getPedidoClienteController().create(pedido);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
   
}
