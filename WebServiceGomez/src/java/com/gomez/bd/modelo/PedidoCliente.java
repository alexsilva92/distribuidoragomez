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
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * PedidoCliente.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
@Entity
@Table(name = "PedidoCliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedidoCliente.findAll", query = "SELECT p FROM PedidoCliente p"),
    @NamedQuery(name = "PedidoCliente.findByIdPedido", query = "SELECT p FROM PedidoCliente p WHERE p.idPedido = :idPedido"),
    @NamedQuery(name = "PedidoCliente.findByFechaLlegada", query = "SELECT p FROM PedidoCliente p WHERE p.fechaLlegada = :fechaLlegada"),
    @NamedQuery(name = "PedidoCliente.findByFechaEmision", query = "SELECT p FROM PedidoCliente p WHERE p.fechaEmision = :fechaEmision")})
public class PedidoCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPedido")
    private Integer idPedido;
    @Column(name = "fechaLlegada")
    @Temporal(TemporalType.DATE)
    private Date fechaLlegada;
    @Column(name = "fechaEmision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @JoinColumn(name = "estado", referencedColumnName = "estado")
    @ManyToOne
    private EstadoPedido estado;
    @JoinColumn(name = "empleado", referencedColumnName = "dni")
    @ManyToOne
    private Empleado empleado;
    @JoinColumn(name = "cliente", referencedColumnName = "dni")
    @ManyToOne(optional = false)
    private Cliente cliente;
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

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
