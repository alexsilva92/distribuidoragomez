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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.MarcaProducto;
import com.gomez.bd.modelo.Producto;
import com.gomez.bd.modelo.Stock;
import com.gomez.bd.modelo.TienePedidoCliente;
import java.util.ArrayList;
import java.util.List;
import com.gomez.bd.modelo.TienePedidoDistribuidor;
import com.gomez.bd.modelo.TieneDistribuidor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * ProductoJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
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
        if (producto.getTienePedidoDistribuidorList() == null) {
            producto.setTienePedidoDistribuidorList(new ArrayList<TienePedidoDistribuidor>());
        }
        if (producto.getTieneDistribuidorList() == null) {
            producto.setTieneDistribuidorList(new ArrayList<TieneDistribuidor>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MarcaProducto marca = producto.getMarca();
            if (marca != null) {
                marca = em.getReference(marca.getClass(), marca.getMarcaProducto());
                producto.setMarca(marca);
            }
            Stock stock = producto.getStock();
            if (stock != null) {
                stock = em.getReference(stock.getClass(), stock.getProducto());
                producto.setStock(stock);
            }
            List<TienePedidoCliente> attachedTienePedidoClienteList = new ArrayList<TienePedidoCliente>();
            for (TienePedidoCliente tienePedidoClienteListTienePedidoClienteToAttach : producto.getTienePedidoClienteList()) {
                tienePedidoClienteListTienePedidoClienteToAttach = em.getReference(tienePedidoClienteListTienePedidoClienteToAttach.getClass(), tienePedidoClienteListTienePedidoClienteToAttach.getTienePedidoClientePK());
                attachedTienePedidoClienteList.add(tienePedidoClienteListTienePedidoClienteToAttach);
            }
            producto.setTienePedidoClienteList(attachedTienePedidoClienteList);
            List<TienePedidoDistribuidor> attachedTienePedidoDistribuidorList = new ArrayList<TienePedidoDistribuidor>();
            for (TienePedidoDistribuidor tienePedidoDistribuidorListTienePedidoDistribuidorToAttach : producto.getTienePedidoDistribuidorList()) {
                tienePedidoDistribuidorListTienePedidoDistribuidorToAttach = em.getReference(tienePedidoDistribuidorListTienePedidoDistribuidorToAttach.getClass(), tienePedidoDistribuidorListTienePedidoDistribuidorToAttach.getTienePedidoDistribuidorPK());
                attachedTienePedidoDistribuidorList.add(tienePedidoDistribuidorListTienePedidoDistribuidorToAttach);
            }
            producto.setTienePedidoDistribuidorList(attachedTienePedidoDistribuidorList);
            List<TieneDistribuidor> attachedTieneDistribuidorList = new ArrayList<TieneDistribuidor>();
            for (TieneDistribuidor tieneDistribuidorListTieneDistribuidorToAttach : producto.getTieneDistribuidorList()) {
                tieneDistribuidorListTieneDistribuidorToAttach = em.getReference(tieneDistribuidorListTieneDistribuidorToAttach.getClass(), tieneDistribuidorListTieneDistribuidorToAttach.getTieneDistribuidorPK());
                attachedTieneDistribuidorList.add(tieneDistribuidorListTieneDistribuidorToAttach);
            }
            producto.setTieneDistribuidorList(attachedTieneDistribuidorList);
            em.persist(producto);
            if (marca != null) {
                marca.getProductoList().add(producto);
                marca = em.merge(marca);
            }
            if (stock != null) {
                Producto oldProducto1OfStock = stock.getProducto1();
                if (oldProducto1OfStock != null) {
                    oldProducto1OfStock.setStock(null);
                    oldProducto1OfStock = em.merge(oldProducto1OfStock);
                }
                stock.setProducto1(producto);
                stock = em.merge(stock);
            }
            for (TienePedidoCliente tienePedidoClienteListTienePedidoCliente : producto.getTienePedidoClienteList()) {
                Producto oldProducto1OfTienePedidoClienteListTienePedidoCliente = tienePedidoClienteListTienePedidoCliente.getProducto1();
                tienePedidoClienteListTienePedidoCliente.setProducto1(producto);
                tienePedidoClienteListTienePedidoCliente = em.merge(tienePedidoClienteListTienePedidoCliente);
                if (oldProducto1OfTienePedidoClienteListTienePedidoCliente != null) {
                    oldProducto1OfTienePedidoClienteListTienePedidoCliente.getTienePedidoClienteList().remove(tienePedidoClienteListTienePedidoCliente);
                    oldProducto1OfTienePedidoClienteListTienePedidoCliente = em.merge(oldProducto1OfTienePedidoClienteListTienePedidoCliente);
                }
            }
            for (TienePedidoDistribuidor tienePedidoDistribuidorListTienePedidoDistribuidor : producto.getTienePedidoDistribuidorList()) {
                Producto oldProducto1OfTienePedidoDistribuidorListTienePedidoDistribuidor = tienePedidoDistribuidorListTienePedidoDistribuidor.getProducto1();
                tienePedidoDistribuidorListTienePedidoDistribuidor.setProducto1(producto);
                tienePedidoDistribuidorListTienePedidoDistribuidor = em.merge(tienePedidoDistribuidorListTienePedidoDistribuidor);
                if (oldProducto1OfTienePedidoDistribuidorListTienePedidoDistribuidor != null) {
                    oldProducto1OfTienePedidoDistribuidorListTienePedidoDistribuidor.getTienePedidoDistribuidorList().remove(tienePedidoDistribuidorListTienePedidoDistribuidor);
                    oldProducto1OfTienePedidoDistribuidorListTienePedidoDistribuidor = em.merge(oldProducto1OfTienePedidoDistribuidorListTienePedidoDistribuidor);
                }
            }
            for (TieneDistribuidor tieneDistribuidorListTieneDistribuidor : producto.getTieneDistribuidorList()) {
                Producto oldProducto1OfTieneDistribuidorListTieneDistribuidor = tieneDistribuidorListTieneDistribuidor.getProducto1();
                tieneDistribuidorListTieneDistribuidor.setProducto1(producto);
                tieneDistribuidorListTieneDistribuidor = em.merge(tieneDistribuidorListTieneDistribuidor);
                if (oldProducto1OfTieneDistribuidorListTieneDistribuidor != null) {
                    oldProducto1OfTieneDistribuidorListTieneDistribuidor.getTieneDistribuidorList().remove(tieneDistribuidorListTieneDistribuidor);
                    oldProducto1OfTieneDistribuidorListTieneDistribuidor = em.merge(oldProducto1OfTieneDistribuidorListTieneDistribuidor);
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
            MarcaProducto marcaOld = persistentProducto.getMarca();
            MarcaProducto marcaNew = producto.getMarca();
            Stock stockOld = persistentProducto.getStock();
            Stock stockNew = producto.getStock();
            List<TienePedidoCliente> tienePedidoClienteListOld = persistentProducto.getTienePedidoClienteList();
            List<TienePedidoCliente> tienePedidoClienteListNew = producto.getTienePedidoClienteList();
            List<TienePedidoDistribuidor> tienePedidoDistribuidorListOld = persistentProducto.getTienePedidoDistribuidorList();
            List<TienePedidoDistribuidor> tienePedidoDistribuidorListNew = producto.getTienePedidoDistribuidorList();
            List<TieneDistribuidor> tieneDistribuidorListOld = persistentProducto.getTieneDistribuidorList();
            List<TieneDistribuidor> tieneDistribuidorListNew = producto.getTieneDistribuidorList();
            List<String> illegalOrphanMessages = null;
            if (stockOld != null && !stockOld.equals(stockNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Stock " + stockOld + " since its producto1 field is not nullable.");
            }
            for (TienePedidoCliente tienePedidoClienteListOldTienePedidoCliente : tienePedidoClienteListOld) {
                if (!tienePedidoClienteListNew.contains(tienePedidoClienteListOldTienePedidoCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TienePedidoCliente " + tienePedidoClienteListOldTienePedidoCliente + " since its producto1 field is not nullable.");
                }
            }
            for (TienePedidoDistribuidor tienePedidoDistribuidorListOldTienePedidoDistribuidor : tienePedidoDistribuidorListOld) {
                if (!tienePedidoDistribuidorListNew.contains(tienePedidoDistribuidorListOldTienePedidoDistribuidor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TienePedidoDistribuidor " + tienePedidoDistribuidorListOldTienePedidoDistribuidor + " since its producto1 field is not nullable.");
                }
            }
            for (TieneDistribuidor tieneDistribuidorListOldTieneDistribuidor : tieneDistribuidorListOld) {
                if (!tieneDistribuidorListNew.contains(tieneDistribuidorListOldTieneDistribuidor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TieneDistribuidor " + tieneDistribuidorListOldTieneDistribuidor + " since its producto1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (marcaNew != null) {
                marcaNew = em.getReference(marcaNew.getClass(), marcaNew.getMarcaProducto());
                producto.setMarca(marcaNew);
            }
            if (stockNew != null) {
                stockNew = em.getReference(stockNew.getClass(), stockNew.getProducto());
                producto.setStock(stockNew);
            }
            List<TienePedidoCliente> attachedTienePedidoClienteListNew = new ArrayList<TienePedidoCliente>();
            for (TienePedidoCliente tienePedidoClienteListNewTienePedidoClienteToAttach : tienePedidoClienteListNew) {
                tienePedidoClienteListNewTienePedidoClienteToAttach = em.getReference(tienePedidoClienteListNewTienePedidoClienteToAttach.getClass(), tienePedidoClienteListNewTienePedidoClienteToAttach.getTienePedidoClientePK());
                attachedTienePedidoClienteListNew.add(tienePedidoClienteListNewTienePedidoClienteToAttach);
            }
            tienePedidoClienteListNew = attachedTienePedidoClienteListNew;
            producto.setTienePedidoClienteList(tienePedidoClienteListNew);
            List<TienePedidoDistribuidor> attachedTienePedidoDistribuidorListNew = new ArrayList<TienePedidoDistribuidor>();
            for (TienePedidoDistribuidor tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach : tienePedidoDistribuidorListNew) {
                tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach = em.getReference(tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach.getClass(), tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach.getTienePedidoDistribuidorPK());
                attachedTienePedidoDistribuidorListNew.add(tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach);
            }
            tienePedidoDistribuidorListNew = attachedTienePedidoDistribuidorListNew;
            producto.setTienePedidoDistribuidorList(tienePedidoDistribuidorListNew);
            List<TieneDistribuidor> attachedTieneDistribuidorListNew = new ArrayList<TieneDistribuidor>();
            for (TieneDistribuidor tieneDistribuidorListNewTieneDistribuidorToAttach : tieneDistribuidorListNew) {
                tieneDistribuidorListNewTieneDistribuidorToAttach = em.getReference(tieneDistribuidorListNewTieneDistribuidorToAttach.getClass(), tieneDistribuidorListNewTieneDistribuidorToAttach.getTieneDistribuidorPK());
                attachedTieneDistribuidorListNew.add(tieneDistribuidorListNewTieneDistribuidorToAttach);
            }
            tieneDistribuidorListNew = attachedTieneDistribuidorListNew;
            producto.setTieneDistribuidorList(tieneDistribuidorListNew);
            producto = em.merge(producto);
            if (marcaOld != null && !marcaOld.equals(marcaNew)) {
                marcaOld.getProductoList().remove(producto);
                marcaOld = em.merge(marcaOld);
            }
            if (marcaNew != null && !marcaNew.equals(marcaOld)) {
                marcaNew.getProductoList().add(producto);
                marcaNew = em.merge(marcaNew);
            }
            if (stockNew != null && !stockNew.equals(stockOld)) {
                Producto oldProducto1OfStock = stockNew.getProducto1();
                if (oldProducto1OfStock != null) {
                    oldProducto1OfStock.setStock(null);
                    oldProducto1OfStock = em.merge(oldProducto1OfStock);
                }
                stockNew.setProducto1(producto);
                stockNew = em.merge(stockNew);
            }
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
            for (TienePedidoDistribuidor tienePedidoDistribuidorListNewTienePedidoDistribuidor : tienePedidoDistribuidorListNew) {
                if (!tienePedidoDistribuidorListOld.contains(tienePedidoDistribuidorListNewTienePedidoDistribuidor)) {
                    Producto oldProducto1OfTienePedidoDistribuidorListNewTienePedidoDistribuidor = tienePedidoDistribuidorListNewTienePedidoDistribuidor.getProducto1();
                    tienePedidoDistribuidorListNewTienePedidoDistribuidor.setProducto1(producto);
                    tienePedidoDistribuidorListNewTienePedidoDistribuidor = em.merge(tienePedidoDistribuidorListNewTienePedidoDistribuidor);
                    if (oldProducto1OfTienePedidoDistribuidorListNewTienePedidoDistribuidor != null && !oldProducto1OfTienePedidoDistribuidorListNewTienePedidoDistribuidor.equals(producto)) {
                        oldProducto1OfTienePedidoDistribuidorListNewTienePedidoDistribuidor.getTienePedidoDistribuidorList().remove(tienePedidoDistribuidorListNewTienePedidoDistribuidor);
                        oldProducto1OfTienePedidoDistribuidorListNewTienePedidoDistribuidor = em.merge(oldProducto1OfTienePedidoDistribuidorListNewTienePedidoDistribuidor);
                    }
                }
            }
            for (TieneDistribuidor tieneDistribuidorListNewTieneDistribuidor : tieneDistribuidorListNew) {
                if (!tieneDistribuidorListOld.contains(tieneDistribuidorListNewTieneDistribuidor)) {
                    Producto oldProducto1OfTieneDistribuidorListNewTieneDistribuidor = tieneDistribuidorListNewTieneDistribuidor.getProducto1();
                    tieneDistribuidorListNewTieneDistribuidor.setProducto1(producto);
                    tieneDistribuidorListNewTieneDistribuidor = em.merge(tieneDistribuidorListNewTieneDistribuidor);
                    if (oldProducto1OfTieneDistribuidorListNewTieneDistribuidor != null && !oldProducto1OfTieneDistribuidorListNewTieneDistribuidor.equals(producto)) {
                        oldProducto1OfTieneDistribuidorListNewTieneDistribuidor.getTieneDistribuidorList().remove(tieneDistribuidorListNewTieneDistribuidor);
                        oldProducto1OfTieneDistribuidorListNewTieneDistribuidor = em.merge(oldProducto1OfTieneDistribuidorListNewTieneDistribuidor);
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
            Stock stockOrphanCheck = producto.getStock();
            if (stockOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Stock " + stockOrphanCheck + " in its stock field has a non-nullable producto1 field.");
            }
            List<TienePedidoCliente> tienePedidoClienteListOrphanCheck = producto.getTienePedidoClienteList();
            for (TienePedidoCliente tienePedidoClienteListOrphanCheckTienePedidoCliente : tienePedidoClienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the TienePedidoCliente " + tienePedidoClienteListOrphanCheckTienePedidoCliente + " in its tienePedidoClienteList field has a non-nullable producto1 field.");
            }
            List<TienePedidoDistribuidor> tienePedidoDistribuidorListOrphanCheck = producto.getTienePedidoDistribuidorList();
            for (TienePedidoDistribuidor tienePedidoDistribuidorListOrphanCheckTienePedidoDistribuidor : tienePedidoDistribuidorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the TienePedidoDistribuidor " + tienePedidoDistribuidorListOrphanCheckTienePedidoDistribuidor + " in its tienePedidoDistribuidorList field has a non-nullable producto1 field.");
            }
            List<TieneDistribuidor> tieneDistribuidorListOrphanCheck = producto.getTieneDistribuidorList();
            for (TieneDistribuidor tieneDistribuidorListOrphanCheckTieneDistribuidor : tieneDistribuidorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the TieneDistribuidor " + tieneDistribuidorListOrphanCheckTieneDistribuidor + " in its tieneDistribuidorList field has a non-nullable producto1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            MarcaProducto marca = producto.getMarca();
            if (marca != null) {
                marca.getProductoList().remove(producto);
                marca = em.merge(marca);
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
