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
import com.gomez.bd.modelo.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.Distribuidor;
import com.gomez.bd.modelo.TieneDistribuidor;
import com.gomez.bd.modelo.TieneDistribuidorPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * TieneDistribuidorJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class TieneDistribuidorJpaController implements Serializable {

    public TieneDistribuidorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TieneDistribuidor tieneDistribuidor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tieneDistribuidor.getTieneDistribuidorPK() == null) {
            tieneDistribuidor.setTieneDistribuidorPK(new TieneDistribuidorPK());
        }
        tieneDistribuidor.getTieneDistribuidorPK().setDistribuidor(tieneDistribuidor.getDistribuidor1().getCifNif());
        tieneDistribuidor.getTieneDistribuidorPK().setProducto(tieneDistribuidor.getProducto1().getCodigo());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Distribuidor distribuidor1 = tieneDistribuidor.getDistribuidor1();
            if (distribuidor1 != null) {
                distribuidor1 = em.getReference(distribuidor1.getClass(), distribuidor1.getCifNif());
                tieneDistribuidor.setDistribuidor1(distribuidor1);
            }
            em.persist(tieneDistribuidor);
            if (distribuidor1 != null) {
                distribuidor1.getTieneDistribuidorList().add(tieneDistribuidor);
                distribuidor1 = em.merge(distribuidor1);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTieneDistribuidor(tieneDistribuidor.getTieneDistribuidorPK()) != null) {
                throw new PreexistingEntityException("TieneDistribuidor " + tieneDistribuidor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TieneDistribuidor tieneDistribuidor) throws NonexistentEntityException, RollbackFailureException, Exception {
        tieneDistribuidor.getTieneDistribuidorPK().setDistribuidor(tieneDistribuidor.getDistribuidor1().getCifNif());
        tieneDistribuidor.getTieneDistribuidorPK().setProducto(tieneDistribuidor.getProducto1().getCodigo());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TieneDistribuidor persistentTieneDistribuidor = em.find(TieneDistribuidor.class, tieneDistribuidor.getTieneDistribuidorPK());
            Distribuidor distribuidor1Old = persistentTieneDistribuidor.getDistribuidor1();
            Distribuidor distribuidor1New = tieneDistribuidor.getDistribuidor1();
            if (distribuidor1New != null) {
                distribuidor1New = em.getReference(distribuidor1New.getClass(), distribuidor1New.getCifNif());
                tieneDistribuidor.setDistribuidor1(distribuidor1New);
            }
            tieneDistribuidor = em.merge(tieneDistribuidor);
            if (distribuidor1Old != null && !distribuidor1Old.equals(distribuidor1New)) {
                distribuidor1Old.getTieneDistribuidorList().remove(tieneDistribuidor);
                distribuidor1Old = em.merge(distribuidor1Old);
            }
            if (distribuidor1New != null && !distribuidor1New.equals(distribuidor1Old)) {
                distribuidor1New.getTieneDistribuidorList().add(tieneDistribuidor);
                distribuidor1New = em.merge(distribuidor1New);
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
                TieneDistribuidorPK id = tieneDistribuidor.getTieneDistribuidorPK();
                if (findTieneDistribuidor(id) == null) {
                    throw new NonexistentEntityException("The tieneDistribuidor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TieneDistribuidorPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TieneDistribuidor tieneDistribuidor;
            try {
                tieneDistribuidor = em.getReference(TieneDistribuidor.class, id);
                tieneDistribuidor.getTieneDistribuidorPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tieneDistribuidor with id " + id + " no longer exists.", enfe);
            }
            Distribuidor distribuidor1 = tieneDistribuidor.getDistribuidor1();
            if (distribuidor1 != null) {
                distribuidor1.getTieneDistribuidorList().remove(tieneDistribuidor);
                distribuidor1 = em.merge(distribuidor1);
            }
            em.remove(tieneDistribuidor);
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

    public List<TieneDistribuidor> findTieneDistribuidorEntities() {
        return findTieneDistribuidorEntities(true, -1, -1);
    }

    public List<TieneDistribuidor> findTieneDistribuidorEntities(int maxResults, int firstResult) {
        return findTieneDistribuidorEntities(false, maxResults, firstResult);
    }

    private List<TieneDistribuidor> findTieneDistribuidorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TieneDistribuidor.class));
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

    public TieneDistribuidor findTieneDistribuidor(TieneDistribuidorPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TieneDistribuidor.class, id);
        } finally {
            em.close();
        }
    }

    public int getTieneDistribuidorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TieneDistribuidor> rt = cq.from(TieneDistribuidor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<TieneDistribuidor> getProductos(String distribuidor){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT p FROM TieneDistribuidor p WHERE "
                + "p.distribuidor = \"E20304050\"");
        //Distribuidor _distribuidor = em.find(Distribuidor.class, distribuidor);
        //q.setParameter("distribuidor",_distribuidor);
        
       return q.getResultList();     
    }
}
