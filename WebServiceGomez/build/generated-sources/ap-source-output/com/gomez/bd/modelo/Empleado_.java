package com.gomez.bd.modelo;

import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.PedidoDistribuidor;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-06-16T13:16:16")
@StaticMetamodel(Empleado.class)
public class Empleado_ { 

    public static volatile SingularAttribute<Empleado, String> nombre;
    public static volatile ListAttribute<Empleado, PedidoCliente> pedidoClienteList;
    public static volatile ListAttribute<Empleado, PedidoDistribuidor> pedidoDistribuidorList;
    public static volatile SingularAttribute<Empleado, String> apellidos;
    public static volatile SingularAttribute<Empleado, String> login;
    public static volatile SingularAttribute<Empleado, String> dni;
    public static volatile SingularAttribute<Empleado, String> password;

}