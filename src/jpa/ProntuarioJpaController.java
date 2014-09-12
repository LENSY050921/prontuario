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
import model.Prontuario;

/**
 *
 * @author carlos
 */
public class ProntuarioJpaController implements Serializable {

    public ProntuarioJpaController() {
        emf = Persistence.createEntityManagerFactory("Prontuario_EletronicoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prontuario prontuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(prontuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prontuario prontuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            prontuario = em.merge(prontuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = prontuario.getId();
                if (findProntuario(id) == null) {
                    throw new NonexistentEntityException("The prontuario with id " + id + " no longer exists.");
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
            Prontuario prontuario;
            try {
                prontuario = em.getReference(Prontuario.class, id);
                prontuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prontuario with id " + id + " no longer exists.", enfe);
            }
            em.remove(prontuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prontuario> findProntuarioEntities() {
        return findProntuarioEntities(true, -1, -1);
    }

    public List<Prontuario> findProntuarioEntities(int maxResults, int firstResult) {
        return findProntuarioEntities(false, maxResults, firstResult);
    }

    private List<Prontuario> findProntuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prontuario.class));
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

    public Prontuario findProntuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prontuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getProntuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prontuario> rt = cq.from(Prontuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
