package main.java.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Produto {
	private Integer idProduto;
	private IntegerProperty item = new SimpleIntegerProperty();
	private StringProperty descricao = new SimpleStringProperty();
	private IntegerProperty referencia = new SimpleIntegerProperty();
	private DoubleProperty valorUnitario = new SimpleDoubleProperty();
	private IntegerProperty quantidade = new SimpleIntegerProperty();
	private DoubleProperty valorTotal = new SimpleDoubleProperty();
	private StringProperty ambiente = new SimpleStringProperty();
	private Orcamento orcamento;

	public Produto() {

	}

	// ID PRODUTO

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idProduto")
	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orcamento_id", referencedColumnName = "idOrcamento")
	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	@Column(name = "item")
	// ITEM
	public Integer getItem() {
		return item.get();
	}

	public void setItem(Integer item) {
		this.item.set(item);
	}

	public IntegerProperty ItemProperty() {
		return item;
	}

	@Column(name = "quantidade")
	// ITEM
	public Integer getQuantidade() {
		return quantidade.get();
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade.set(quantidade);
	}

	public IntegerProperty QuantidadeProperty() {
		return quantidade;
	}

	@Column(name = "descricao")
	// DESCRIÇAO
	public String getDescricao() {
		return descricao.get();
	}

	public void setDescricao(String descricao) {
		this.descricao.set(descricao);
	}

	public StringProperty DescricaoProperty() {
		return descricao;
	}

	@Column(name = "referencia")
	// REFERENCIA
	public Integer getReferencia() {
		return referencia.get();
	}

	public void setReferencia(Integer referencia) {
		this.referencia.set(referencia);
	}

	public IntegerProperty ReferenciaProperty() {
		return referencia;
	}

	@Column(name = "valorUnitario")
	// VALOR UNITARIO
	public Double getValorUnitario() {
		return valorUnitario.get();
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario.set(valorUnitario);
	}

	public DoubleProperty ValorUnitarioProperty() {
		return valorUnitario;
	}

	@Column(name = "valorTotal")
	// VALOR TOTAL
	public Double getValorTotal() {
		return getValorUnitario() * getQuantidade();
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal.set(valorTotal);
	}

	public DoubleProperty ValorTotalProperty() {
		return valorTotal;
	}

	@Column(name = "ambiente")
	// AMBIENTE
	public String getAmbiente() {
		return ambiente.get();
	}

	public void setAmbiente(String ambiente) {
		this.ambiente.set(ambiente);
	}

	public StringProperty AmbienteProperty() {
		return ambiente;
	}

}
