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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Distribuidor.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
@Entity
@Table(name = "Distribuidor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Distribuidor.findAll", query = "SELECT d FROM Distribuidor d"),
    @NamedQuery(name = "Distribuidor.findByCifNif", query = "SELECT d FROM Distribuidor d WHERE d.cifNif = :cifNif"),
    @NamedQuery(name = "Distribuidor.findByRazonSocial", query = "SELECT d FROM Distribuidor d WHERE d.razonSocial = :razonSocial"),
    @NamedQuery(name = "Distribuidor.findByTelefono", query = "SELECT d FROM Distribuidor d WHERE d.telefono = :telefono")})
public class Distribuidor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "cifNif")
    private String cifNif;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "razonSocial")
    private String razonSocial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "telefono")
    private String telefono;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distribuidor")
    private List<PedidoDistribuidor> pedidoDistribuidorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distribuidor1")
    private List<TieneDistribuidor> tieneDistribuidorList;

    public Distribuidor() {
    }

    public Distribuidor(String cifNif) {
        this.cifNif = cifNif;
    }

    public Distribuidor(String cifNif, String razonSocial, String telefono) {
        this.cifNif = cifNif;
        this.razonSocial = razonSocial;
        this.telefono = telefono;
    }

    public String getCifNif() {
        return cifNif;
    }

    public void setCifNif(String cifNif) {
        this.cifNif = cifNif;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @XmlTransient
    public List<PedidoDistribuidor> getPedidoDistribuidorList() {
        return pedidoDistribuidorList;
    }

    public void setPedidoDistribuidorList(List<PedidoDistribuidor> pedidoDistribuidorList) {
        this.pedidoDistribuidorList = pedidoDistribuidorList;
    }

    @XmlTransient
    public List<TieneDistribuidor> getTieneDistribuidorList() {
        return tieneDistribuidorList;
    }

    public void setTieneDistribuidorList(List<TieneDistribuidor> tieneDistribuidorList) {
        this.tieneDistribuidorList = tieneDistribuidorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cifNif != null ? cifNif.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Distribuidor)) {
            return false;
        }
        Distribuidor other = (Distribuidor) object;
        if ((this.cifNif == null && other.cifNif != null) || (this.cifNif != null && !this.cifNif.equals(other.cifNif))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.Distribuidor[ cifNif=" + cifNif + " ]";
    }

}
