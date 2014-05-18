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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TieneDistribuidorPK.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
@Embeddable
public class TieneDistribuidorPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "distribuidor")
    private String distribuidor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "producto")
    private String producto;

    public TieneDistribuidorPK() {
    }

    public TieneDistribuidorPK(String distribuidor, String producto) {
        this.distribuidor = distribuidor;
        this.producto = producto;
    }

    public String getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(String distribuidor) {
        this.distribuidor = distribuidor;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (distribuidor != null ? distribuidor.hashCode() : 0);
        hash += (producto != null ? producto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TieneDistribuidorPK)) {
            return false;
        }
        TieneDistribuidorPK other = (TieneDistribuidorPK) object;
        if ((this.distribuidor == null && other.distribuidor != null) || (this.distribuidor != null && !this.distribuidor.equals(other.distribuidor))) {
            return false;
        }
        if ((this.producto == null && other.producto != null) || (this.producto != null && !this.producto.equals(other.producto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.TieneDistribuidorPK[ distribuidor=" + distribuidor + ", producto=" + producto + " ]";
    }

}
