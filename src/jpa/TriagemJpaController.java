/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.exceptions.NonexistentEntityException;
import model.Triagem;

/**
 *
 * @author carlos
 */
public class TriagemJpaController implements Serializable {

    public TriagemJpaController() {
        emf = Persistence.createEntityManagerFactory("Prontuario_EletronicoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Triagem triagem) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(triagem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Triagem triagem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            triagem = em.merge(triagem);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = triagem.getId();
                if (findTriagem(id) == null) {
                    throw new NonexistentEntityException("The triagem with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Triagem triagem;
            try {
                triagem = em.getReference(Triagem.class, id);
                triagem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The triagem with id " + id + " no longer exists.", enfe);
            }
            em.remove(triagem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Triagem> findTriagemEntities() {
        return findTriagemEntities(true, -1, -1);
    }

    public List<Triagem> findTriagemEntities(int maxResults, int firstResult) {
        return findTriagemEntities(false, maxResults, firstResult);
    }

    private List<Triagem> findTriagemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Triagem.class));
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

    public Triagem findTriagem(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Triagem.class, id);
        } finally {
            em.close();
        }
    }

    public int getTriagemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Triagem> rt = cq.from(Triagem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
