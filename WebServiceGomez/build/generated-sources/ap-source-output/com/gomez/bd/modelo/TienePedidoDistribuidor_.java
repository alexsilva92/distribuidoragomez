package com.gomez.bd.modelo;

import com.gomez.bd.modelo.PedidoDistribuidor;
import com.gomez.bd.modelo.Producto;
import com.gomez.bd.modelo.TienePedidoDistribuidorPK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-06-16T20:06:27")
@StaticMetamodel(TienePedidoDistribuidor.class)
public class TienePedidoDistribuidor_ { 

    public static volatile SingularAttribute<TienePedidoDistribuidor, TienePedidoDistribuidorPK> tienePedidoDistribuidorPK;
    public static volatile SingularAttribute<TienePedidoDistribuidor, PedidoDistribuidor> pedidoDistribuidor;
    public static volatile SingularAttribute<TienePedidoDistribuidor, Producto> producto1;
    public static volatile SingularAttribute<TienePedidoDistribuidor, Integer> cantidad;

}