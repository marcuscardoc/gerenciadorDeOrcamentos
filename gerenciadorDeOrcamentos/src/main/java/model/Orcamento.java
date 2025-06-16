package main.java.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Orcamento {
	private Integer idOrcamento;
	private StringProperty nomeOrcamento = new SimpleStringProperty();
	private StringProperty nomeCliente = new SimpleStringProperty();
	private StringProperty nomeVendedor = new SimpleStringProperty();
	private StringProperty nomeArquiteto = new SimpleStringProperty();
	private StringProperty validadeDaProposta = new SimpleStringProperty();
	private StringProperty formaDePagamento = new SimpleStringProperty();
	private DoubleProperty valorTotalOrçamento = new SimpleDoubleProperty();
	private DoubleProperty desconto = new SimpleDoubleProperty();
	private DoubleProperty valorTotalComDesconto = new SimpleDoubleProperty();
	List<Produto> produtos = new ArrayList<>();

	public Orcamento() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// ID ORÇAMENTO
	public Integer getIdOrcamento() {
		return idOrcamento;
	}

	public void setIdOrcamento(Integer idOrcamento) {
		this.idOrcamento = idOrcamento;
	}

	@Column(nullable = false, name = "nomeOrcamento")
	// NOME ORÇAMENTO
	public String getNomeOrcamento() {
		return nomeOrcamento.get();
	}

	public void setNomeOrcamento(String nomeOrcamento) {
		this.nomeOrcamento.set(nomeOrcamento);
	}

	public StringProperty NomeOrcamentoProperty() {
		return nomeOrcamento;
	}

	@Column(name = "nomeCliente")
	// TELEFONE VENDEDOR
	public String getNomeCliente() {
		return nomeCliente.get();
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente.set(nomeCliente);
	}

	public StringProperty NomeClienteProperty() {
		return nomeCliente;
	}

	@Column(name = "nomeVendedor")
	// NOME VENDEDOR
	public String getNomeVendedor() {
		return nomeVendedor.get();
	}

	public void setNomeVendedor(String nomeVendedor) {
		this.nomeVendedor.set(nomeVendedor);
	}

	public StringProperty Property() {
		return nomeVendedor;
	}

	@Column(name = "nomeArquiteto")
	// NOME ARQUITETO
	public String getNomeArquiteto() {
		return nomeArquiteto.get();
	}

	public void setNomeArquiteto(String nomeArquiteto) {
		this.nomeArquiteto.set(nomeArquiteto);
	}

	public StringProperty NomeArquitetoProperty() {
		return nomeArquiteto;
	}

	@Column(name = "validadeProposta")
	// VALIDADE DA PROPOSTA
	public String getValidadeDaProposta() {
		return validadeDaProposta.get();
	}

	public void setValidadeDaProposta(String validadeDaProposta) {
		this.validadeDaProposta.set(validadeDaProposta);
	}

	public StringProperty ValidadeDaPropostaProperty() {
		return validadeDaProposta;
	}
	
	@Column(name = "formaDePagamento")
	// VALIDADE DA PROPOSTA
	public String getFormaDePagamento() {
		return formaDePagamento.get();
	}

	public void setFormaDePagamento(String formaDePagamento) {
		this.formaDePagamento.set(formaDePagamento);
	}

	public StringProperty FormaDePagamentoProperty() {
		return formaDePagamento;
	}

	@OneToMany(targetEntity = Produto.class, mappedBy = "orcamento", fetch = FetchType.EAGER)
	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	@Column(name = "valorTotal")
	// VALOR TOTAL ORÇAMENTO
	public Double getValorTotalOrçamento() {
		return valorTotalOrçamento.get();
	}

	public void setValorTotalOrçamento(Double valorTotalOrçamento) {
		this.valorTotalOrçamento.set(valorTotalOrçamento);
	}

	public DoubleProperty ValorTotalOrçamentoProperty() {
		return valorTotalOrçamento;
	}

	@Column(name = "desconto")
	// DESCONTO
	public Double getDesconto() {
		return desconto.get();
	}

	public void setDesconto(Double desconto) {
		this.desconto.set(desconto);
	}

	public DoubleProperty DescontoProperty() {
		return desconto;
	}

	@Column(name = "totalComDesconto")
	// VALOR TOTAL COM DESCONTO
	public Double getValorTotalComDesconto() {
		return valorTotalComDesconto.get();
	}

	public void setValorTotalComDesconto(Double valorTotalComDesconto) {
		this.valorTotalComDesconto.set(valorTotalComDesconto);
	}

	public DoubleProperty ValorTotalComDescontoProperty() {
		return valorTotalComDesconto;
	}

}
