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
import com.gomez.bd.modelo.EstadoPedido;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.PedidoDistribuidor;
import java.util.ArrayList;
import java.util.List;
import com.gomez.bd.modelo.PedidoCliente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * EstadoPedidoJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class EstadoPedidoJpaController implements Serializable {

    public void EstadoPedidoJpaController(UserTransaction utx, EntityManagerFactory emf){this.utx = utx;
        this.emf = emf;
}
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoPedido estadoPedido) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (estadoPedido.getPedidoDistribuidorList() == null) {
            estadoPedido.setPedidoDistribuidorList(new ArrayList<PedidoDistribuidor>());
        }
        if (estadoPedido.getPedidoClienteList() == null) {
            estadoPedido.setPedidoClienteList(new ArrayList<PedidoCliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PedidoDistribuidor> attachedPedidoDistribuidorList = new ArrayList<PedidoDistribuidor>();
            for (PedidoDistribuidor pedidoDistribuidorListPedidoDistribuidorToAttach : estadoPedido.getPedidoDistribuidorList()) {
                pedidoDistribuidorListPedidoDistribuidorToAttach = em.getReference(pedidoDistribuidorListPedidoDistribuidorToAttach.getClass(), pedidoDistribuidorListPedidoDistribuidorToAttach.getIdPedido());
                attachedPedidoDistribuidorList.add(pedidoDistribuidorListPedidoDistribuidorToAttach);
            }
            estadoPedido.setPedidoDistribuidorList(attachedPedidoDistribuidorList);
            List<PedidoCliente> attachedPedidoClienteList = new ArrayList<PedidoCliente>();
            for (PedidoCliente pedidoClienteListPedidoClienteToAttach : estadoPedido.getPedidoClienteList()) {
                pedidoClienteListPedidoClienteToAttach = em.getReference(pedidoClienteListPedidoClienteToAttach.getClass(), pedidoClienteListPedidoClienteToAttach.getIdPedido());
                attachedPedidoClienteList.add(pedidoClienteListPedidoClienteToAttach);
            }
            estadoPedido.setPedidoClienteList(attachedPedidoClienteList);
            em.persist(estadoPedido);
            for (PedidoDistribuidor pedidoDistribuidorListPedidoDistribuidor : estadoPedido.getPedidoDistribuidorList()) {
                EstadoPedido oldEstadoOfPedidoDistribuidorListPedidoDistribuidor = pedidoDistribuidorListPedidoDistribuidor.getEstado();
                pedidoDistribuidorListPedidoDistribuidor.setEstado(estadoPedido);
                pedidoDistribuidorListPedidoDistribuidor = em.merge(pedidoDistribuidorListPedidoDistribuidor);
                if (oldEstadoOfPedidoDistribuidorListPedidoDistribuidor != null) {
                    oldEstadoOfPedidoDistribuidorListPedidoDistribuidor.getPedidoDistribuidorList().remove(pedidoDistribuidorListPedidoDistribuidor);
                    oldEstadoOfPedidoDistribuidorListPedidoDistribuidor = em.merge(oldEstadoOfPedidoDistribuidorListPedidoDistribuidor);
                }
            }
            for (PedidoCliente pedidoClienteListPedidoCliente : estadoPedido.getPedidoClienteList()) {
                EstadoPedido oldEstadoOfPedidoClienteListPedidoCliente = pedidoClienteListPedidoCliente.getEstado();
                pedidoClienteListPedidoCliente.setEstado(estadoPedido);
                pedidoClienteListPedidoCliente = em.merge(pedidoClienteListPedidoCliente);
                if (oldEstadoOfPedidoClienteListPedidoCliente != null) {
                    oldEstadoOfPedidoClienteListPedidoCliente.getPedidoClienteList().remove(pedidoClienteListPedidoCliente);
                    oldEstadoOfPedidoClienteListPedidoCliente = em.merge(oldEstadoOfPedidoClienteListPedidoCliente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEstadoPedido(estadoPedido.getEstado()) != null) {
                throw new PreexistingEntityException("EstadoPedido " + estadoPedido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoPedido estadoPedido) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EstadoPedido persistentEstadoPedido = em.find(EstadoPedido.class, estadoPedido.getEstado());
            List<PedidoDistribuidor> pedidoDistribuidorListOld = persistentEstadoPedido.getPedidoDistribuidorList();
            List<PedidoDistribuidor> pedidoDistribuidorListNew = estadoPedido.getPedidoDistribuidorList();
            List<PedidoCliente> pedidoClienteListOld = persistentEstadoPedido.getPedidoClienteList();
            List<PedidoCliente> pedidoClienteListNew = estadoPedido.getPedidoClienteList();
            List<PedidoDistribuidor> attachedPedidoDistribuidorListNew = new ArrayList<PedidoDistribuidor>();
            for (PedidoDistribuidor pedidoDistribuidorListNewPedidoDistribuidorToAttach : pedidoDistribuidorListNew) {
                pedidoDistribuidorListNewPedidoDistribuidorToAttach = em.getReference(pedidoDistribuidorListNewPedidoDistribuidorToAttach.getClass(), pedidoDistribuidorListNewPedidoDistribuidorToAttach.getIdPedido());
                attachedPedidoDistribuidorListNew.add(pedidoDistribuidorListNewPedidoDistribuidorToAttach);
            }
            pedidoDistribuidorListNew = attachedPedidoDistribuidorListNew;
            estadoPedido.setPedidoDistribuidorList(pedidoDistribuidorListNew);
            List<PedidoCliente> attachedPedidoClienteListNew = new ArrayList<PedidoCliente>();
            for (PedidoCliente pedidoClienteListNewPedidoClienteToAttach : pedidoClienteListNew) {
                pedidoClienteListNewPedidoClienteToAttach = em.getReference(pedidoClienteListNewPedidoClienteToAttach.getClass(), pedidoClienteListNewPedidoClienteToAttach.getIdPedido());
                attachedPedidoClienteListNew.add(pedidoClienteListNewPedidoClienteToAttach);
            }
            pedidoClienteListNew = attachedPedidoClienteListNew;
            estadoPedido.setPedidoClienteList(pedidoClienteListNew);
            estadoPedido = em.merge(estadoPedido);
            for (PedidoDistribuidor pedidoDistribuidorListOldPedidoDistribuidor : pedidoDistribuidorListOld) {
                if (!pedidoDistribuidorListNew.contains(pedidoDistribuidorListOldPedidoDistribuidor)) {
                    pedidoDistribuidorListOldPedidoDistribuidor.setEstado(null);
                    pedidoDistribuidorListOldPedidoDistribuidor = em.merge(pedidoDistribuidorListOldPedidoDistribuidor);
                }
            }
            for (PedidoDistribuidor pedidoDistribuidorListNewPedidoDistribuidor : pedidoDistribuidorListNew) {
                if (!pedidoDistribuidorListOld.contains(pedidoDistribuidorListNewPedidoDistribuidor)) {
                    EstadoPedido oldEstadoOfPedidoDistribuidorListNewPedidoDistribuidor = pedidoDistribuidorListNewPedidoDistribuidor.getEstado();
                    pedidoDistribuidorListNewPedidoDistribuidor.setEstado(estadoPedido);
                    pedidoDistribuidorListNewPedidoDistribuidor = em.merge(pedidoDistribuidorListNewPedidoDistribuidor);
                    if (oldEstadoOfPedidoDistribuidorListNewPedidoDistribuidor != null && !oldEstadoOfPedidoDistribuidorListNewPedidoDistribuidor.equals(estadoPedido)) {
                        oldEstadoOfPedidoDistribuidorListNewPedidoDistribuidor.getPedidoDistribuidorList().remove(pedidoDistribuidorListNewPedidoDistribuidor);
                        oldEstadoOfPedidoDistribuidorListNewPedidoDistribuidor = em.merge(oldEstadoOfPedidoDistribuidorListNewPedidoDistribuidor);
                    }
                }
            }
            for (PedidoCliente pedidoClienteListOldPedidoCliente : pedidoClienteListOld) {
                if (!pedidoClienteListNew.contains(pedidoClienteListOldPedidoCliente)) {
                    pedidoClienteListOldPedidoCliente.setEstado(null);
                    pedidoClienteListOldPedidoCliente = em.merge(pedidoClienteListOldPedidoCliente);
                }
            }
            for (PedidoCliente pedidoClienteListNewPedidoCliente : pedidoClienteListNew) {
                if (!pedidoClienteListOld.contains(pedidoClienteListNewPedidoCliente)) {
                    EstadoPedido oldEstadoOfPedidoClienteListNewPedidoCliente = pedidoClienteListNewPedidoCliente.getEstado();
                    pedidoClienteListNewPedidoCliente.setEstado(estadoPedido);
                    pedidoClienteListNewPedidoCliente = em.merge(pedidoClienteListNewPedidoCliente);
                    if (oldEstadoOfPedidoClienteListNewPedidoCliente != null && !oldEstadoOfPedidoClienteListNewPedidoCliente.equals(estadoPedido)) {
                        oldEstadoOfPedidoClienteListNewPedidoCliente.getPedidoClienteList().remove(pedidoClienteListNewPedidoCliente);
                        oldEstadoOfPedidoClienteListNewPedidoCliente = em.merge(oldEstadoOfPedidoClienteListNewPedidoCliente);
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
                String id = estadoPedido.getEstado();
                if (findEstadoPedido(id) == null) {
                    throw new NonexistentEntityException("The estadoPedido with id " + id + " no longer exists.");
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
            EstadoPedido estadoPedido;
            try {
                estadoPedido = em.getReference(EstadoPedido.class, id);
                estadoPedido.getEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoPedido with id " + id + " no longer exists.", enfe);
            }
            List<PedidoDistribuidor> pedidoDistribuidorList = estadoPedido.getPedidoDistribuidorList();
            for (PedidoDistribuidor pedidoDistribuidorListPedidoDistribuidor : pedidoDistribuidorList) {
                pedidoDistribuidorListPedidoDistribuidor.setEstado(null);
                pedidoDistribuidorListPedidoDistribuidor = em.merge(pedidoDistribuidorListPedidoDistribuidor);
            }
            List<PedidoCliente> pedidoClienteList = estadoPedido.getPedidoClienteList();
            for (PedidoCliente pedidoClienteListPedidoCliente : pedidoClienteList) {
                pedidoClienteListPedidoCliente.setEstado(null);
                pedidoClienteListPedidoCliente = em.merge(pedidoClienteListPedidoCliente);
            }
            em.remove(estadoPedido);
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

    public List<EstadoPedido> findEstadoPedidoEntities() {
        return findEstadoPedidoEntities(true, -1, -1);
    }

    public List<EstadoPedido> findEstadoPedidoEntities(int maxResults, int firstResult) {
        return findEstadoPedidoEntities(false, maxResults, firstResult);
    }

    private List<EstadoPedido> findEstadoPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoPedido.class));
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

    public EstadoPedido findEstadoPedido(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoPedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoPedido> rt = cq.from(EstadoPedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
