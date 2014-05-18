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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PedidoDistribuidor.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
@Entity
@Table(name = "PedidoDistribuidor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedidoDistribuidor.findAll", query = "SELECT p FROM PedidoDistribuidor p"),
    @NamedQuery(name = "PedidoDistribuidor.findByIdPedido", query = "SELECT p FROM PedidoDistribuidor p WHERE p.idPedido = :idPedido"),
    @NamedQuery(name = "PedidoDistribuidor.findByFechaLlegada", query = "SELECT p FROM PedidoDistribuidor p WHERE p.fechaLlegada = :fechaLlegada")})
public class PedidoDistribuidor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPedido")
    private Integer idPedido;
    @Column(name = "fechaLlegada")
    @Temporal(TemporalType.DATE)
    private Date fechaLlegada;
    @JoinColumn(name = "estado", referencedColumnName = "estado")
    @ManyToOne
    private EstadoPedido estado;
    @JoinColumn(name = "empleado", referencedColumnName = "dni")
    @ManyToOne
    private Empleado empleado;
    @JoinColumn(name = "distribuidor", referencedColumnName = "cifNif")
    @ManyToOne(optional = false)
    private Distribuidor distribuidor;

    public PedidoDistribuidor() {
    }

    public PedidoDistribuidor(Integer idPedido) {
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

    public Distribuidor getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(Distribuidor distribuidor) {
        this.distribuidor = distribuidor;
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
        if (!(object instanceof PedidoDistribuidor)) {
            return false;
        }
        PedidoDistribuidor other = (PedidoDistribuidor) object;
        if ((this.idPedido == null && other.idPedido != null) || (this.idPedido != null && !this.idPedido.equals(other.idPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.PedidoDistribuidor[ idPedido=" + idPedido + " ]";
    }

}
