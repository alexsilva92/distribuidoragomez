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
import com.gomez.bd.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.EstadoPedido;
import com.gomez.bd.modelo.Empleado;
import com.gomez.bd.modelo.Distribuidor;
import com.gomez.bd.modelo.PedidoDistribuidor;
import com.gomez.bd.modelo.TienePedidoDistribuidor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * PedidoDistribuidorJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class PedidoDistribuidorJpaController implements Serializable {

    public PedidoDistribuidorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PedidoDistribuidor pedidoDistribuidor) throws RollbackFailureException, Exception {
        if (pedidoDistribuidor.getTienePedidoDistribuidorList() == null) {
            pedidoDistribuidor.setTienePedidoDistribuidorList(new ArrayList<TienePedidoDistribuidor>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EstadoPedido estado = pedidoDistribuidor.getEstado();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getEstado());
                pedidoDistribuidor.setEstado(estado);
            }
            Empleado empleado = pedidoDistribuidor.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getDni());
                pedidoDistribuidor.setEmpleado(empleado);
            }
            Distribuidor distribuidor = pedidoDistribuidor.getDistribuidor();
            if (distribuidor != null) {
                distribuidor = em.getReference(distribuidor.getClass(), distribuidor.getCifNif());
                pedidoDistribuidor.setDistribuidor(distribuidor);
            }
            List<TienePedidoDistribuidor> attachedTienePedidoDistribuidorList = new ArrayList<TienePedidoDistribuidor>();
            for (TienePedidoDistribuidor tienePedidoDistribuidorListTienePedidoDistribuidorToAttach : pedidoDistribuidor.getTienePedidoDistribuidorList()) {
                tienePedidoDistribuidorListTienePedidoDistribuidorToAttach = em.getReference(tienePedidoDistribuidorListTienePedidoDistribuidorToAttach.getClass(), tienePedidoDistribuidorListTienePedidoDistribuidorToAttach.getTienePedidoDistribuidorPK());
                attachedTienePedidoDistribuidorList.add(tienePedidoDistribuidorListTienePedidoDistribuidorToAttach);
            }
            pedidoDistribuidor.setTienePedidoDistribuidorList(attachedTienePedidoDistribuidorList);
            em.persist(pedidoDistribuidor);
            if (estado != null) {
                estado.getPedidoDistribuidorList().add(pedidoDistribuidor);
                estado = em.merge(estado);
            }
            if (empleado != null) {
                empleado.getPedidoDistribuidorList().add(pedidoDistribuidor);
                empleado = em.merge(empleado);
            }
            if (distribuidor != null) {
                distribuidor.getPedidoDistribuidorList().add(pedidoDistribuidor);
                distribuidor = em.merge(distribuidor);
            }
            for (TienePedidoDistribuidor tienePedidoDistribuidorListTienePedidoDistribuidor : pedidoDistribuidor.getTienePedidoDistribuidorList()) {
                PedidoDistribuidor oldPedidoDistribuidorOfTienePedidoDistribuidorListTienePedidoDistribuidor = tienePedidoDistribuidorListTienePedidoDistribuidor.getPedidoDistribuidor();
                tienePedidoDistribuidorListTienePedidoDistribuidor.setPedidoDistribuidor(pedidoDistribuidor);
                tienePedidoDistribuidorListTienePedidoDistribuidor = em.merge(tienePedidoDistribuidorListTienePedidoDistribuidor);
                if (oldPedidoDistribuidorOfTienePedidoDistribuidorListTienePedidoDistribuidor != null) {
                    oldPedidoDistribuidorOfTienePedidoDistribuidorListTienePedidoDistribuidor.getTienePedidoDistribuidorList().remove(tienePedidoDistribuidorListTienePedidoDistribuidor);
                    oldPedidoDistribuidorOfTienePedidoDistribuidorListTienePedidoDistribuidor = em.merge(oldPedidoDistribuidorOfTienePedidoDistribuidorListTienePedidoDistribuidor);
                }
            }
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

    public void edit(PedidoDistribuidor pedidoDistribuidor) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PedidoDistribuidor persistentPedidoDistribuidor = em.find(PedidoDistribuidor.class, pedidoDistribuidor.getIdPedido());
            EstadoPedido estadoOld = persistentPedidoDistribuidor.getEstado();
            EstadoPedido estadoNew = pedidoDistribuidor.getEstado();
            Empleado empleadoOld = persistentPedidoDistribuidor.getEmpleado();
            Empleado empleadoNew = pedidoDistribuidor.getEmpleado();
            Distribuidor distribuidorOld = persistentPedidoDistribuidor.getDistribuidor();
            Distribuidor distribuidorNew = pedidoDistribuidor.getDistribuidor();
            List<TienePedidoDistribuidor> tienePedidoDistribuidorListOld = persistentPedidoDistribuidor.getTienePedidoDistribuidorList();
            List<TienePedidoDistribuidor> tienePedidoDistribuidorListNew = pedidoDistribuidor.getTienePedidoDistribuidorList();
            List<String> illegalOrphanMessages = null;
            for (TienePedidoDistribuidor tienePedidoDistribuidorListOldTienePedidoDistribuidor : tienePedidoDistribuidorListOld) {
                if (!tienePedidoDistribuidorListNew.contains(tienePedidoDistribuidorListOldTienePedidoDistribuidor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TienePedidoDistribuidor " + tienePedidoDistribuidorListOldTienePedidoDistribuidor + " since its pedidoDistribuidor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getEstado());
                pedidoDistribuidor.setEstado(estadoNew);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getDni());
                pedidoDistribuidor.setEmpleado(empleadoNew);
            }
            if (distribuidorNew != null) {
                distribuidorNew = em.getReference(distribuidorNew.getClass(), distribuidorNew.getCifNif());
                pedidoDistribuidor.setDistribuidor(distribuidorNew);
            }
            List<TienePedidoDistribuidor> attachedTienePedidoDistribuidorListNew = new ArrayList<TienePedidoDistribuidor>();
            for (TienePedidoDistribuidor tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach : tienePedidoDistribuidorListNew) {
                tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach = em.getReference(tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach.getClass(), tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach.getTienePedidoDistribuidorPK());
                attachedTienePedidoDistribuidorListNew.add(tienePedidoDistribuidorListNewTienePedidoDistribuidorToAttach);
            }
            tienePedidoDistribuidorListNew = attachedTienePedidoDistribuidorListNew;
            pedidoDistribuidor.setTienePedidoDistribuidorList(tienePedidoDistribuidorListNew);
            pedidoDistribuidor = em.merge(pedidoDistribuidor);
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getPedidoDistribuidorList().remove(pedidoDistribuidor);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getPedidoDistribuidorList().add(pedidoDistribuidor);
                estadoNew = em.merge(estadoNew);
            }
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                empleadoOld.getPedidoDistribuidorList().remove(pedidoDistribuidor);
                empleadoOld = em.merge(empleadoOld);
            }
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                empleadoNew.getPedidoDistribuidorList().add(pedidoDistribuidor);
                empleadoNew = em.merge(empleadoNew);
            }
            if (distribuidorOld != null && !distribuidorOld.equals(distribuidorNew)) {
                distribuidorOld.getPedidoDistribuidorList().remove(pedidoDistribuidor);
                distribuidorOld = em.merge(distribuidorOld);
            }
            if (distribuidorNew != null && !distribuidorNew.equals(distribuidorOld)) {
                distribuidorNew.getPedidoDistribuidorList().add(pedidoDistribuidor);
                distribuidorNew = em.merge(distribuidorNew);
            }
            for (TienePedidoDistribuidor tienePedidoDistribuidorListNewTienePedidoDistribuidor : tienePedidoDistribuidorListNew) {
                if (!tienePedidoDistribuidorListOld.contains(tienePedidoDistribuidorListNewTienePedidoDistribuidor)) {
                    PedidoDistribuidor oldPedidoDistribuidorOfTienePedidoDistribuidorListNewTienePedidoDistribuidor = tienePedidoDistribuidorListNewTienePedidoDistribuidor.getPedidoDistribuidor();
                    tienePedidoDistribuidorListNewTienePedidoDistribuidor.setPedidoDistribuidor(pedidoDistribuidor);
                    tienePedidoDistribuidorListNewTienePedidoDistribuidor = em.merge(tienePedidoDistribuidorListNewTienePedidoDistribuidor);
                    if (oldPedidoDistribuidorOfTienePedidoDistribuidorListNewTienePedidoDistribuidor != null && !oldPedidoDistribuidorOfTienePedidoDistribuidorListNewTienePedidoDistribuidor.equals(pedidoDistribuidor)) {
                        oldPedidoDistribuidorOfTienePedidoDistribuidorListNewTienePedidoDistribuidor.getTienePedidoDistribuidorList().remove(tienePedidoDistribuidorListNewTienePedidoDistribuidor);
                        oldPedidoDistribuidorOfTienePedidoDistribuidorListNewTienePedidoDistribuidor = em.merge(oldPedidoDistribuidorOfTienePedidoDistribuidorListNewTienePedidoDistribuidor);
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
                Integer id = pedidoDistribuidor.getIdPedido();
                if (findPedidoDistribuidor(id) == null) {
                    throw new NonexistentEntityException("The pedidoDistribuidor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PedidoDistribuidor pedidoDistribuidor;
            try {
                pedidoDistribuidor = em.getReference(PedidoDistribuidor.class, id);
                pedidoDistribuidor.getIdPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedidoDistribuidor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TienePedidoDistribuidor> tienePedidoDistribuidorListOrphanCheck = pedidoDistribuidor.getTienePedidoDistribuidorList();
            for (TienePedidoDistribuidor tienePedidoDistribuidorListOrphanCheckTienePedidoDistribuidor : tienePedidoDistribuidorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PedidoDistribuidor (" + pedidoDistribuidor + ") cannot be destroyed since the TienePedidoDistribuidor " + tienePedidoDistribuidorListOrphanCheckTienePedidoDistribuidor + " in its tienePedidoDistribuidorList field has a non-nullable pedidoDistribuidor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EstadoPedido estado = pedidoDistribuidor.getEstado();
            if (estado != null) {
                estado.getPedidoDistribuidorList().remove(pedidoDistribuidor);
                estado = em.merge(estado);
            }
            Empleado empleado = pedidoDistribuidor.getEmpleado();
            if (empleado != null) {
                empleado.getPedidoDistribuidorList().remove(pedidoDistribuidor);
                empleado = em.merge(empleado);
            }
            Distribuidor distribuidor = pedidoDistribuidor.getDistribuidor();
            if (distribuidor != null) {
                distribuidor.getPedidoDistribuidorList().remove(pedidoDistribuidor);
                distribuidor = em.merge(distribuidor);
            }
            em.remove(pedidoDistribuidor);
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

    public List<PedidoDistribuidor> findPedidoDistribuidorEntities() {
        return findPedidoDistribuidorEntities(true, -1, -1);
    }

    public List<PedidoDistribuidor> findPedidoDistribuidorEntities(int maxResults, int firstResult) {
        return findPedidoDistribuidorEntities(false, maxResults, firstResult);
    }

    private List<PedidoDistribuidor> findPedidoDistribuidorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PedidoDistribuidor.class));
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

    public PedidoDistribuidor findPedidoDistribuidor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PedidoDistribuidor.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoDistribuidorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PedidoDistribuidor> rt = cq.from(PedidoDistribuidor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
