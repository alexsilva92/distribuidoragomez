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

import com.gomez.bd.controller.exceptions.IllegalOrphanException;
import com.gomez.bd.controller.exceptions.NonexistentEntityException;
import com.gomez.bd.controller.exceptions.PreexistingEntityException;
import com.gomez.bd.controller.exceptions.RollbackFailureException;
import com.gomez.bd.modelo.Producto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.TienePedidoCliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * ProductoJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class ProductoJpaController implements Serializable {

    public void ProductoJpaController(UserTransaction utx, EntityManagerFactory emf){this.utx = utx;
        this.emf = emf;
}
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (producto.getTienePedidoClienteList() == null) {
            producto.setTienePedidoClienteList(new ArrayList<TienePedidoCliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<TienePedidoCliente> attachedTienePedidoClienteList = new ArrayList<TienePedidoCliente>();
            for (TienePedidoCliente tienePedidoClienteListTienePedidoClienteToAttach : producto.getTienePedidoClienteList()) {
                tienePedidoClienteListTienePedidoClienteToAttach = em.getReference(tienePedidoClienteListTienePedidoClienteToAttach.getClass(), tienePedidoClienteListTienePedidoClienteToAttach.getTienePedidoClientePK());
                attachedTienePedidoClienteList.add(tienePedidoClienteListTienePedidoClienteToAttach);
            }
            producto.setTienePedidoClienteList(attachedTienePedidoClienteList);
            em.persist(producto);
            for (TienePedidoCliente tienePedidoClienteListTienePedidoCliente : producto.getTienePedidoClienteList()) {
                Producto oldProducto1OfTienePedidoClienteListTienePedidoCliente = tienePedidoClienteListTienePedidoCliente.getProducto1();
                tienePedidoClienteListTienePedidoCliente.setProducto1(producto);
                tienePedidoClienteListTienePedidoCliente = em.merge(tienePedidoClienteListTienePedidoCliente);
                if (oldProducto1OfTienePedidoClienteListTienePedidoCliente != null) {
                    oldProducto1OfTienePedidoClienteListTienePedidoCliente.getTienePedidoClienteList().remove(tienePedidoClienteListTienePedidoCliente);
                    oldProducto1OfTienePedidoClienteListTienePedidoCliente = em.merge(oldProducto1OfTienePedidoClienteListTienePedidoCliente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProducto(producto.getCodigo()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto persistentProducto = em.find(Producto.class, producto.getCodigo());
            List<TienePedidoCliente> tienePedidoClienteListOld = persistentProducto.getTienePedidoClienteList();
            List<TienePedidoCliente> tienePedidoClienteListNew = producto.getTienePedidoClienteList();
            List<String> illegalOrphanMessages = null;
            for (TienePedidoCliente tienePedidoClienteListOldTienePedidoCliente : tienePedidoClienteListOld) {
                if (!tienePedidoClienteListNew.contains(tienePedidoClienteListOldTienePedidoCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TienePedidoCliente " + tienePedidoClienteListOldTienePedidoCliente + " since its producto1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<TienePedidoCliente> attachedTienePedidoClienteListNew = new ArrayList<TienePedidoCliente>();
            for (TienePedidoCliente tienePedidoClienteListNewTienePedidoClienteToAttach : tienePedidoClienteListNew) {
                tienePedidoClienteListNewTienePedidoClienteToAttach = em.getReference(tienePedidoClienteListNewTienePedidoClienteToAttach.getClass(), tienePedidoClienteListNewTienePedidoClienteToAttach.getTienePedidoClientePK());
                attachedTienePedidoClienteListNew.add(tienePedidoClienteListNewTienePedidoClienteToAttach);
            }
            tienePedidoClienteListNew = attachedTienePedidoClienteListNew;
            producto.setTienePedidoClienteList(tienePedidoClienteListNew);
            producto = em.merge(producto);
            for (TienePedidoCliente tienePedidoClienteListNewTienePedidoCliente : tienePedidoClienteListNew) {
                if (!tienePedidoClienteListOld.contains(tienePedidoClienteListNewTienePedidoCliente)) {
                    Producto oldProducto1OfTienePedidoClienteListNewTienePedidoCliente = tienePedidoClienteListNewTienePedidoCliente.getProducto1();
                    tienePedidoClienteListNewTienePedidoCliente.setProducto1(producto);
                    tienePedidoClienteListNewTienePedidoCliente = em.merge(tienePedidoClienteListNewTienePedidoCliente);
                    if (oldProducto1OfTienePedidoClienteListNewTienePedidoCliente != null && !oldProducto1OfTienePedidoClienteListNewTienePedidoCliente.equals(producto)) {
                        oldProducto1OfTienePedidoClienteListNewTienePedidoCliente.getTienePedidoClienteList().remove(tienePedidoClienteListNewTienePedidoCliente);
                        oldProducto1OfTienePedidoClienteListNewTienePedidoCliente = em.merge(oldProducto1OfTienePedidoClienteListNewTienePedidoCliente);
                    }
                }
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
                String id = producto.getCodigo();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TienePedidoCliente> tienePedidoClienteListOrphanCheck = producto.getTienePedidoClienteList();
            for (TienePedidoCliente tienePedidoClienteListOrphanCheckTienePedidoCliente : tienePedidoClienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the TienePedidoCliente " + tienePedidoClienteListOrphanCheckTienePedidoCliente + " in its tienePedidoClienteList field has a non-nullable producto1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(producto);
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

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
