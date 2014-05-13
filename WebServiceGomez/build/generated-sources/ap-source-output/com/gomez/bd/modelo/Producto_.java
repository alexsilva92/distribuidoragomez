package com.gomez.bd.modelo;

import com.gomez.bd.modelo.Stock;
import com.gomez.bd.modelo.TieneDistribuidor;
import com.gomez.bd.modelo.TienePedidoCliente;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-05-13T16:58:44")
@StaticMetamodel(Producto.class)
public class Producto_ { 

    public static volatile SingularAttribute<Producto, String> codigo;
    public static volatile SingularAttribute<Producto, String> nombre;
    public static volatile SingularAttribute<Producto, String> categoria;
    public static volatile SingularAttribute<Producto, String> subacategoria;
    public static volatile SingularAttribute<Producto, String> imagen;
    public static volatile SingularAttribute<Producto, Stock> stock;
    public static volatile SingularAttribute<Producto, Float> precio;
    public static volatile ListAttribute<Producto, TieneDistribuidor> tieneDistribuidorList;
    public static volatile ListAttribute<Producto, TienePedidoCliente> tienePedidoClienteList;

}