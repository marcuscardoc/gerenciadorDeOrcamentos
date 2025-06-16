package main.java.DAO;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import main.java.model.Orcamento;
import main.java.model.Produto;

public class OrcamentoDAO {

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("pjtbvpisos");

	public void CriarOrcamento(Orcamento orcamento) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(orcamento);
		em.getTransaction().commit();
		em.close();
	}
	
	public void modificarOrcamento(Orcamento orcamento) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			if (orcamento.getIdOrcamento() == null) {
				em.persist(orcamento);
			} else {
				em.merge(orcamento);
			}
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

	public List<Orcamento> buscarTodos() {
		EntityManager em = emf.createEntityManager();
		try {
			return em.createQuery("FROM Orcamento", Orcamento.class).getResultList();
		} finally {
			em.close();
		}
	}

	public Orcamento buscarPorNome(String nome) {
	    EntityManager em = emf.createEntityManager();
	    Orcamento orcamento = null;

	    try {
	        String jpql = "SELECT o FROM Orcamento o WHERE o.nomeOrcamento = :nomeOrcamento";
	        TypedQuery<Orcamento> query = em.createQuery(jpql, Orcamento.class);
	        query.setParameter("nomeOrcamento", nome);

	        // Recupera o orçamento, se existir
	        List<Orcamento> resultados = query.getResultList();
	        if (!resultados.isEmpty()) {
	            orcamento = resultados.get(0);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }

	    return orcamento;
	}


	public void excluir(Integer id) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			Orcamento orcamento = em.find(Orcamento.class, id);
			if (orcamento != null) {
				em.remove(orcamento);
			}
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}
}
