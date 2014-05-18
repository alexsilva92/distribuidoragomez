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
 * MarcaProducto.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
@Entity
@Table(name = "MarcaProducto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MarcaProducto.findAll", query = "SELECT m FROM MarcaProducto m"),
    @NamedQuery(name = "MarcaProducto.findByMarcaProducto", query = "SELECT m FROM MarcaProducto m WHERE m.marcaProducto = :marcaProducto"),
    @NamedQuery(name = "MarcaProducto.findByCategoria", query = "SELECT m FROM MarcaProducto m WHERE m.categoria = :categoria")})
public class MarcaProducto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "marcaProducto")
    private String marcaProducto;
    @Size(max = 45)
    @Column(name = "categoria")
    private String categoria;
    @OneToMany(mappedBy = "marca")
    private List<Producto> productoList;

    public MarcaProducto() {
    }

    public MarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }

    public String getMarcaProducto() {
        return marcaProducto;
    }

    public void setMarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @XmlTransient
    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (marcaProducto != null ? marcaProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MarcaProducto)) {
            return false;
        }
        MarcaProducto other = (MarcaProducto) object;
        if ((this.marcaProducto == null && other.marcaProducto != null) || (this.marcaProducto != null && !this.marcaProducto.equals(other.marcaProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.MarcaProducto[ marcaProducto=" + marcaProducto + " ]";
    }

}
