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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Producto.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
@Entity
@Table(name = "Producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByCodigo", query = "SELECT p FROM Producto p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "Producto.findByNombre", query = "SELECT p FROM Producto p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Producto.findByPrecio", query = "SELECT p FROM Producto p WHERE p.precio = :precio"),
    @NamedQuery(name = "Producto.findByImagen", query = "SELECT p FROM Producto p WHERE p.imagen = :imagen")})
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio")
    private float precio;
    @Size(max = 100)
    @Column(name = "imagen")
    private String imagen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto1")
    private List<TienePedidoCliente> tienePedidoClienteList;
    @JoinColumn(name = "marca", referencedColumnName = "marcaProducto")
    @ManyToOne
    private MarcaProducto marca;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "producto1")
    private Stock stock;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto1")
    private List<TienePedidoDistribuidor> tienePedidoDistribuidorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto1")
    private List<TieneDistribuidor> tieneDistribuidorList;

    public Producto() {
    }

    public Producto(String codigo) {
        this.codigo = codigo;
    }

    public Producto(String codigo, String nombre, float precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @XmlTransient
    public List<TienePedidoCliente> getTienePedidoClienteList() {
        return tienePedidoClienteList;
    }

    public void setTienePedidoClienteList(List<TienePedidoCliente> tienePedidoClienteList) {
        this.tienePedidoClienteList = tienePedidoClienteList;
    }

    public MarcaProducto getMarca() {
        return marca;
    }

    public void setMarca(MarcaProducto marca) {
        this.marca = marca;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @XmlTransient
    public List<TienePedidoDistribuidor> getTienePedidoDistribuidorList() {
        return tienePedidoDistribuidorList;
    }

    public void setTienePedidoDistribuidorList(List<TienePedidoDistribuidor> tienePedidoDistribuidorList) {
        this.tienePedidoDistribuidorList = tienePedidoDistribuidorList;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gomez.bd.modelo.Producto[ codigo=" + codigo + " ]";
    }

}
