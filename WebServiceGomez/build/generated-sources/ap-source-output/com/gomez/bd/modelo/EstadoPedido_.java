package com.gomez.bd.modelo;

import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.PedidoDistribuidor;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-05-26T16:27:55")
@StaticMetamodel(EstadoPedido.class)
public class EstadoPedido_ { 

    public static volatile SingularAttribute<EstadoPedido, String> estado;
    public static volatile ListAttribute<EstadoPedido, PedidoCliente> pedidoClienteList;
    public static volatile ListAttribute<EstadoPedido, PedidoDistribuidor> pedidoDistribuidorList;

}