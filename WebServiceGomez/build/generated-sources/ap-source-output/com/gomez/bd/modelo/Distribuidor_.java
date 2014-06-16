package com.gomez.bd.modelo;

import com.gomez.bd.modelo.TieneDistribuidor;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-06-16T13:16:16")
@StaticMetamodel(Distribuidor.class)
public class Distribuidor_ { 

    public static volatile SingularAttribute<Distribuidor, String> razonSocial;
    public static volatile SingularAttribute<Distribuidor, String> telefono;
    public static volatile SingularAttribute<Distribuidor, String> cifNif;
    public static volatile ListAttribute<Distribuidor, TieneDistribuidor> tieneDistribuidorList;

}