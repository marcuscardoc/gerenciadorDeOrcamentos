package main.java.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import main.java.model.Produto;

public class ProdutoDAO {
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("pjtbvpisos");
	
	public List<Produto> buscarPorOrcamento(Integer orcamentoId) {
	    EntityManager em = emf.createEntityManager();
	    try {
	        return em.createQuery("SELECT p FROM Produto p WHERE p.orcamento.id = :orcamentoId", Produto.class)
	                 .setParameter("orcamentoId", orcamentoId)
	                 .getResultList();
	    } finally {
	        em.close();
	    }
	}


	public void CriarProduto(Produto produto) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(produto);
		em.getTransaction().commit();
		em.close();
	}

	public void ExcluirProduto(Produto produto) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction entityTransaction = null;

		try {
			entityTransaction = em.getTransaction();
			entityTransaction.begin();

			Produto produtoDelete = em.find(Produto.class, produto.getIdProduto());
			if (produtoDelete != null) {
				em.remove(produtoDelete);
			}

			entityTransaction.commit();
		} catch (Exception e) {
			if (entityTransaction != null && entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
			e.printStackTrace();
		}
	}

	public void ModificarProduto(Produto produto) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = null;
		try {
			transaction = em.getTransaction();
			transaction.begin();

			em.merge(produto);

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	public List<Produto> buscarPorDescricaoReferenciaAmbiente(String descricaoProduto, Integer referencia, String ambiente) {
	    EntityManager em = emf.createEntityManager();
	    List<Produto> produtos;

	    try {
	        // Inicia a query base
	        String jpql = "SELECT p FROM Produto p WHERE 1=1";

	        // Adiciona condições dinamicamente
	        if (descricaoProduto != null && !descricaoProduto.isEmpty()) {
	            jpql += " AND (LOWER(p.descricao) LIKE :descricao OR LOWER(p.ambiente) LIKE :ambiente)";
	        }
	        if (referencia != null) {
	            jpql += " AND p.referencia = :referencia";
	        }

	        // Cria a consulta
	        TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);

	        // Define os parâmetros dinamicamente
	        if (descricaoProduto != null && !descricaoProduto.isEmpty()) {
	            query.setParameter("descricao", "%" + descricaoProduto + "%");
	            query.setParameter("ambiente", "%" + ambiente + "%");
	        }
	        if (referencia != null) {
	            query.setParameter("referencia", referencia);
	        }

	        // Executa a consulta
	        produtos = query.getResultList();
	    } finally {
	        em.close();
	    }

	    return produtos;
	}


	public List<Produto> buscarProduto() {
		EntityManager em = emf.createEntityManager();
		List<Produto> produtos = null;
		try {
			produtos = em.createQuery("FROM Produto", Produto.class).getResultList();
		} finally {
			em.close();
		}
		return produtos;
	}
}
