package com.gomez.bd.modelo;

import com.gomez.bd.modelo.PedidoDistribuidor;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-05-11T18:14:00")
@StaticMetamodel(Distribuidor.class)
public class Distribuidor_ { 

    public static volatile ListAttribute<Distribuidor, PedidoDistribuidor> pedidoDistribuidorList;
    public static volatile SingularAttribute<Distribuidor, String> razonSocial;
    public static volatile SingularAttribute<Distribuidor, String> telefono;
    public static volatile SingularAttribute<Distribuidor, String> cifNif;

}