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
import com.gomez.bd.modelo.PedidoCliente;
import com.gomez.bd.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;

/**
 * UsuarioJpaController.java
 * @author Alejandro Silva <alexsilva792@gmail.com>
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf){
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (usuario.getPedidoClienteList() == null) {
            usuario.setPedidoClienteList(new ArrayList<PedidoCliente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PedidoCliente> attachedPedidoClienteList = new ArrayList<PedidoCliente>();
            for (PedidoCliente pedidoClienteListPedidoClienteToAttach : usuario.getPedidoClienteList()) {
                pedidoClienteListPedidoClienteToAttach = em.getReference(pedidoClienteListPedidoClienteToAttach.getClass(), pedidoClienteListPedidoClienteToAttach.getIdPedido());
                attachedPedidoClienteList.add(pedidoClienteListPedidoClienteToAttach);
            }
            usuario.setPedidoClienteList(attachedPedidoClienteList);
            em.persist(usuario);
            for (PedidoCliente pedidoClienteListPedidoCliente : usuario.getPedidoClienteList()) {
                Usuario oldClienteOfPedidoClienteListPedidoCliente = pedidoClienteListPedidoCliente.getCliente();
                pedidoClienteListPedidoCliente.setCliente(usuario);
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
            if (findUsuario(usuario.getDni()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getDni());
            List<PedidoCliente> pedidoClienteListOld = persistentUsuario.getPedidoClienteList();
            List<PedidoCliente> pedidoClienteListNew = usuario.getPedidoClienteList();
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
            usuario.setPedidoClienteList(pedidoClienteListNew);
            usuario = em.merge(usuario);
            for (PedidoCliente pedidoClienteListNewPedidoCliente : pedidoClienteListNew) {
                if (!pedidoClienteListOld.contains(pedidoClienteListNewPedidoCliente)) {
                    Usuario oldClienteOfPedidoClienteListNewPedidoCliente = pedidoClienteListNewPedidoCliente.getCliente();
                    pedidoClienteListNewPedidoCliente.setCliente(usuario);
                    pedidoClienteListNewPedidoCliente = em.merge(pedidoClienteListNewPedidoCliente);
                    if (oldClienteOfPedidoClienteListNewPedidoCliente != null && !oldClienteOfPedidoClienteListNewPedidoCliente.equals(usuario)) {
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
                String id = usuario.getDni();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getDni();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PedidoCliente> pedidoClienteListOrphanCheck = usuario.getPedidoClienteList();
            for (PedidoCliente pedidoClienteListOrphanCheckPedidoCliente : pedidoClienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the PedidoCliente " + pedidoClienteListOrphanCheckPedidoCliente + " in its pedidoClienteList field has a non-nullable cliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public boolean login(String login, String password){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT u FROM Usuario u WHERE "
                + "u.login = :login AND u.password = :password");
        q.setParameter("login", login);
        q.setParameter("password", password);
        
        try{
            Usuario usuario = (Usuario) q.getSingleResult();
            if(usuario != null){
                return true;
            }else{
                return false;
            }
        }catch(NoResultException  ex){
            return false;
        }
    }
}
