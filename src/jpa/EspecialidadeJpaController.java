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
import model.Especialidade;

/**
 *
 * @author carlos
 */
public class EspecialidadeJpaController implements Serializable {

    public EspecialidadeJpaController() {
        emf = Persistence.createEntityManagerFactory("Prontuario_EletronicoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Especialidade especialidade) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(especialidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Especialidade especialidade) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            especialidade = em.merge(especialidade);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = especialidade.getId();
                if (findEspecialidade(id) == null) {
                    throw new NonexistentEntityException("The especialidade with id " + id + " no longer exists.");
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
            Especialidade especialidade;
            try {
                especialidade = em.getReference(Especialidade.class, id);
                especialidade.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The especialidade with id " + id + " no longer exists.", enfe);
            }
            em.remove(especialidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Especialidade> findEspecialidadeEntities() {
        return findEspecialidadeEntities(true, -1, -1);
    }

    public List<Especialidade> findEspecialidadeEntities(int maxResults, int firstResult) {
        return findEspecialidadeEntities(false, maxResults, firstResult);
    }

    private List<Especialidade> findEspecialidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Especialidade.class));
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

    public Especialidade findEspecialidade(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Especialidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getEspecialidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Especialidade> rt = cq.from(Especialidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
