package com.gomez.bd.modelo;

import com.gomez.bd.modelo.PedidoCliente;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-05-26T16:27:55")
@StaticMetamodel(Cliente.class)
public class Cliente_ { 

    public static volatile SingularAttribute<Cliente, String> nombre;
    public static volatile SingularAttribute<Cliente, String> direccion;
    public static volatile SingularAttribute<Cliente, String> cp;
    public static volatile ListAttribute<Cliente, PedidoCliente> pedidoClienteList;
    public static volatile SingularAttribute<Cliente, String> poblacion;
    public static volatile SingularAttribute<Cliente, String> apellidos;
    public static volatile SingularAttribute<Cliente, String> razonSocial;
    public static volatile SingularAttribute<Cliente, String> login;
    public static volatile SingularAttribute<Cliente, String> provincia;
    public static volatile SingularAttribute<Cliente, String> dni;
    public static volatile SingularAttribute<Cliente, String> password;

}