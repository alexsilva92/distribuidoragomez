package com.gomez.bd.modelo;

import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.Producto;
import com.gomez.bd.modelo.TienePedidoClientePK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-05-11T18:14:00")
@StaticMetamodel(TienePedidoCliente.class)
public class TienePedidoCliente_ { 

    public static volatile SingularAttribute<TienePedidoCliente, PedidoCliente> pedidoCliente;
    public static volatile SingularAttribute<TienePedidoCliente, TienePedidoClientePK> tienePedidoClientePK;
    public static volatile SingularAttribute<TienePedidoCliente, Producto> producto1;
    public static volatile SingularAttribute<TienePedidoCliente, Integer> cantidad;

}