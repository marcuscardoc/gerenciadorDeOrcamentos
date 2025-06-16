package main.java.controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import main.java.DAO.OrcamentoDAO;
import main.java.DAO.ProdutoDAO;
import main.java.model.Orcamento;
import main.java.model.Produto;

public class PageOrcamentoController {
//PANE
	@FXML
	private AnchorPane paneOrçamento;

//BUTTON	
	@FXML
	private Button adicionarProdutoBTN;

	@FXML
	private Button pesquisarBTN;

	@FXML
	private Button salvarOrcamentoBTN;

	@FXML
	private Button excluirProdutoBTN;

	@FXML
	private Button cacularBTN;
	
	@FXML
	private Button voltarBTN;

//COMBOBOX 
	@FXML
	private ComboBox<String> formaDePagamentoBOX;
	
//TABLEVIEW
	@FXML
	private TableView<Produto> tableViewOrcamento;

//TABLECOLUMN
	@FXML
	private TableColumn<Produto, String> tColumnAmbiente;

	@FXML
	private TableColumn<Produto, String> tColumnDescricao;

	@FXML
	private TableColumn<Produto, Number> tColumnItem;

	@FXML
	private TableColumn<Produto, Integer> tColumnQuantidade;

	@FXML
	private TableColumn<Produto, Integer> tColumnReferencia;

	@FXML
	private TableColumn<Produto, Double> tColumnValorTotal;

	@FXML
	private TableColumn<Produto, Double> tColumnValorUnitario;

//TEXTFIELD
	@FXML
	private TextField nomeVendedorTF;

	@FXML
	private TextField nomeArquitetoTF;

	@FXML
	private TextField nomeClienteTF;

	@FXML
	private TextField telefoneVendedorTF;

	@FXML
	private TextField validadeDaPropostaTF;

	@FXML
	private TextField valorUnitarioTF;

	@FXML
	private TextField pesquisarProdutoTF;

	@FXML
	private TextField quantidadeTF;

	@FXML
	private TextField referenciaTF;

	@FXML
	private TextField nomeDoOrcamentoTF;

	@FXML
	private TextField ambienteTF;

	@FXML
	private TextField descricaoTF;

	@FXML
	private TextField descontoTF;

	@FXML
	private TextField valorTotalDoOrcamentoTF;

	@FXML
	private TextField totalComDesconto;

//OBSERVABLELIST
	@FXML
	ObservableList<Orcamento> OrcamentoDATA = FXCollections.observableArrayList();

	@FXML
	ObservableList<Produto> ProdutoDATA = FXCollections.observableArrayList();

//ATRIBUTOS
	private PagePrincipalController pagePrincipalController;

	public void setPagePrincipalController(PagePrincipalController controller) {
		this.pagePrincipalController = controller;
	}

//ATUALIZA A LISTVIEW DA PAGE PRINCIPAL	
	private void atualizarListaPrincipal(Orcamento orcamento) {
		if (pagePrincipalController != null) {
			pagePrincipalController.adicionarOrcamentoNaLista(orcamento.getNomeOrcamento());
		}
	}

//INICIADOR	
	@FXML
	private void initialize() {
		//INICIA COMBOBOX
		formaDePagamentoBOX.getItems().add("DINHEIRO");
		formaDePagamentoBOX.getItems().add("DEBITO");
		formaDePagamentoBOX.getItems().add("CREDITO");
		formaDePagamentoBOX.getItems().add("PIX");
		
        //TORNANDO TABLEVIEW EDITAVEL E CARREGANDO OS DADOS DOS PRODUTOS
		tableViewOrcamento.setEditable(true);
		carregarDadosProdutos();

		//COLUNA ITEM
		tColumnItem.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(tableViewOrcamento.getItems().indexOf(cellData.getValue()) + 1));

		//COLUNA AMBIENTE MOSTRA ERRO AMBIENTE EM BRANCO E MODIFICA PRODUTO DIRETO NA TABLE
		tColumnAmbiente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmbiente()));
		tColumnAmbiente.setCellFactory(TextFieldTableCell.forTableColumn());
		tColumnAmbiente.setOnEditCommit(event -> {
			String newValue = event.getNewValue();

			if (newValue == null || newValue.trim().isEmpty()) {
				mostrarAlerta("ERRO!!", "AMBIENTE EM BRANCO", Alert.AlertType.ERROR);
				event.consume();
			} else {
				ProdutoDAO produtoDAO = new ProdutoDAO();
				Produto produto = event.getRowValue();
				produto.setAmbiente(newValue);
				produtoDAO.ModificarProduto(produto);
				tableViewOrcamento.refresh();
			}
		});
		
		//COLUNA QUANTIDADE SALVA MODIFICAÇOES DIRETO NA TABLE
		tColumnQuantidade.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantidade()).asObject());
		tColumnQuantidade.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		tColumnQuantidade.setOnEditCommit(event -> {
			Integer newValue = event.getNewValue();
			ProdutoDAO produtoDAO = new ProdutoDAO();
			Produto produto = event.getRowValue();
			produto.setQuantidade(newValue);
			produtoDAO.ModificarProduto(produto);
			tableViewOrcamento.refresh();
			atualizarValorTotal();
			totalComDesconto.clear();

		});

		//COLUNA DESCRIÇAO MOSTRA ERRO SE FICAR EM BRANCO E SALVA DIRETO NA TABLE
		tColumnDescricao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
		tColumnDescricao.setCellFactory(TextFieldTableCell.forTableColumn());
		tColumnDescricao.setOnEditCommit(event -> {
			String newValue = event.getNewValue();

			if (newValue == null || newValue.trim().isEmpty()) {
				mostrarAlerta("ERRO!!", "DESCRIÇAO EM BRANCO", Alert.AlertType.ERROR);
				event.consume();
			} else {
				ProdutoDAO produtoDAO = new ProdutoDAO();
				Produto produto = event.getRowValue();
				produto.setDescricao(newValue);
				produtoDAO.ModificarProduto(produto);
				tableViewOrcamento.refresh();
			}
		});
			
		tColumnReferencia.setCellValueFactory(
			    cellData -> new SimpleIntegerProperty(cellData.getValue().getReferencia()).asObject()
			);

			tColumnReferencia.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

			tColumnReferencia.setOnEditCommit(event -> {
			    String newValueStr = event.getNewValue().toString(); // Remove espaços em branco do início e do fim

			    // Validação: campo não pode estar vazio
			    if (newValueStr.isEmpty()) {
			        mostrarAlerta("ERRO!!", "REFERÊNCIA EM BRANCO", Alert.AlertType.ERROR);
			        event.getTableView().refresh(); // Reverte a edição
			        return;
			    }

			    // Validação: campo deve conter apenas números
			    try {
			        Integer.parseInt(newValueStr); // Tenta converter para Integer
			    } catch (NumberFormatException e) {
			        mostrarAlerta("ERRO!!", "REFERÊNCIA DEVE CONTER APENAS NÚMEROS", Alert.AlertType.ERROR);
			        event.getTableView().refresh(); // Reverte a edição
			        return;
			    }

			    try {
			        // Converte o valor para Integer e atualiza o produto
			        Integer novoValor = Integer.parseInt(newValueStr);
			        Produto produto = event.getRowValue();
			        produto.setReferencia(novoValor);

			        ProdutoDAO produtoDAO = new ProdutoDAO();
			        produtoDAO.ModificarProduto(produto);

			    } catch (Exception e) {
			        mostrarAlerta("ERRO!!", "ERRO INESPERADO", Alert.AlertType.ERROR);
			        event.getTableView().refresh(); // Reverte a edição
			    }
			});


		// TRANSFORMAR EM EDITAVEL
		tColumnValorUnitario.setCellValueFactory(
				cellData -> new SimpleDoubleProperty(cellData.getValue().getValorUnitario()).asObject());
		tColumnValorUnitario.setCellFactory(coluna -> {
			Locale locale = Locale.of("pt", "BR");
			Locale Brasil = locale;
			NumberFormat formatoBrasileiro = NumberFormat.getCurrencyInstance(Brasil);

			// Configura a célula para exibir valores formatados e permitir edição
			TextFieldTableCell<Produto, Double> cell = new TextFieldTableCell<>(new DoubleStringConverter() {
				@Override
				public String toString(Double valor) {
					// Verifica se o valor é diferente de null
					if (valor != null) {
						return formatoBrasileiro.format(valor); // Formata o valor
					} else {
						return ""; // Retorna uma string vazia se o valor for null
					}
				}

				@Override
				public Double fromString(String valor) {
					try {
						// Remove caracteres não numéricos, substitui vírgula por ponto e converte para
						// Double
						if (valor != null && !valor.isEmpty()) {
							String valorNumerico = valor.replaceAll("[^\\d.,-]", "").replace(",", ".");
							return Double.parseDouble(valorNumerico);
						} else {
							return null; // Retorna null se a string estiver vazia
						}
					} catch (NumberFormatException e) {
						return null; // Retorna null se ocorrer uma exceção
					}
				}
			});

			return cell;
		});

		// Define o comportamento de edição com verificação de entrada inválida
		tColumnValorUnitario.setOnEditCommit(event -> {
			Double novoValor = event.getNewValue();

			// Verifica se o novo valor é nulo ou menor que zero
			if (novoValor == null || novoValor < 0) {
				mostrarAlerta("ERRO!!", "ENTRADA DE VALOR UNITARIO INVALIDA, INSIRA UM VALOR VALIDO",
						Alert.AlertType.ERROR);
				event.consume();
				tableViewOrcamento.refresh();
			} else {
				// Atualiza o valor unitário do produto
				ProdutoDAO produtoDAO = new ProdutoDAO();
				Produto produto = event.getRowValue();
				produto.setValorUnitario(novoValor);
				produtoDAO.ModificarProduto(produto);
				atualizarValorTotal(); // Atualiza o valor total após a edição
				totalComDesconto.clear();
				tableViewOrcamento.refresh();
			}
		});

		tColumnValorTotal.setCellValueFactory(
				cellData -> new SimpleDoubleProperty(cellData.getValue().getValorTotal()).asObject());
		tColumnValorTotal.setCellFactory(coluna -> new TableCell<>() {
			Locale Brasil = Locale.of("pt", "BR");
			private final NumberFormat formatoBrasileiro = NumberFormat.getCurrencyInstance(Brasil);

			@Override
			protected void updateItem(Double referencia, boolean empty) {
				super.updateItem(referencia, empty);
				if (empty || referencia == null) {
					setText(null); // Não exibe nada para células vazias
				} else {
					setText(formatoBrasileiro.format(referencia)); // Exibe o número formatado
				}
			}
		});

		tableViewOrcamento.setItems(ProdutoDATA);
		totalComDesconto.setEditable(false);
		valorTotalDoOrcamentoTF.setEditable(false);
		Locale Brasil = Locale.of("pt", "BR");
		NumberFormat ptBr = NumberFormat.getCurrencyInstance(Brasil);
		valorTotalDoOrcamentoTF.setText(String.valueOf(ptBr.format(valorTotDoOrcamento())));

	}

//CALCULAR VALOR TOTAL DO ORÇAMENTO
	private Double valorTotDoOrcamento() {
		double valorTotal = 0.0;

		for (Produto produto : tableViewOrcamento.getItems()) {
			valorTotal += produto.getValorTotal();
		}

		Double valorFormatado = Math.round(valorTotal * 100.0) / 100.0;
		return valorFormatado;
	}

	private void atualizarValorTotal() {
		Locale of = Locale.of("pt", "BR");
		Locale brasil = of;
		NumberFormat formatoBrasileiro = NumberFormat.getCurrencyInstance(brasil);
		double valorTotal = ProdutoDATA.stream()
				.mapToDouble(produto -> produto.getValorUnitario() * produto.getQuantidade()).sum();
		String valorTotFormatado = formatoBrasileiro.format(valorTotal);
		valorTotalDoOrcamentoTF.setText(valorTotFormatado);
		totalComDesconto.clear();
	}

//CALCULAR VALOR TOTAL DO ORÇAMENTO COM DESCONTO
	@FXML
	private void calcularDesconto() {
		Orcamento orcamento = new Orcamento();
		String Desconto = descontoTF.getText();

		Double desconto;
		if (Desconto.isEmpty()) {
			desconto = 0.0;
		} else {
			desconto = Double.parseDouble(Desconto);
		}

		orcamento.setDesconto(desconto);

		Double valorTotal = valorTotDoOrcamento();
		Double valorTot = valorTotal * (orcamento.getDesconto() / 100);
		Double valorTott = valorTotal - valorTot;
		Double valorFormatado = Math.round(valorTott * 100.0) / 100.0;
		Locale Brasil = Locale.of("pt", "BR");
		NumberFormat ptBr = NumberFormat.getCurrencyInstance(Brasil);
		totalComDesconto.setText(String.valueOf(ptBr.format(valorFormatado)));
	}

//GERENCIADOR DE ALERTS
	private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
		Alert alerta = new Alert(tipo);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
		alerta.setContentText(mensagem);
		alerta.showAndWait();
	}

//CARREGAR DADOS DO ORÇAMENTO 
	@FXML
	private void carregarDadosProdutos() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		List<Produto> produto = produtoDAO.buscarProduto();

		ProdutoDATA.setAll(produto);
	}

//METODO PARA POPULAR COMBOBOX	

//ADICIONAR PRODUTO NA TABLE 
	@FXML
	private void addProduto() {
	    pagePrincipalController.inicializarOrcamento(); // Certifica-se de que o orçamento foi criado no banco

	    String quantidadee = quantidadeTF.getText();
	    String referenciaa = referenciaTF.getText();
	    String valorUnitarioo = valorUnitarioTF.getText();
	    String ambiente = ambienteTF.getText();
	    String descricao = descricaoTF.getText();

	    if (!quantidadee.isEmpty() && !referenciaa.isEmpty() && !valorUnitarioo.isEmpty() &&
	        !ambiente.isEmpty() && !descricao.isEmpty()) {

	        try {
	            int quantidade = Integer.parseInt(quantidadee);
	            int referencia = Integer.parseInt(referenciaa);
	            Double valorUnitario = Double.parseDouble(valorUnitarioo);

	            Produto produto = new Produto();
	            produto.setQuantidade(quantidade);
	            produto.setReferencia(referencia);
	            produto.setValorUnitario(valorUnitario);
	            produto.setAmbiente(ambiente);
	            produto.setDescricao(descricao);

	            // Associar ao orçamento atual
	            produto.setOrcamento(pagePrincipalController.orcamentoAtual);
	            
	            ProdutoDAO produtoDAO = new ProdutoDAO();
	            produtoDAO.CriarProduto(produto); // Salva o produto no banco
	            ProdutoDATA.add(produto);
	            tableViewOrcamento.setItems(ProdutoDATA);
	            tableViewOrcamento.refresh();
	            atualizarValorTotal();

	            // Limpar campos
	            quantidadeTF.clear();
	            referenciaTF.clear();
	            valorUnitarioTF.clear();
	            ambienteTF.clear();
	            descricaoTF.clear();
	            totalComDesconto.clear();
	        } catch (NumberFormatException e) {
	            mostrarAlerta("Erro de entrada","Certifique-se de que os campos numéricos estão corretos.",Alert.AlertType.ERROR);
	        }
	    } else {
	        mostrarAlerta("Campos obrigatórios","Por favor, preencha todos os campos para adicionar o produto.",Alert.AlertType.WARNING);
	    }
	}

	
	public void carregarProdutos(List<Produto> produtos) {
	    ProdutoDATA.clear();
	    ProdutoDATA.addAll(produtos);
	    tableViewOrcamento.setItems(ProdutoDATA);
	    tableViewOrcamento.refresh();
	    atualizarValorTotal();
	}



	public double parseValor(String valor) {
		// Remove "R$", espaços e pontos
		String valorLimpo = valor.replace("R$", "").replace(" ", "").replace("  ", "")
				.replace("\u00A0", "");
		// Substitui vírgula por ponto
		valorLimpo = valorLimpo.replace(",", ".");
		// Converte para double
		return Double.parseDouble(valorLimpo.trim());
	}

	@FXML
	private void addOrcamento() {
	    if (pagePrincipalController.obterOrcamentoAtual() == null) {
	        mostrarAlerta("Erro", "Nenhum orçamento selecionado para editar.",Alert.AlertType.ERROR);
	        return;
	    }
	    
	    String descontoo = descontoTF.getText();
	    Double desconto = Double.parseDouble(descontoo);
	    
	   // String ValorTott = valorTotalDoOrcamentoTF.getText();
	    double valorTotal = 0.0;

		for (Produto produto : tableViewOrcamento.getItems()) {
			valorTotal += produto.getValorTotal();
		}

		Double valorTotalFormatado = Math.round(valorTotal * 100.0) / 100.0;
	    
	    
	    String valorTotalOrcamentoo = valorTotalDoOrcamentoTF.getText();
	    Double valorTotalOrcamento = parseValor(valorTotalOrcamentoo);
		Double valorTotalComDesconto = valorTotalOrcamento * (pagePrincipalController.orcamentoAtual.getDesconto() / 100);
		Double valorTotalSubtracao = valorTotalOrcamento - valorTotalComDesconto;
		Double valorTotalComDescontoFormatado = Math.round(valorTotalSubtracao * 100.0) / 100.0;
	    Double totComDesconto = valorTotalComDescontoFormatado;
	    
	 

	    // Atualizando os campos do orçamento atual
	    pagePrincipalController.orcamentoAtual.setNomeVendedor(nomeVendedorTF.getText());
	    pagePrincipalController.orcamentoAtual.setNomeArquiteto(nomeArquitetoTF.getText());
	    pagePrincipalController.orcamentoAtual.setNomeCliente(nomeClienteTF.getText());
	    pagePrincipalController.orcamentoAtual.setFormaDePagamento(formaDePagamentoBOX.getValue());
	    pagePrincipalController.orcamentoAtual.setValidadeDaProposta(validadeDaPropostaTF.getText());
	    pagePrincipalController.orcamentoAtual.setNomeOrcamento(nomeDoOrcamentoTF.getText());
	    pagePrincipalController.orcamentoAtual.setValorTotalOrçamento(valorTotalFormatado);
	    pagePrincipalController.orcamentoAtual.setDesconto(desconto);
	    pagePrincipalController.orcamentoAtual.setValorTotalComDesconto(totComDesconto);

	    // Atualizando no banco de dados
	    OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
	    orcamentoDAO.modificarOrcamento(pagePrincipalController.orcamentoAtual); // Atualizar o orçamento no banco de dados

	    // Atualizar a lista principal para refletir as mudanças
	    atualizarListaPrincipal(pagePrincipalController.orcamentoAtual);

	    mostrarAlerta("Sucesso", "Orçamento atualizado com sucesso!",Alert.AlertType.INFORMATION);
	}


//EXCLUIR PRODUTO DA TABLE
	@FXML
	private void excluirProduto() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produtoSelecionado = tableViewOrcamento.getSelectionModel().getSelectedItem();

		if (produtoSelecionado != null) {
			try {
				produtoDAO.ExcluirProduto(produtoSelecionado);
				;
				ProdutoDATA.remove(produtoSelecionado);
				atualizarValorTotal();
			} catch (Exception e) {
				mostrarAlerta("Erro", "Falha ao excluir o soldado", Alert.AlertType.ERROR);
				e.printStackTrace();
			}
		} else {
			mostrarAlerta("Aviso", "Nenhum soldado selecionado!", Alert.AlertType.WARNING);
		}
	}

	public void iniciarNovoOrcamento() {
		// Cria uma lista vazia para os produtos
		ObservableList<Produto> produtosVazios = FXCollections.observableArrayList();

		// Limpa a tabela e associa a lista vazia
		tableViewOrcamento.setItems(produtosVazios);

		// Limpa os campos do orçamento (opcional, para garantir que estejam zerados)
		nomeVendedorTF.clear();
		nomeArquitetoTF.clear();
		nomeClienteTF.clear();
		nomeDoOrcamentoTF.clear();
		valorTotalDoOrcamentoTF.clear();
		descontoTF.clear();
		totalComDesconto.clear();
	}

	public TableView<Produto> getTableView() {
		return tableViewOrcamento;
	}


	public void carregarOrcamento(Orcamento orcamentoSelecionado) {
	    if (orcamentoSelecionado == null) {
	        mostrarAlerta("Erro", "Nenhum orçamento selecionado.",Alert.AlertType.ERROR);
	        return;
	    }

	    pagePrincipalController.orcamentoAtual = orcamentoSelecionado;

	    // Preenchendo os campos da interface
	    nomeVendedorTF.setText(pagePrincipalController.orcamentoAtual.getNomeVendedor());
	    nomeArquitetoTF.setText(pagePrincipalController.orcamentoAtual.getNomeArquiteto());
	    nomeClienteTF.setText(pagePrincipalController.orcamentoAtual.getNomeCliente());
	    formaDePagamentoBOX.setValue(pagePrincipalController.orcamentoAtual.getFormaDePagamento());
	    validadeDaPropostaTF.setText(pagePrincipalController.orcamentoAtual.getValidadeDaProposta());
	    nomeDoOrcamentoTF.setText(pagePrincipalController.orcamentoAtual.getNomeOrcamento());
	    valorTotalDoOrcamentoTF.setText(String.valueOf(pagePrincipalController.orcamentoAtual.getValorTotalOrçamento()));
	    descontoTF.setText(String.valueOf(pagePrincipalController.orcamentoAtual.getDesconto()));
	  
	    
	    String valorTotalOrcamentoo = valorTotalDoOrcamentoTF.getText();
	    Double valorTotalOrcamento = parseValor(valorTotalOrcamentoo);
		Double valorTot = valorTotalOrcamento * (pagePrincipalController.orcamentoAtual.getDesconto() / 100);
		Double valorTott = valorTotalOrcamento - valorTot;
		Double valorFormatado = Math.round(valorTott * 100.0) / 100.0;
		Locale Brasil = Locale.of("pt", "BR");
		NumberFormat ptBr = NumberFormat.getCurrencyInstance(Brasil);
		totalComDesconto.setText(String.valueOf(ptBr.format(valorFormatado)));
	}
	
	@FXML
	public void voltar(ActionEvent event) {
	    try {
	        // Carrega o FXML da página anterior
	        Parent paginaAnterior = FXMLLoader.load(getClass().getResource("/main/java/view/pagePrincipal.fxml"));
	        
	        // Cria uma nova cena para a página anterior
	        Scene scene = new Scene(paginaAnterior);

	        // Obtém a janela atual (a página que será fechada)
	        Stage stageAtual = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        
	        // Fecha a janela atual
	        stageAtual.close();
	        
	        // Cria e exibe a nova janela
	        Stage stageAnterior = new Stage();
	        stageAnterior.setScene(scene);
	        stageAnterior.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	        mostrarAlerta("Erro!","Erro ao voltar para a página anterior.",AlertType.ERROR);
	    }
	}




//BUSCAR POR NOME E CODIGO
	@FXML
	private void buscarPorNomeCodigo() {
		ProdutoDAO produtoDAO = new ProdutoDAO();

		// Obtém o texto de entrada do campo de pesquisa
		String pesquisaTexto = pesquisarProdutoTF.getText().trim();
		String pesquisaDescricao = null;
		String pesquisaAmbiente = null;
		Integer pesquisaReferencia = null;

		if (!pesquisaTexto.isEmpty()) {
			try {
				// Se o texto for numérico, trata como referência
				pesquisaReferencia = Integer.parseInt(pesquisaTexto);
			} catch (NumberFormatException e) {
				// Se não for numérico, trata como texto para descrição e ambiente
				pesquisaDescricao = pesquisaTexto.toLowerCase();
				pesquisaAmbiente = pesquisaTexto.toLowerCase();
			}
		}

		// Consulta no DAO com os três parâmetros
		List<Produto> produtos = produtoDAO.buscarPorDescricaoReferenciaAmbiente(pesquisaDescricao, pesquisaReferencia,
				pesquisaAmbiente);

		// Atualiza a tabela com os resultados ou exibe alerta
		if (produtos != null && !produtos.isEmpty()) {
			ObservableList<Produto> produtosFiltrados = FXCollections.observableArrayList(produtos);
			tableViewOrcamento.setItems(produtosFiltrados);
		} else {
			carregarDadosProdutos(); // Recarrega os dados originais
			mostrarAlerta("Aviso", "Nenhum produto encontrado.", Alert.AlertType.INFORMATION);
		}
	}

//BAIXAR PDF 

	// APRENDER A MANIPULAR COMBOBOX

}
