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

package com.gomez.bd.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * PedidoClienteBean.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class PedidoClienteBean implements Serializable {
    private int idPedido;
    private ClienteBean cliente;
    private EmpleadoBean empleado;
    private String estado;
    private Date fechaLlegada;
    private List<StockBean> productos;

    public PedidoClienteBean() {
    }

    public PedidoClienteBean(int idPedido, ClienteBean cliente, 
    EmpleadoBean empleado, String estado, Date fechaLlegada,
    List<StockBean> productos) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.empleado = empleado;
        this.estado = estado;
        this.fechaLlegada = fechaLlegada;
        this.productos = productos;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public ClienteBean getCliente() {
        return cliente;
    }

    public void setCliente(ClienteBean cliente) {
        this.cliente = cliente;
    }

    public EmpleadoBean getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoBean empleado) {
        this.empleado = empleado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public List<StockBean> getProductos() {
        return productos;
    }

    public void setProductos(List<StockBean> productos) {
        this.productos = productos;
    }
}
