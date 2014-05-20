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

import com.gomez.bd.bean.exceptions.NonexistentEntityException;
import com.gomez.bd.bean.exceptions.PreexistingEntityException;
import com.gomez.bd.bean.exceptions.RollbackFailureException;
import com.gomez.bd.modelo.MarcaProducto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * MarcaProductoJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class MarcaProductoJpaController implements Serializable {

    public MarcaProductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MarcaProducto marcaProducto) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (marcaProducto.getProductoList() == null) {
            marcaProducto.setProductoList(new ArrayList<Producto>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : marcaProducto.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getCodigo());
                attachedProductoList.add(productoListProductoToAttach);
            }
            marcaProducto.setProductoList(attachedProductoList);
            em.persist(marcaProducto);
            for (Producto productoListProducto : marcaProducto.getProductoList()) {
                MarcaProducto oldMarcaOfProductoListProducto = productoListProducto.getMarca();
                productoListProducto.setMarca(marcaProducto);
                productoListProducto = em.merge(productoListProducto);
                if (oldMarcaOfProductoListProducto != null) {
                    oldMarcaOfProductoListProducto.getProductoList().remove(productoListProducto);
                    oldMarcaOfProductoListProducto = em.merge(oldMarcaOfProductoListProducto);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMarcaProducto(marcaProducto.getMarcaProducto()) != null) {
                throw new PreexistingEntityException("MarcaProducto " + marcaProducto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MarcaProducto marcaProducto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MarcaProducto persistentMarcaProducto = em.find(MarcaProducto.class, marcaProducto.getMarcaProducto());
            List<Producto> productoListOld = persistentMarcaProducto.getProductoList();
            List<Producto> productoListNew = marcaProducto.getProductoList();
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getCodigo());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            marcaProducto.setProductoList(productoListNew);
            marcaProducto = em.merge(marcaProducto);
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    productoListOldProducto.setMarca(null);
                    productoListOldProducto = em.merge(productoListOldProducto);
                }
            }
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    MarcaProducto oldMarcaOfProductoListNewProducto = productoListNewProducto.getMarca();
                    productoListNewProducto.setMarca(marcaProducto);
                    productoListNewProducto = em.merge(productoListNewProducto);
                    if (oldMarcaOfProductoListNewProducto != null && !oldMarcaOfProductoListNewProducto.equals(marcaProducto)) {
                        oldMarcaOfProductoListNewProducto.getProductoList().remove(productoListNewProducto);
                        oldMarcaOfProductoListNewProducto = em.merge(oldMarcaOfProductoListNewProducto);
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
                String id = marcaProducto.getMarcaProducto();
                if (findMarcaProducto(id) == null) {
                    throw new NonexistentEntityException("The marcaProducto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MarcaProducto marcaProducto;
            try {
                marcaProducto = em.getReference(MarcaProducto.class, id);
                marcaProducto.getMarcaProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The marcaProducto with id " + id + " no longer exists.", enfe);
            }
            List<Producto> productoList = marcaProducto.getProductoList();
            for (Producto productoListProducto : productoList) {
                productoListProducto.setMarca(null);
                productoListProducto = em.merge(productoListProducto);
            }
            em.remove(marcaProducto);
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

    public List<MarcaProducto> findMarcaProductoEntities() {
        return findMarcaProductoEntities(true, -1, -1);
    }

    public List<MarcaProducto> findMarcaProductoEntities(int maxResults, int firstResult) {
        return findMarcaProductoEntities(false, maxResults, firstResult);
    }

    private List<MarcaProducto> findMarcaProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MarcaProducto.class));
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

    public MarcaProducto findMarcaProducto(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MarcaProducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getMarcaProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MarcaProducto> rt = cq.from(MarcaProducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
