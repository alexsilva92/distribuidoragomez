package com.gomez.bd.modelo;

import com.gomez.bd.modelo.PedidoCliente;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-05-13T18:56:38")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> nombre;
    public static volatile SingularAttribute<Usuario, String> direccion;
    public static volatile SingularAttribute<Usuario, String> cp;
    public static volatile ListAttribute<Usuario, PedidoCliente> pedidoClienteList;
    public static volatile SingularAttribute<Usuario, String> poblacion;
    public static volatile SingularAttribute<Usuario, String> razonSocial;
    public static volatile SingularAttribute<Usuario, String> apellidos;
    public static volatile SingularAttribute<Usuario, String> login;
    public static volatile SingularAttribute<Usuario, String> provincia;
    public static volatile SingularAttribute<Usuario, String> dni;
    public static volatile SingularAttribute<Usuario, String> password;
    public static volatile SingularAttribute<Usuario, String> salt;

}