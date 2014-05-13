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
import com.gomez.bd.modelo.Empleado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.gomez.bd.modelo.PedidoDistribuidor;
import java.util.ArrayList;
import java.util.List;
import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 * EmpleadoJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(UserTransaction utx, EntityManagerFactory emf){this.utx = utx;
        this.emf = emf;
}
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (empleado.getPedidoDistribuidorList() == null) {
            empleado.setPedidoDistribuidorList(new ArrayList<PedidoDistribuidor>());
        }
        if (empleado.getPedidoClienteList() == null) {
            empleado.setPedidoClienteList(new ArrayList<PedidoCliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PedidoDistribuidor> attachedPedidoDistribuidorList = new ArrayList<PedidoDistribuidor>();
            for (PedidoDistribuidor pedidoDistribuidorListPedidoDistribuidorToAttach : empleado.getPedidoDistribuidorList()) {
                pedidoDistribuidorListPedidoDistribuidorToAttach = em.getReference(pedidoDistribuidorListPedidoDistribuidorToAttach.getClass(), pedidoDistribuidorListPedidoDistribuidorToAttach.getIdPedido());
                attachedPedidoDistribuidorList.add(pedidoDistribuidorListPedidoDistribuidorToAttach);
            }
            empleado.setPedidoDistribuidorList(attachedPedidoDistribuidorList);
            List<PedidoCliente> attachedPedidoClienteList = new ArrayList<PedidoCliente>();
            for (PedidoCliente pedidoClienteListPedidoClienteToAttach : empleado.getPedidoClienteList()) {
                pedidoClienteListPedidoClienteToAttach = em.getReference(pedidoClienteListPedidoClienteToAttach.getClass(), pedidoClienteListPedidoClienteToAttach.getIdPedido());
                attachedPedidoClienteList.add(pedidoClienteListPedidoClienteToAttach);
            }
            empleado.setPedidoClienteList(attachedPedidoClienteList);
            em.persist(empleado);
            for (PedidoDistribuidor pedidoDistribuidorListPedidoDistribuidor : empleado.getPedidoDistribuidorList()) {
                Empleado oldEmpleadoOfPedidoDistribuidorListPedidoDistribuidor = pedidoDistribuidorListPedidoDistribuidor.getEmpleado();
                pedidoDistribuidorListPedidoDistribuidor.setEmpleado(empleado);
                pedidoDistribuidorListPedidoDistribuidor = em.merge(pedidoDistribuidorListPedidoDistribuidor);
                if (oldEmpleadoOfPedidoDistribuidorListPedidoDistribuidor != null) {
                    oldEmpleadoOfPedidoDistribuidorListPedidoDistribuidor.getPedidoDistribuidorList().remove(pedidoDistribuidorListPedidoDistribuidor);
                    oldEmpleadoOfPedidoDistribuidorListPedidoDistribuidor = em.merge(oldEmpleadoOfPedidoDistribuidorListPedidoDistribuidor);
                }
            }
            for (PedidoCliente pedidoClienteListPedidoCliente : empleado.getPedidoClienteList()) {
                Empleado oldEmpleadoOfPedidoClienteListPedidoCliente = pedidoClienteListPedidoCliente.getEmpleado();
                pedidoClienteListPedidoCliente.setEmpleado(empleado);
                pedidoClienteListPedidoCliente = em.merge(pedidoClienteListPedidoCliente);
                if (oldEmpleadoOfPedidoClienteListPedidoCliente != null) {
                    oldEmpleadoOfPedidoClienteListPedidoCliente.getPedidoClienteList().remove(pedidoClienteListPedidoCliente);
                    oldEmpleadoOfPedidoClienteListPedidoCliente = em.merge(oldEmpleadoOfPedidoClienteListPedidoCliente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEmpleado(empleado.getDni()) != null) {
                throw new PreexistingEntityException("Empleado " + empleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getDni());
            List<PedidoDistribuidor> pedidoDistribuidorListOld = persistentEmpleado.getPedidoDistribuidorList();
            List<PedidoDistribuidor> pedidoDistribuidorListNew = empleado.getPedidoDistribuidorList();
            List<PedidoCliente> pedidoClienteListOld = persistentEmpleado.getPedidoClienteList();
            List<PedidoCliente> pedidoClienteListNew = empleado.getPedidoClienteList();
            List<PedidoDistribuidor> attachedPedidoDistribuidorListNew = new ArrayList<PedidoDistribuidor>();
            for (PedidoDistribuidor pedidoDistribuidorListNewPedidoDistribuidorToAttach : pedidoDistribuidorListNew) {
                pedidoDistribuidorListNewPedidoDistribuidorToAttach = em.getReference(pedidoDistribuidorListNewPedidoDistribuidorToAttach.getClass(), pedidoDistribuidorListNewPedidoDistribuidorToAttach.getIdPedido());
                attachedPedidoDistribuidorListNew.add(pedidoDistribuidorListNewPedidoDistribuidorToAttach);
            }
            pedidoDistribuidorListNew = attachedPedidoDistribuidorListNew;
            empleado.setPedidoDistribuidorList(pedidoDistribuidorListNew);
            List<PedidoCliente> attachedPedidoClienteListNew = new ArrayList<PedidoCliente>();
            for (PedidoCliente pedidoClienteListNewPedidoClienteToAttach : pedidoClienteListNew) {
                pedidoClienteListNewPedidoClienteToAttach = em.getReference(pedidoClienteListNewPedidoClienteToAttach.getClass(), pedidoClienteListNewPedidoClienteToAttach.getIdPedido());
                attachedPedidoClienteListNew.add(pedidoClienteListNewPedidoClienteToAttach);
            }
            pedidoClienteListNew = attachedPedidoClienteListNew;
            empleado.setPedidoClienteList(pedidoClienteListNew);
            empleado = em.merge(empleado);
            for (PedidoDistribuidor pedidoDistribuidorListOldPedidoDistribuidor : pedidoDistribuidorListOld) {
                if (!pedidoDistribuidorListNew.contains(pedidoDistribuidorListOldPedidoDistribuidor)) {
                    pedidoDistribuidorListOldPedidoDistribuidor.setEmpleado(null);
                    pedidoDistribuidorListOldPedidoDistribuidor = em.merge(pedidoDistribuidorListOldPedidoDistribuidor);
                }
            }
            for (PedidoDistribuidor pedidoDistribuidorListNewPedidoDistribuidor : pedidoDistribuidorListNew) {
                if (!pedidoDistribuidorListOld.contains(pedidoDistribuidorListNewPedidoDistribuidor)) {
                    Empleado oldEmpleadoOfPedidoDistribuidorListNewPedidoDistribuidor = pedidoDistribuidorListNewPedidoDistribuidor.getEmpleado();
                    pedidoDistribuidorListNewPedidoDistribuidor.setEmpleado(empleado);
                    pedidoDistribuidorListNewPedidoDistribuidor = em.merge(pedidoDistribuidorListNewPedidoDistribuidor);
                    if (oldEmpleadoOfPedidoDistribuidorListNewPedidoDistribuidor != null && !oldEmpleadoOfPedidoDistribuidorListNewPedidoDistribuidor.equals(empleado)) {
                        oldEmpleadoOfPedidoDistribuidorListNewPedidoDistribuidor.getPedidoDistribuidorList().remove(pedidoDistribuidorListNewPedidoDistribuidor);
                        oldEmpleadoOfPedidoDistribuidorListNewPedidoDistribuidor = em.merge(oldEmpleadoOfPedidoDistribuidorListNewPedidoDistribuidor);
                    }
                }
            }
            for (PedidoCliente pedidoClienteListOldPedidoCliente : pedidoClienteListOld) {
                if (!pedidoClienteListNew.contains(pedidoClienteListOldPedidoCliente)) {
                    pedidoClienteListOldPedidoCliente.setEmpleado(null);
                    pedidoClienteListOldPedidoCliente = em.merge(pedidoClienteListOldPedidoCliente);
                }
            }
            for (PedidoCliente pedidoClienteListNewPedidoCliente : pedidoClienteListNew) {
                if (!pedidoClienteListOld.contains(pedidoClienteListNewPedidoCliente)) {
                    Empleado oldEmpleadoOfPedidoClienteListNewPedidoCliente = pedidoClienteListNewPedidoCliente.getEmpleado();
                    pedidoClienteListNewPedidoCliente.setEmpleado(empleado);
                    pedidoClienteListNewPedidoCliente = em.merge(pedidoClienteListNewPedidoCliente);
                    if (oldEmpleadoOfPedidoClienteListNewPedidoCliente != null && !oldEmpleadoOfPedidoClienteListNewPedidoCliente.equals(empleado)) {
                        oldEmpleadoOfPedidoClienteListNewPedidoCliente.getPedidoClienteList().remove(pedidoClienteListNewPedidoCliente);
                        oldEmpleadoOfPedidoClienteListNewPedidoCliente = em.merge(oldEmpleadoOfPedidoClienteListNewPedidoCliente);
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
                String id = empleado.getDni();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getDni();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<PedidoDistribuidor> pedidoDistribuidorList = empleado.getPedidoDistribuidorList();
            for (PedidoDistribuidor pedidoDistribuidorListPedidoDistribuidor : pedidoDistribuidorList) {
                pedidoDistribuidorListPedidoDistribuidor.setEmpleado(null);
                pedidoDistribuidorListPedidoDistribuidor = em.merge(pedidoDistribuidorListPedidoDistribuidor);
            }
            List<PedidoCliente> pedidoClienteList = empleado.getPedidoClienteList();
            for (PedidoCliente pedidoClienteListPedidoCliente : pedidoClienteList) {
                pedidoClienteListPedidoCliente.setEmpleado(null);
                pedidoClienteListPedidoCliente = em.merge(pedidoClienteListPedidoCliente);
            }
            em.remove(empleado);
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

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
        public boolean login(String login, String password){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT e FROM Empleado e WHERE "
                + "e.login = :login AND e.password = :password");
        q.setParameter("login", login);
        q.setParameter("password", password);
        
        try{
            Empleado empleado = (Empleado) q.getSingleResult();
            if(empleado != null){
                return true;
            }else{
                return false;
            }
        }catch(NoResultException  ex){
            return false;
        }
    }
}
