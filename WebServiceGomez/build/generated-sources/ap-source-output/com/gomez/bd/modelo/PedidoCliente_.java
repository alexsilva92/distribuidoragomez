package com.gomez.bd.modelo;

import com.gomez.bd.modelo.Empleado;
import com.gomez.bd.modelo.EstadoPedido;
import com.gomez.bd.modelo.TienePedidoCliente;
import com.gomez.bd.modelo.Usuario;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-05-11T18:14:00")
@StaticMetamodel(PedidoCliente.class)
public class PedidoCliente_ { 

    public static volatile SingularAttribute<PedidoCliente, Integer> idPedido;
    public static volatile SingularAttribute<PedidoCliente, EstadoPedido> estado;
    public static volatile SingularAttribute<PedidoCliente, Empleado> empleado;
    public static volatile SingularAttribute<PedidoCliente, Usuario> cliente;
    public static volatile SingularAttribute<PedidoCliente, Date> fechaLllegada;
    public static volatile ListAttribute<PedidoCliente, TienePedidoCliente> tienePedidoClienteList;

}