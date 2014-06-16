package com.gomez.bd.modelo;

import com.gomez.bd.modelo.Distribuidor;
import com.gomez.bd.modelo.Producto;
import com.gomez.bd.modelo.TieneDistribuidorPK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-06-16T13:16:16")
@StaticMetamodel(TieneDistribuidor.class)
public class TieneDistribuidor_ { 

    public static volatile SingularAttribute<TieneDistribuidor, TieneDistribuidorPK> tieneDistribuidorPK;
    public static volatile SingularAttribute<TieneDistribuidor, Producto> producto1;
    public static volatile SingularAttribute<TieneDistribuidor, Integer> cantidad;
    public static volatile SingularAttribute<TieneDistribuidor, Distribuidor> distribuidor1;

}