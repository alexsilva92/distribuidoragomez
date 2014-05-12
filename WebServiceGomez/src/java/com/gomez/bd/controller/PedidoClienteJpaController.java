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
import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.Usuario;
import com.gomez.bd.modelo.TienePedidoCliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 * PedidoClienteJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class PedidoClienteJpaController implements Serializable {

    public PedidoClienteJpaController(UserTransaction utx, EntityManagerFactory emf){this.utx = utx;
        this.emf = emf;
}
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PedidoCliente pedidoCliente) throws RollbackFailureException, Exception {
        if (pedidoCliente.getTienePedidoClienteList() == null) {
            pedidoCliente.setTienePedidoClienteList(new ArrayList<TienePedidoCliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EstadoPedido estado = pedidoCliente.getEstado();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getEstado());
                pedidoCliente.setEstado(estado);
            }
            Empleado empleado = pedidoCliente.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getDni());
                pedidoCliente.setEmpleado(empleado);
            }
            Usuario cliente = pedidoCliente.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getDni());
                pedidoCliente.setCliente(cliente);
            }
            List<TienePedidoCliente> attachedTienePedidoClienteList = new ArrayList<TienePedidoCliente>();
            for (TienePedidoCliente tienePedidoClienteListTienePedidoClienteToAttach : pedidoCliente.getTienePedidoClienteList()) {
                tienePedidoClienteListTienePedidoClienteToAttach = em.getReference(tienePedidoClienteListTienePedidoClienteToAttach.getClass(), tienePedidoClienteListTienePedidoClienteToAttach.getTienePedidoClientePK());
                attachedTienePedidoClienteList.add(tienePedidoClienteListTienePedidoClienteToAttach);
            }
            pedidoCliente.setTienePedidoClienteList(attachedTienePedidoClienteList);
            em.persist(pedidoCliente);
            if (estado != null) {
                estado.getPedidoClienteList().add(pedidoCliente);
                estado = em.merge(estado);
            }
            if (empleado != null) {
                empleado.getPedidoClienteList().add(pedidoCliente);
                empleado = em.merge(empleado);
            }
            if (cliente != null) {
                cliente.getPedidoClienteList().add(pedidoCliente);
                cliente = em.merge(cliente);
            }
            for (TienePedidoCliente tienePedidoClienteListTienePedidoCliente : pedidoCliente.getTienePedidoClienteList()) {
                PedidoCliente oldPedidoClienteOfTienePedidoClienteListTienePedidoCliente = tienePedidoClienteListTienePedidoCliente.getPedidoCliente();
                tienePedidoClienteListTienePedidoCliente.setPedidoCliente(pedidoCliente);
                tienePedidoClienteListTienePedidoCliente = em.merge(tienePedidoClienteListTienePedidoCliente);
                if (oldPedidoClienteOfTienePedidoClienteListTienePedidoCliente != null) {
                    oldPedidoClienteOfTienePedidoClienteListTienePedidoCliente.getTienePedidoClienteList().remove(tienePedidoClienteListTienePedidoCliente);
                    oldPedidoClienteOfTienePedidoClienteListTienePedidoCliente = em.merge(oldPedidoClienteOfTienePedidoClienteListTienePedidoCliente);
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

    public void edit(PedidoCliente pedidoCliente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PedidoCliente persistentPedidoCliente = em.find(PedidoCliente.class, pedidoCliente.getIdPedido());
            EstadoPedido estadoOld = persistentPedidoCliente.getEstado();
            EstadoPedido estadoNew = pedidoCliente.getEstado();
            Empleado empleadoOld = persistentPedidoCliente.getEmpleado();
            Empleado empleadoNew = pedidoCliente.getEmpleado();
            Usuario clienteOld = persistentPedidoCliente.getCliente();
            Usuario clienteNew = pedidoCliente.getCliente();
            List<TienePedidoCliente> tienePedidoClienteListOld = persistentPedidoCliente.getTienePedidoClienteList();
            List<TienePedidoCliente> tienePedidoClienteListNew = pedidoCliente.getTienePedidoClienteList();
            List<String> illegalOrphanMessages = null;
            for (TienePedidoCliente tienePedidoClienteListOldTienePedidoCliente : tienePedidoClienteListOld) {
                if (!tienePedidoClienteListNew.contains(tienePedidoClienteListOldTienePedidoCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TienePedidoCliente " + tienePedidoClienteListOldTienePedidoCliente + " since its pedidoCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getEstado());
                pedidoCliente.setEstado(estadoNew);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getDni());
                pedidoCliente.setEmpleado(empleadoNew);
            }
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getDni());
                pedidoCliente.setCliente(clienteNew);
            }
            List<TienePedidoCliente> attachedTienePedidoClienteListNew = new ArrayList<TienePedidoCliente>();
            for (TienePedidoCliente tienePedidoClienteListNewTienePedidoClienteToAttach : tienePedidoClienteListNew) {
                tienePedidoClienteListNewTienePedidoClienteToAttach = em.getReference(tienePedidoClienteListNewTienePedidoClienteToAttach.getClass(), tienePedidoClienteListNewTienePedidoClienteToAttach.getTienePedidoClientePK());
                attachedTienePedidoClienteListNew.add(tienePedidoClienteListNewTienePedidoClienteToAttach);
            }
            tienePedidoClienteListNew = attachedTienePedidoClienteListNew;
            pedidoCliente.setTienePedidoClienteList(tienePedidoClienteListNew);
            pedidoCliente = em.merge(pedidoCliente);
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getPedidoClienteList().remove(pedidoCliente);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getPedidoClienteList().add(pedidoCliente);
                estadoNew = em.merge(estadoNew);
            }
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                empleadoOld.getPedidoClienteList().remove(pedidoCliente);
                empleadoOld = em.merge(empleadoOld);
            }
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                empleadoNew.getPedidoClienteList().add(pedidoCliente);
                empleadoNew = em.merge(empleadoNew);
            }
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getPedidoClienteList().remove(pedidoCliente);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getPedidoClienteList().add(pedidoCliente);
                clienteNew = em.merge(clienteNew);
            }
            for (TienePedidoCliente tienePedidoClienteListNewTienePedidoCliente : tienePedidoClienteListNew) {
                if (!tienePedidoClienteListOld.contains(tienePedidoClienteListNewTienePedidoCliente)) {
                    PedidoCliente oldPedidoClienteOfTienePedidoClienteListNewTienePedidoCliente = tienePedidoClienteListNewTienePedidoCliente.getPedidoCliente();
                    tienePedidoClienteListNewTienePedidoCliente.setPedidoCliente(pedidoCliente);
                    tienePedidoClienteListNewTienePedidoCliente = em.merge(tienePedidoClienteListNewTienePedidoCliente);
                    if (oldPedidoClienteOfTienePedidoClienteListNewTienePedidoCliente != null && !oldPedidoClienteOfTienePedidoClienteListNewTienePedidoCliente.equals(pedidoCliente)) {
                        oldPedidoClienteOfTienePedidoClienteListNewTienePedidoCliente.getTienePedidoClienteList().remove(tienePedidoClienteListNewTienePedidoCliente);
                        oldPedidoClienteOfTienePedidoClienteListNewTienePedidoCliente = em.merge(oldPedidoClienteOfTienePedidoClienteListNewTienePedidoCliente);
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
                Integer id = pedidoCliente.getIdPedido();
                if (findPedidoCliente(id) == null) {
                    throw new NonexistentEntityException("The pedidoCliente with id " + id + " no longer exists.");
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
            PedidoCliente pedidoCliente;
            try {
                pedidoCliente = em.getReference(PedidoCliente.class, id);
                pedidoCliente.getIdPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedidoCliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TienePedidoCliente> tienePedidoClienteListOrphanCheck = pedidoCliente.getTienePedidoClienteList();
            for (TienePedidoCliente tienePedidoClienteListOrphanCheckTienePedidoCliente : tienePedidoClienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PedidoCliente (" + pedidoCliente + ") cannot be destroyed since the TienePedidoCliente " + tienePedidoClienteListOrphanCheckTienePedidoCliente + " in its tienePedidoClienteList field has a non-nullable pedidoCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EstadoPedido estado = pedidoCliente.getEstado();
            if (estado != null) {
                estado.getPedidoClienteList().remove(pedidoCliente);
                estado = em.merge(estado);
            }
            Empleado empleado = pedidoCliente.getEmpleado();
            if (empleado != null) {
                empleado.getPedidoClienteList().remove(pedidoCliente);
                empleado = em.merge(empleado);
            }
            Usuario cliente = pedidoCliente.getCliente();
            if (cliente != null) {
                cliente.getPedidoClienteList().remove(pedidoCliente);
                cliente = em.merge(cliente);
            }
            em.remove(pedidoCliente);
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

    public List<PedidoCliente> findPedidoClienteEntities() {
        return findPedidoClienteEntities(true, -1, -1);
    }

    public List<PedidoCliente> findPedidoClienteEntities(int maxResults, int firstResult) {
        return findPedidoClienteEntities(false, maxResults, firstResult);
    }

    private List<PedidoCliente> findPedidoClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PedidoCliente.class));
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

    public PedidoCliente findPedidoCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PedidoCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PedidoCliente> rt = cq.from(PedidoCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public List<PedidoCliente> getPedidosPorEmpleado(String dni) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT p FROM PedidoCliente p WHERE "
                + "p.empleado = :empleado");
        Empleado empleado = em.find(Empleado.class, dni);
        q.setParameter("empleado",empleado);
        
       return q.getResultList();  
    }
    
    public List<PedidoCliente> getPedidosPorCliente(String dni) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT p FROM PedidoCliente p WHERE "
                + "p.cliente = :cliente");
        Usuario cliente = em.find(Usuario.class, dni);
        q.setParameter("cliente",cliente);
        
       return q.getResultList();  
    }
}
