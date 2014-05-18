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
import com.gomez.bd.modelo.TienePedidoDistribuidor;
import com.gomez.bd.modelo.TienePedidoDistribuidorPK;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 * TienePedidoDistribuidorJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class TienePedidoDistribuidorJpaController implements Serializable {

    public TienePedidoDistribuidorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TienePedidoDistribuidor tienePedidoDistribuidor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tienePedidoDistribuidor.getTienePedidoDistribuidorPK() == null) {
            tienePedidoDistribuidor.setTienePedidoDistribuidorPK(new TienePedidoDistribuidorPK());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(tienePedidoDistribuidor);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTienePedidoDistribuidor(tienePedidoDistribuidor.getTienePedidoDistribuidorPK()) != null) {
                throw new PreexistingEntityException("TienePedidoDistribuidor " + tienePedidoDistribuidor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TienePedidoDistribuidor tienePedidoDistribuidor) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            tienePedidoDistribuidor = em.merge(tienePedidoDistribuidor);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TienePedidoDistribuidorPK id = tienePedidoDistribuidor.getTienePedidoDistribuidorPK();
                if (findTienePedidoDistribuidor(id) == null) {
                    throw new NonexistentEntityException("The tienePedidoDistribuidor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TienePedidoDistribuidorPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TienePedidoDistribuidor tienePedidoDistribuidor;
            try {
                tienePedidoDistribuidor = em.getReference(TienePedidoDistribuidor.class, id);
                tienePedidoDistribuidor.getTienePedidoDistribuidorPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tienePedidoDistribuidor with id " + id + " no longer exists.", enfe);
            }
            em.remove(tienePedidoDistribuidor);
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

    public List<TienePedidoDistribuidor> findTienePedidoDistribuidorEntities() {
        return findTienePedidoDistribuidorEntities(true, -1, -1);
    }

    public List<TienePedidoDistribuidor> findTienePedidoDistribuidorEntities(int maxResults, int firstResult) {
        return findTienePedidoDistribuidorEntities(false, maxResults, firstResult);
    }

    private List<TienePedidoDistribuidor> findTienePedidoDistribuidorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TienePedidoDistribuidor.class));
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

    public TienePedidoDistribuidor findTienePedidoDistribuidor(TienePedidoDistribuidorPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TienePedidoDistribuidor.class, id);
        } finally {
            em.close();
        }
    }

    public int getTienePedidoDistribuidorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TienePedidoDistribuidor> rt = cq.from(TienePedidoDistribuidor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
