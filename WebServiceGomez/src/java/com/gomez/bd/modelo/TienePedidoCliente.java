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
 * TienePedidoCliente.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
@Entity
@Table(name = "TienePedidoCliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TienePedidoCliente.findAll", query = "SELECT t FROM TienePedidoCliente t"),
    @NamedQuery(name = "TienePedidoCliente.findByPedido", query = "SELECT t FROM TienePedidoCliente t WHERE t.tienePedidoClientePK.pedido = :pedido"),
    @NamedQuery(name = "TienePedidoCliente.findByProducto", query = "SELECT t FROM TienePedidoCliente t WHERE t.tienePedidoClientePK.producto = :producto"),
    @NamedQuery(name = "TienePedidoCliente.findByCantidad", query = "SELECT t FROM TienePedidoCliente t WHERE t.cantidad = :cantidad")})
public class TienePedidoCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TienePedidoClientePK tienePedidoClientePK;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "producto", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto1;
    @JoinColumn(name = "pedido", referencedColumnName = "idPedido", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PedidoCliente pedidoCliente;

    public TienePedidoCliente() {
    }

    public TienePedidoCliente(TienePedidoClientePK tienePedidoClientePK) {
        this.tienePedidoClientePK = tienePedidoClientePK;
    }

    public TienePedidoCliente(int pedido, String producto) {
        this.tienePedidoClientePK = new TienePedidoClientePK(pedido, producto);
    }

    public TienePedidoClientePK getTienePedidoClientePK() {
        return tienePedidoClientePK;
    }

    public void setTienePedidoClientePK(TienePedidoClientePK tienePedidoClientePK) {
        this.tienePedidoClientePK = tienePedidoClientePK;
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

    public PedidoCliente getPedidoCliente() {
        return pedidoCliente;
    }

    public void setPedidoCliente(PedidoCliente pedidoCliente) {
        this.pedidoCliente = pedidoCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tienePedidoClientePK != null ? tienePedidoClientePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TienePedidoCliente)) {
            return false;
        }
        TienePedidoCliente other = (TienePedidoCliente) object;
        if ((this.tienePedidoClientePK == null && other.tienePedidoClientePK != null) || (this.tienePedidoClientePK != null && !this.tienePedidoClientePK.equals(other.tienePedidoClientePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.TienePedidoCliente[ tienePedidoClientePK=" + tienePedidoClientePK + " ]";
    }

}
