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

package com.gomez.bd.controller;

import com.gomez.bd.controller.exceptions.NonexistentEntityException;
import com.gomez.bd.controller.exceptions.PreexistingEntityException;
import com.gomez.bd.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.Producto;
import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.TienePedidoCliente;
import com.gomez.bd.modelo.TienePedidoClientePK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * TienePedidoClienteJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class TienePedidoClienteJpaController implements Serializable {

    public void TienePedidoClienteJpaController(UserTransaction utx, EntityManagerFactory emf){this.utx = utx;
        this.emf = emf;
}
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TienePedidoCliente tienePedidoCliente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tienePedidoCliente.getTienePedidoClientePK() == null) {
            tienePedidoCliente.setTienePedidoClientePK(new TienePedidoClientePK());
        }
        tienePedidoCliente.getTienePedidoClientePK().setProducto(tienePedidoCliente.getProducto1().getCodigo());
        tienePedidoCliente.getTienePedidoClientePK().setPedido(tienePedidoCliente.getPedidoCliente().getIdPedido());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto producto1 = tienePedidoCliente.getProducto1();
            if (producto1 != null) {
                producto1 = em.getReference(producto1.getClass(), producto1.getCodigo());
                tienePedidoCliente.setProducto1(producto1);
            }
            PedidoCliente pedidoCliente = tienePedidoCliente.getPedidoCliente();
            if (pedidoCliente != null) {
                pedidoCliente = em.getReference(pedidoCliente.getClass(), pedidoCliente.getIdPedido());
                tienePedidoCliente.setPedidoCliente(pedidoCliente);
            }
            em.persist(tienePedidoCliente);
            if (producto1 != null) {
                producto1.getTienePedidoClienteList().add(tienePedidoCliente);
                producto1 = em.merge(producto1);
            }
            if (pedidoCliente != null) {
                pedidoCliente.getTienePedidoClienteList().add(tienePedidoCliente);
                pedidoCliente = em.merge(pedidoCliente);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTienePedidoCliente(tienePedidoCliente.getTienePedidoClientePK()) != null) {
                throw new PreexistingEntityException("TienePedidoCliente " + tienePedidoCliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TienePedidoCliente tienePedidoCliente) throws NonexistentEntityException, RollbackFailureException, Exception {
        tienePedidoCliente.getTienePedidoClientePK().setProducto(tienePedidoCliente.getProducto1().getCodigo());
        tienePedidoCliente.getTienePedidoClientePK().setPedido(tienePedidoCliente.getPedidoCliente().getIdPedido());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TienePedidoCliente persistentTienePedidoCliente = em.find(TienePedidoCliente.class, tienePedidoCliente.getTienePedidoClientePK());
            Producto producto1Old = persistentTienePedidoCliente.getProducto1();
            Producto producto1New = tienePedidoCliente.getProducto1();
            PedidoCliente pedidoClienteOld = persistentTienePedidoCliente.getPedidoCliente();
            PedidoCliente pedidoClienteNew = tienePedidoCliente.getPedidoCliente();
            if (producto1New != null) {
                producto1New = em.getReference(producto1New.getClass(), producto1New.getCodigo());
                tienePedidoCliente.setProducto1(producto1New);
            }
            if (pedidoClienteNew != null) {
                pedidoClienteNew = em.getReference(pedidoClienteNew.getClass(), pedidoClienteNew.getIdPedido());
                tienePedidoCliente.setPedidoCliente(pedidoClienteNew);
            }
            tienePedidoCliente = em.merge(tienePedidoCliente);
            if (producto1Old != null && !producto1Old.equals(producto1New)) {
                producto1Old.getTienePedidoClienteList().remove(tienePedidoCliente);
                producto1Old = em.merge(producto1Old);
            }
            if (producto1New != null && !producto1New.equals(producto1Old)) {
                producto1New.getTienePedidoClienteList().add(tienePedidoCliente);
                producto1New = em.merge(producto1New);
            }
            if (pedidoClienteOld != null && !pedidoClienteOld.equals(pedidoClienteNew)) {
                pedidoClienteOld.getTienePedidoClienteList().remove(tienePedidoCliente);
                pedidoClienteOld = em.merge(pedidoClienteOld);
            }
            if (pedidoClienteNew != null && !pedidoClienteNew.equals(pedidoClienteOld)) {
                pedidoClienteNew.getTienePedidoClienteList().add(tienePedidoCliente);
                pedidoClienteNew = em.merge(pedidoClienteNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TienePedidoClientePK id = tienePedidoCliente.getTienePedidoClientePK();
                if (findTienePedidoCliente(id) == null) {
                    throw new NonexistentEntityException("The tienePedidoCliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TienePedidoClientePK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TienePedidoCliente tienePedidoCliente;
            try {
                tienePedidoCliente = em.getReference(TienePedidoCliente.class, id);
                tienePedidoCliente.getTienePedidoClientePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tienePedidoCliente with id " + id + " no longer exists.", enfe);
            }
            Producto producto1 = tienePedidoCliente.getProducto1();
            if (producto1 != null) {
                producto1.getTienePedidoClienteList().remove(tienePedidoCliente);
                producto1 = em.merge(producto1);
            }
            PedidoCliente pedidoCliente = tienePedidoCliente.getPedidoCliente();
            if (pedidoCliente != null) {
                pedidoCliente.getTienePedidoClienteList().remove(tienePedidoCliente);
                pedidoCliente = em.merge(pedidoCliente);
            }
            em.remove(tienePedidoCliente);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TienePedidoCliente> findTienePedidoClienteEntities() {
        return findTienePedidoClienteEntities(true, -1, -1);
    }

    public List<TienePedidoCliente> findTienePedidoClienteEntities(int maxResults, int firstResult) {
        return findTienePedidoClienteEntities(false, maxResults, firstResult);
    }

    private List<TienePedidoCliente> findTienePedidoClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TienePedidoCliente.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TienePedidoCliente findTienePedidoCliente(TienePedidoClientePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TienePedidoCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getTienePedidoClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TienePedidoCliente> rt = cq.from(TienePedidoCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
