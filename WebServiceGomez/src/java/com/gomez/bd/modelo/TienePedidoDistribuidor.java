/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gomez.bd.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alex
 */
@Entity
@Table(name = "TienePedidoDistribuidor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TienePedidoDistribuidor.findAll", query = "SELECT t FROM TienePedidoDistribuidor t"),
    @NamedQuery(name = "TienePedidoDistribuidor.findByPedido", query = "SELECT t FROM TienePedidoDistribuidor t WHERE t.tienePedidoDistribuidorPK.pedido = :pedido"),
    @NamedQuery(name = "TienePedidoDistribuidor.findByProducto", query = "SELECT t FROM TienePedidoDistribuidor t WHERE t.tienePedidoDistribuidorPK.producto = :producto"),
    @NamedQuery(name = "TienePedidoDistribuidor.findByCantidad", query = "SELECT t FROM TienePedidoDistribuidor t WHERE t.cantidad = :cantidad")})
public class TienePedidoDistribuidor implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TienePedidoDistribuidorPK tienePedidoDistribuidorPK;
    @Column(name = "cantidad")
    private Integer cantidad;

    public TienePedidoDistribuidor() {
    }

    public TienePedidoDistribuidor(TienePedidoDistribuidorPK tienePedidoDistribuidorPK) {
        this.tienePedidoDistribuidorPK = tienePedidoDistribuidorPK;
    }

    public TienePedidoDistribuidor(int pedido, String producto) {
        this.tienePedidoDistribuidorPK = new TienePedidoDistribuidorPK(pedido, producto);
    }

    public TienePedidoDistribuidorPK getTienePedidoDistribuidorPK() {
        return tienePedidoDistribuidorPK;
    }

    public void setTienePedidoDistribuidorPK(TienePedidoDistribuidorPK tienePedidoDistribuidorPK) {
        this.tienePedidoDistribuidorPK = tienePedidoDistribuidorPK;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tienePedidoDistribuidorPK != null ? tienePedidoDistribuidorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TienePedidoDistribuidor)) {
            return false;
        }
        TienePedidoDistribuidor other = (TienePedidoDistribuidor) object;
        if ((this.tienePedidoDistribuidorPK == null && other.tienePedidoDistribuidorPK != null) || (this.tienePedidoDistribuidorPK != null && !this.tienePedidoDistribuidorPK.equals(other.tienePedidoDistribuidorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.TienePedidoDistribuidor[ tienePedidoDistribuidorPK=" + tienePedidoDistribuidorPK + " ]";
    }
    
}
