/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gomez.bd.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alex
 */
@Entity
@Table(name = "PedidoCliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedidoCliente.findAll", query = "SELECT p FROM PedidoCliente p"),
    @NamedQuery(name = "PedidoCliente.findByIdPedido", query = "SELECT p FROM PedidoCliente p WHERE p.idPedido = :idPedido"),
    @NamedQuery(name = "PedidoCliente.findByFechaLllegada", query = "SELECT p FROM PedidoCliente p WHERE p.fechaLllegada = :fechaLllegada")})
public class PedidoCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idPedido")
    private Integer idPedido;
    @Column(name = "fechaLllegada")
    @Temporal(TemporalType.DATE)
    private Date fechaLllegada;
    @JoinColumn(name = "empleado", referencedColumnName = "dni")
    @ManyToOne
    private Empleado empleado;
    @JoinColumn(name = "cliente", referencedColumnName = "dni")
    @ManyToOne(optional = false)
    private Usuario cliente;
    @JoinColumn(name = "estado", referencedColumnName = "estado")
    @ManyToOne
    private EstadoPedido estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedidoCliente")
    private List<TienePedidoCliente> tienePedidoClienteList;

    public PedidoCliente() {
    }

    public PedidoCliente(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFechaLllegada() {
        return fechaLllegada;
    }

    public void setFechaLllegada(Date fechaLllegada) {
        this.fechaLllegada = fechaLllegada;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<TienePedidoCliente> getTienePedidoClienteList() {
        return tienePedidoClienteList;
    }

    public void setTienePedidoClienteList(List<TienePedidoCliente> tienePedidoClienteList) {
        this.tienePedidoClienteList = tienePedidoClienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPedido != null ? idPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoCliente)) {
            return false;
        }
        PedidoCliente other = (PedidoCliente) object;
        if ((this.idPedido == null && other.idPedido != null) || (this.idPedido != null && !this.idPedido.equals(other.idPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.PedidoCliente[ idPedido=" + idPedido + " ]";
    }
    
}
