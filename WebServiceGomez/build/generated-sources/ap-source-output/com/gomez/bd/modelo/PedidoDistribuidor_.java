package com.gomez.bd.modelo;

import com.gomez.bd.modelo.Distribuidor;
import com.gomez.bd.modelo.Empleado;
import com.gomez.bd.modelo.EstadoPedido;
import com.gomez.bd.modelo.TienePedidoDistribuidor;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-06-16T20:06:27")
@StaticMetamodel(PedidoDistribuidor.class)
public class PedidoDistribuidor_ { 

    public static volatile ListAttribute<PedidoDistribuidor, TienePedidoDistribuidor> tienePedidoDistribuidorList;
    public static volatile SingularAttribute<PedidoDistribuidor, Integer> idPedido;
    public static volatile SingularAttribute<PedidoDistribuidor, Date> fechaLlegada;
    public static volatile SingularAttribute<PedidoDistribuidor, Distribuidor> distribuidor;
    public static volatile SingularAttribute<PedidoDistribuidor, EstadoPedido> estado;
    public static volatile SingularAttribute<PedidoDistribuidor, Empleado> empleado;

}