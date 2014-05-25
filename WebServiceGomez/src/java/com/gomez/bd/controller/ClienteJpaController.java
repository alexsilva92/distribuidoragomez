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
import com.gomez.bd.modelo.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.PedidoCliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * ClienteJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cliente.getPedidoClienteList() == null) {
            cliente.setPedidoClienteList(new ArrayList<PedidoCliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PedidoCliente> attachedPedidoClienteList = new ArrayList<PedidoCliente>();
            for (PedidoCliente pedidoClienteListPedidoClienteToAttach : cliente.getPedidoClienteList()) {
                pedidoClienteListPedidoClienteToAttach = em.getReference(pedidoClienteListPedidoClienteToAttach.getClass(), pedidoClienteListPedidoClienteToAttach.getIdPedido());
                attachedPedidoClienteList.add(pedidoClienteListPedidoClienteToAttach);
            }
            cliente.setPedidoClienteList(attachedPedidoClienteList);
            em.persist(cliente);
            for (PedidoCliente pedidoClienteListPedidoCliente : cliente.getPedidoClienteList()) {
                Cliente oldClienteOfPedidoClienteListPedidoCliente = pedidoClienteListPedidoCliente.getCliente();
                pedidoClienteListPedidoCliente.setCliente(cliente);
                pedidoClienteListPedidoCliente = em.merge(pedidoClienteListPedidoCliente);
                if (oldClienteOfPedidoClienteListPedidoCliente != null) {
                    oldClienteOfPedidoClienteListPedidoCliente.getPedidoClienteList().remove(pedidoClienteListPedidoCliente);
                    oldClienteOfPedidoClienteListPedidoCliente = em.merge(oldClienteOfPedidoClienteListPedidoCliente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCliente(cliente.getDni()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getDni());
            List<PedidoCliente> pedidoClienteListOld = persistentCliente.getPedidoClienteList();
            List<PedidoCliente> pedidoClienteListNew = cliente.getPedidoClienteList();
            List<String> illegalOrphanMessages = null;
            for (PedidoCliente pedidoClienteListOldPedidoCliente : pedidoClienteListOld) {
                if (!pedidoClienteListNew.contains(pedidoClienteListOldPedidoCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PedidoCliente " + pedidoClienteListOldPedidoCliente + " since its cliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PedidoCliente> attachedPedidoClienteListNew = new ArrayList<PedidoCliente>();
            for (PedidoCliente pedidoClienteListNewPedidoClienteToAttach : pedidoClienteListNew) {
                pedidoClienteListNewPedidoClienteToAttach = em.getReference(pedidoClienteListNewPedidoClienteToAttach.getClass(), pedidoClienteListNewPedidoClienteToAttach.getIdPedido());
                attachedPedidoClienteListNew.add(pedidoClienteListNewPedidoClienteToAttach);
            }
            pedidoClienteListNew = attachedPedidoClienteListNew;
            cliente.setPedidoClienteList(pedidoClienteListNew);
            cliente = em.merge(cliente);
            for (PedidoCliente pedidoClienteListNewPedidoCliente : pedidoClienteListNew) {
                if (!pedidoClienteListOld.contains(pedidoClienteListNewPedidoCliente)) {
                    Cliente oldClienteOfPedidoClienteListNewPedidoCliente = pedidoClienteListNewPedidoCliente.getCliente();
                    pedidoClienteListNewPedidoCliente.setCliente(cliente);
                    pedidoClienteListNewPedidoCliente = em.merge(pedidoClienteListNewPedidoCliente);
                    if (oldClienteOfPedidoClienteListNewPedidoCliente != null && !oldClienteOfPedidoClienteListNewPedidoCliente.equals(cliente)) {
                        oldClienteOfPedidoClienteListNewPedidoCliente.getPedidoClienteList().remove(pedidoClienteListNewPedidoCliente);
                        oldClienteOfPedidoClienteListNewPedidoCliente = em.merge(oldClienteOfPedidoClienteListNewPedidoCliente);
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
                String id = cliente.getDni();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getDni();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PedidoCliente> pedidoClienteListOrphanCheck = cliente.getPedidoClienteList();
            for (PedidoCliente pedidoClienteListOrphanCheckPedidoCliente : pedidoClienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the PedidoCliente " + pedidoClienteListOrphanCheckPedidoCliente + " in its pedidoClienteList field has a non-nullable cliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cliente);
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

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
