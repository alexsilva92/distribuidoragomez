/*
 * Copyright 2014 Alejandro Silva <alexsilva792@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gomez.bd.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TieneDistribuidor.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
@Entity
@Table(name = "TieneDistribuidor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TieneDistribuidor.findAll", query = "SELECT t FROM TieneDistribuidor t"),
    @NamedQuery(name = "TieneDistribuidor.findByDistribuidor", query = "SELECT t FROM TieneDistribuidor t WHERE t.tieneDistribuidorPK.distribuidor = :distribuidor"),
    @NamedQuery(name = "TieneDistribuidor.findByProducto", query = "SELECT t FROM TieneDistribuidor t WHERE t.tieneDistribuidorPK.producto = :producto"),
    @NamedQuery(name = "TieneDistribuidor.findByCantidad", query = "SELECT t FROM TieneDistribuidor t WHERE t.cantidad = :cantidad")})
public class TieneDistribuidor implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TieneDistribuidorPK tieneDistribuidorPK;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "producto", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto1;
    @JoinColumn(name = "distribuidor", referencedColumnName = "cifNif", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Distribuidor distribuidor1;

    public TieneDistribuidor(){}

    public TieneDistribuidor(TieneDistribuidorPK tieneDistribuidorPK) {
        this.tieneDistribuidorPK = tieneDistribuidorPK;
    }

    public TieneDistribuidor(String distribuidor, String producto) {
        this.tieneDistribuidorPK = new TieneDistribuidorPK(distribuidor, producto);
    }

    public TieneDistribuidorPK getTieneDistribuidorPK() {
        return tieneDistribuidorPK;
    }

    public void setTieneDistribuidorPK(TieneDistribuidorPK tieneDistribuidorPK) {
        this.tieneDistribuidorPK = tieneDistribuidorPK;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto1() {
        return producto1;
    }

    public void setProducto1(Producto producto1) {
        this.producto1 = producto1;
    }

    public Distribuidor getDistribuidor1() {
        return distribuidor1;
    }

    public void setDistribuidor1(Distribuidor distribuidor1) {
        this.distribuidor1 = distribuidor1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tieneDistribuidorPK != null ? tieneDistribuidorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TieneDistribuidor)) {
            return false;
        }
        TieneDistribuidor other = (TieneDistribuidor) object;
        if ((this.tieneDistribuidorPK == null && other.tieneDistribuidorPK != null) || (this.tieneDistribuidorPK != null && !this.tieneDistribuidorPK.equals(other.tieneDistribuidorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.TieneDistribuidor[ tieneDistribuidorPK=" + tieneDistribuidorPK + " ]";
    }
}
