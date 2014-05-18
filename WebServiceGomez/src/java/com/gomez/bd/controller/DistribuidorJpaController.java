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
import com.gomez.bd.modelo.Distribuidor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.PedidoDistribuidor;
import java.util.ArrayList;
import java.util.List;
import com.gomez.bd.modelo.TieneDistribuidor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * DistribuidorJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class DistribuidorJpaController implements Serializable {

    public DistribuidorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Distribuidor distribuidor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (distribuidor.getPedidoDistribuidorList() == null) {
            distribuidor.setPedidoDistribuidorList(new ArrayList<PedidoDistribuidor>());
        }
        if (distribuidor.getTieneDistribuidorList() == null) {
            distribuidor.setTieneDistribuidorList(new ArrayList<TieneDistribuidor>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PedidoDistribuidor> attachedPedidoDistribuidorList = new ArrayList<PedidoDistribuidor>();
            for (PedidoDistribuidor pedidoDistribuidorListPedidoDistribuidorToAttach : distribuidor.getPedidoDistribuidorList()) {
                pedidoDistribuidorListPedidoDistribuidorToAttach = em.getReference(pedidoDistribuidorListPedidoDistribuidorToAttach.getClass(), pedidoDistribuidorListPedidoDistribuidorToAttach.getIdPedido());
                attachedPedidoDistribuidorList.add(pedidoDistribuidorListPedidoDistribuidorToAttach);
            }
            distribuidor.setPedidoDistribuidorList(attachedPedidoDistribuidorList);
            List<TieneDistribuidor> attachedTieneDistribuidorList = new ArrayList<TieneDistribuidor>();
            for (TieneDistribuidor tieneDistribuidorListTieneDistribuidorToAttach : distribuidor.getTieneDistribuidorList()) {
                tieneDistribuidorListTieneDistribuidorToAttach = em.getReference(tieneDistribuidorListTieneDistribuidorToAttach.getClass(), tieneDistribuidorListTieneDistribuidorToAttach.getTieneDistribuidorPK());
                attachedTieneDistribuidorList.add(tieneDistribuidorListTieneDistribuidorToAttach);
            }
            distribuidor.setTieneDistribuidorList(attachedTieneDistribuidorList);
            em.persist(distribuidor);
            for (PedidoDistribuidor pedidoDistribuidorListPedidoDistribuidor : distribuidor.getPedidoDistribuidorList()) {
                Distribuidor oldDistribuidorOfPedidoDistribuidorListPedidoDistribuidor = pedidoDistribuidorListPedidoDistribuidor.getDistribuidor();
                pedidoDistribuidorListPedidoDistribuidor.setDistribuidor(distribuidor);
                pedidoDistribuidorListPedidoDistribuidor = em.merge(pedidoDistribuidorListPedidoDistribuidor);
                if (oldDistribuidorOfPedidoDistribuidorListPedidoDistribuidor != null) {
                    oldDistribuidorOfPedidoDistribuidorListPedidoDistribuidor.getPedidoDistribuidorList().remove(pedidoDistribuidorListPedidoDistribuidor);
                    oldDistribuidorOfPedidoDistribuidorListPedidoDistribuidor = em.merge(oldDistribuidorOfPedidoDistribuidorListPedidoDistribuidor);
                }
            }
            for (TieneDistribuidor tieneDistribuidorListTieneDistribuidor : distribuidor.getTieneDistribuidorList()) {
                Distribuidor oldDistribuidor1OfTieneDistribuidorListTieneDistribuidor = tieneDistribuidorListTieneDistribuidor.getDistribuidor1();
                tieneDistribuidorListTieneDistribuidor.setDistribuidor1(distribuidor);
                tieneDistribuidorListTieneDistribuidor = em.merge(tieneDistribuidorListTieneDistribuidor);
                if (oldDistribuidor1OfTieneDistribuidorListTieneDistribuidor != null) {
                    oldDistribuidor1OfTieneDistribuidorListTieneDistribuidor.getTieneDistribuidorList().remove(tieneDistribuidorListTieneDistribuidor);
                    oldDistribuidor1OfTieneDistribuidorListTieneDistribuidor = em.merge(oldDistribuidor1OfTieneDistribuidorListTieneDistribuidor);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDistribuidor(distribuidor.getCifNif()) != null) {
                throw new PreexistingEntityException("Distribuidor " + distribuidor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Distribuidor distribuidor) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Distribuidor persistentDistribuidor = em.find(Distribuidor.class, distribuidor.getCifNif());
            List<PedidoDistribuidor> pedidoDistribuidorListOld = persistentDistribuidor.getPedidoDistribuidorList();
            List<PedidoDistribuidor> pedidoDistribuidorListNew = distribuidor.getPedidoDistribuidorList();
            List<TieneDistribuidor> tieneDistribuidorListOld = persistentDistribuidor.getTieneDistribuidorList();
            List<TieneDistribuidor> tieneDistribuidorListNew = distribuidor.getTieneDistribuidorList();
            List<String> illegalOrphanMessages = null;
            for (PedidoDistribuidor pedidoDistribuidorListOldPedidoDistribuidor : pedidoDistribuidorListOld) {
                if (!pedidoDistribuidorListNew.contains(pedidoDistribuidorListOldPedidoDistribuidor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PedidoDistribuidor " + pedidoDistribuidorListOldPedidoDistribuidor + " since its distribuidor field is not nullable.");
                }
            }
            for (TieneDistribuidor tieneDistribuidorListOldTieneDistribuidor : tieneDistribuidorListOld) {
                if (!tieneDistribuidorListNew.contains(tieneDistribuidorListOldTieneDistribuidor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TieneDistribuidor " + tieneDistribuidorListOldTieneDistribuidor + " since its distribuidor1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PedidoDistribuidor> attachedPedidoDistribuidorListNew = new ArrayList<PedidoDistribuidor>();
            for (PedidoDistribuidor pedidoDistribuidorListNewPedidoDistribuidorToAttach : pedidoDistribuidorListNew) {
                pedidoDistribuidorListNewPedidoDistribuidorToAttach = em.getReference(pedidoDistribuidorListNewPedidoDistribuidorToAttach.getClass(), pedidoDistribuidorListNewPedidoDistribuidorToAttach.getIdPedido());
                attachedPedidoDistribuidorListNew.add(pedidoDistribuidorListNewPedidoDistribuidorToAttach);
            }
            pedidoDistribuidorListNew = attachedPedidoDistribuidorListNew;
            distribuidor.setPedidoDistribuidorList(pedidoDistribuidorListNew);
            List<TieneDistribuidor> attachedTieneDistribuidorListNew = new ArrayList<TieneDistribuidor>();
            for (TieneDistribuidor tieneDistribuidorListNewTieneDistribuidorToAttach : tieneDistribuidorListNew) {
                tieneDistribuidorListNewTieneDistribuidorToAttach = em.getReference(tieneDistribuidorListNewTieneDistribuidorToAttach.getClass(), tieneDistribuidorListNewTieneDistribuidorToAttach.getTieneDistribuidorPK());
                attachedTieneDistribuidorListNew.add(tieneDistribuidorListNewTieneDistribuidorToAttach);
            }
            tieneDistribuidorListNew = attachedTieneDistribuidorListNew;
            distribuidor.setTieneDistribuidorList(tieneDistribuidorListNew);
            distribuidor = em.merge(distribuidor);
            for (PedidoDistribuidor pedidoDistribuidorListNewPedidoDistribuidor : pedidoDistribuidorListNew) {
                if (!pedidoDistribuidorListOld.contains(pedidoDistribuidorListNewPedidoDistribuidor)) {
                    Distribuidor oldDistribuidorOfPedidoDistribuidorListNewPedidoDistribuidor = pedidoDistribuidorListNewPedidoDistribuidor.getDistribuidor();
                    pedidoDistribuidorListNewPedidoDistribuidor.setDistribuidor(distribuidor);
                    pedidoDistribuidorListNewPedidoDistribuidor = em.merge(pedidoDistribuidorListNewPedidoDistribuidor);
                    if (oldDistribuidorOfPedidoDistribuidorListNewPedidoDistribuidor != null && !oldDistribuidorOfPedidoDistribuidorListNewPedidoDistribuidor.equals(distribuidor)) {
                        oldDistribuidorOfPedidoDistribuidorListNewPedidoDistribuidor.getPedidoDistribuidorList().remove(pedidoDistribuidorListNewPedidoDistribuidor);
                        oldDistribuidorOfPedidoDistribuidorListNewPedidoDistribuidor = em.merge(oldDistribuidorOfPedidoDistribuidorListNewPedidoDistribuidor);
                    }
                }
            }
            for (TieneDistribuidor tieneDistribuidorListNewTieneDistribuidor : tieneDistribuidorListNew) {
                if (!tieneDistribuidorListOld.contains(tieneDistribuidorListNewTieneDistribuidor)) {
                    Distribuidor oldDistribuidor1OfTieneDistribuidorListNewTieneDistribuidor = tieneDistribuidorListNewTieneDistribuidor.getDistribuidor1();
                    tieneDistribuidorListNewTieneDistribuidor.setDistribuidor1(distribuidor);
                    tieneDistribuidorListNewTieneDistribuidor = em.merge(tieneDistribuidorListNewTieneDistribuidor);
                    if (oldDistribuidor1OfTieneDistribuidorListNewTieneDistribuidor != null && !oldDistribuidor1OfTieneDistribuidorListNewTieneDistribuidor.equals(distribuidor)) {
                        oldDistribuidor1OfTieneDistribuidorListNewTieneDistribuidor.getTieneDistribuidorList().remove(tieneDistribuidorListNewTieneDistribuidor);
                        oldDistribuidor1OfTieneDistribuidorListNewTieneDistribuidor = em.merge(oldDistribuidor1OfTieneDistribuidorListNewTieneDistribuidor);
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
                String id = distribuidor.getCifNif();
                if (findDistribuidor(id) == null) {
                    throw new NonexistentEntityException("The distribuidor with id " + id + " no longer exists.");
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
            Distribuidor distribuidor;
            try {
                distribuidor = em.getReference(Distribuidor.class, id);
                distribuidor.getCifNif();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The distribuidor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PedidoDistribuidor> pedidoDistribuidorListOrphanCheck = distribuidor.getPedidoDistribuidorList();
            for (PedidoDistribuidor pedidoDistribuidorListOrphanCheckPedidoDistribuidor : pedidoDistribuidorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Distribuidor (" + distribuidor + ") cannot be destroyed since the PedidoDistribuidor " + pedidoDistribuidorListOrphanCheckPedidoDistribuidor + " in its pedidoDistribuidorList field has a non-nullable distribuidor field.");
            }
            List<TieneDistribuidor> tieneDistribuidorListOrphanCheck = distribuidor.getTieneDistribuidorList();
            for (TieneDistribuidor tieneDistribuidorListOrphanCheckTieneDistribuidor : tieneDistribuidorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Distribuidor (" + distribuidor + ") cannot be destroyed since the TieneDistribuidor " + tieneDistribuidorListOrphanCheckTieneDistribuidor + " in its tieneDistribuidorList field has a non-nullable distribuidor1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(distribuidor);
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

    public List<Distribuidor> findDistribuidorEntities() {
        return findDistribuidorEntities(true, -1, -1);
    }

    public List<Distribuidor> findDistribuidorEntities(int maxResults, int firstResult) {
        return findDistribuidorEntities(false, maxResults, firstResult);
    }

    private List<Distribuidor> findDistribuidorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Distribuidor.class));
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

    public Distribuidor findDistribuidor(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Distribuidor.class, id);
        } finally {
            em.close();
        }
    }

    public int getDistribuidorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Distribuidor> rt = cq.from(Distribuidor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
