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
 * TienePedidoDistribuidorPK.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
@Embeddable
public class TienePedidoDistribuidorPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "pedido")
    private int pedido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "producto")
    private String producto;

    public TienePedidoDistribuidorPK(){}

    public TienePedidoDistribuidorPK(int pedido, String producto) {
        this.pedido = pedido;
        this.producto = producto;
    }

    public int getPedido() {
        return pedido;
    }

    public void setPedido(int pedido) {
        this.pedido = pedido;
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
        hash += (int) pedido;
        hash += (producto != null ? producto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TienePedidoDistribuidorPK)) {
            return false;
        }
        TienePedidoDistribuidorPK other = (TienePedidoDistribuidorPK) object;
        if (this.pedido != other.pedido) {
            return false;
        }
        if ((this.producto == null && other.producto != null) || (this.producto != null && !this.producto.equals(other.producto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.TienePedidoDistribuidorPK[ pedido=" + pedido + ", producto=" + producto + " ]";
    }
}
