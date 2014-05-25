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

package com.gomez.webservice;

import com.gomez.bd.modelo.Cliente;
import com.gomez.bd.modelo.Distribuidor;
import com.gomez.bd.modelo.Empleado;
import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.TieneDistribuidor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 * QueryJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class QueryJpaController {
    
    public QueryJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public boolean loginCliente(String login, String password){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT c FROM Cliente c WHERE "
                + "c.login = :login AND c.password = :password");
        q.setParameter("login", login);
        q.setParameter("password", password);
        
        try{
            Cliente cliente = (Cliente) q.getSingleResult();
            if(cliente != null){
                return true;
            }else{
                return false;
            }
        }catch(NoResultException  ex){
            return false;
        }
    }
    
    public boolean loginEmpleado(String login, String password){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT e FROM Empleado e WHERE "
                + "e.login = :login AND e.password = :password");
        q.setParameter("login", login);
        q.setParameter("password", password);
        
        try{
            Empleado empleado = (Empleado) q.getSingleResult();
            if(empleado != null){
                return true;
            }else{
                return false;
            }
        }catch(NoResultException  ex){
            return false;
        }
    }
    
    public List<PedidoCliente> getPedidosPorEmpleado(String dni) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT p FROM PedidoCliente p WHERE "
                + "p.empleado = :empleado");
        Empleado empleado = em.find(Empleado.class, dni);
        q.setParameter("empleado",empleado);
        
       return q.getResultList();  
    }
    
    public List<PedidoCliente> getPedidosPorCliente(String dni) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT p FROM PedidoCliente p WHERE "
                + "p.cliente = :cliente");
        Cliente cliente = em.find(Cliente.class, dni);
        q.setParameter("cliente",cliente);
        
       return q.getResultList();  
    }
    
    public List<TieneDistribuidor> getProductosDistribuidor(String 
    distribuidor) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT p FROM TieneDistribuidor p WHERE "
                + "p.distribuidor = :distribuidor");
        q.setParameter("distribuidor",distribuidor);
        
       return q.getResultList();  
    }
}
