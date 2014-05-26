package com.gomez.bd.modelo;

import com.gomez.bd.modelo.Producto;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-05-26T16:27:55")
@StaticMetamodel(MarcaProducto.class)
public class MarcaProducto_ { 

    public static volatile SingularAttribute<MarcaProducto, String> categoria;
    public static volatile SingularAttribute<MarcaProducto, String> marcaProducto;
    public static volatile ListAttribute<MarcaProducto, Producto> productoList;

}