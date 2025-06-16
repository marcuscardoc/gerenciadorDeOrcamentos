package main.java.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.DAO.OrcamentoDAO;
import main.java.DAO.ProdutoDAO;
import main.java.model.Orcamento;
import main.java.model.Produto;

public class PagePrincipalController {
//PANE
	@FXML
	private AnchorPane panePagePrincipal;

//BUTTON
	@FXML
	private Button abrirOrcamentoBTN;

	@FXML
	private Button baixarOrcamentoBTN;

	@FXML
	private Button criarOrcamentoBTN;

	@FXML
	private Button excluirOrcamentoBTN;

//LISTVIEW
	@FXML
	private ListView<String> listaCurriculos;

	private ObservableList<String> nomesOrcamentos = FXCollections.observableArrayList();
	private OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
	private ProdutoDAO produtoDAO = new ProdutoDAO();
	public Orcamento orcamentoAtual;

    public Orcamento obterOrcamentoAtual() {
        return orcamentoAtual;
    }

    public void setOrcamentoAtual(Orcamento orcamento) {
        this.orcamentoAtual = orcamento;
    }

    @FXML
    public void inicializarOrcamento() {
        if (orcamentoAtual == null) { // Só cria um novo se não houver um orçamento atual
            orcamentoAtual = new Orcamento();
            orcamentoAtual.setNomeOrcamento("ORÇAMENTO SEM NOME");
            orcamentoAtual.setValidadeDaProposta("10 DIAS");
            OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
            orcamentoDAO.CriarOrcamento(orcamentoAtual); // Persistência inicial no banco
        }
    }

	
	@FXML
	public void initialize() {
		carregarNomesOrcamentos();
	}
	
	public void adicionarOrcamentoNaLista(String nomeOrcamento) {
	    nomesOrcamentos.add(nomeOrcamento); // Adiciona à lista observável
	}

	@FXML
	private void CriarOrcamento() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java/view/curriculo.fxml"));
	        Parent root = loader.load();

	        // Obtendo o controller da página de orçamento
	        PageOrcamentoController controller = loader.getController();
	        controller.setPagePrincipalController(this);

	        // Configurando um novo orçamento vazio
	        controller.iniciarNovoOrcamento(); // Método para configurar um novo orçamento vazio

	        // Abrindo a página
	        Stage stage = new Stage();
	        stage.setTitle("Criar Orçamento");
	        stage.setScene(new Scene(root));
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.showAndWait();

	        // Atualizar a lista de orçamentos após o fechamento
	        carregarNomesOrcamentos();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	private void carregarNomesOrcamentos() {
		nomesOrcamentos.clear();
		List<Orcamento> orcamentos = orcamentoDAO.buscarTodos();
		for (Orcamento orcamento : orcamentos) {
			nomesOrcamentos.add(orcamento.getNomeOrcamento());
		}
		listaCurriculos.setItems(nomesOrcamentos);
	}

	
	@FXML
	private void abrirOrcamento(ActionEvent event) {
	    String nomeSelecionado = listaCurriculos.getSelectionModel().getSelectedItem();

	    if (nomeSelecionado != null) {
	        Orcamento orcamento = orcamentoDAO.buscarPorNome(nomeSelecionado);

	        if (orcamento != null) {
	            try {
	                // Carrega o arquivo FXML da página de orçamento
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java/view/curriculo.fxml"));
	                Parent root = loader.load();

	                // Obtém o controlador da página de orçamento
	                PageOrcamentoController controller = loader.getController();
	                controller.setPagePrincipalController(this);

	                // Carrega os dados do orçamento selecionado
	                controller.carregarOrcamento(orcamento);

	                // Carrega os produtos associados ao orçamento
	                List<Produto> produtos = produtoDAO.buscarPorOrcamento(orcamento.getIdOrcamento());
	                controller.carregarProdutos(produtos);

	                // Abre a tela de modificação do orçamento
	                Stage stage = new Stage();
	                stage.setTitle("Editar Orçamento: " + nomeSelecionado);
	                stage.setScene(new Scene(root));
	                stage.initModality(Modality.APPLICATION_MODAL);
	                stage.showAndWait();

	    	        // Obtém a janela atual (a página que será fechada)
	    	        Stage stageAtual = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	        
	    	        // Fecha a janela atual
	    	        stageAtual.close();
	    	        
	    	        
	    	        
	                // Atualiza a lista de orçamentos após o fechamento
	                carregarNomesOrcamentos();
	            } catch (Exception e) {
	                e.printStackTrace();
	                exibirAlerta(AlertType.ERROR, "Erro", "Erro ao abrir o orçamento.");
	            }
	        } else {
	            exibirAlerta(AlertType.WARNING, "Orçamento não encontrado", 
	                    "Não foi possível encontrar o orçamento: " + nomeSelecionado);
	        }
	    } else {
	        exibirAlerta(AlertType.WARNING, "Nenhum orçamento selecionado", 
	                "Por favor, selecione um orçamento para abrir.");
	    }
	}
	
	@FXML
	private void excluirOrcamento() {
		String nomeSelecionado = listaCurriculos.getSelectionModel().getSelectedItem();
		if (nomeSelecionado != null) {
			Orcamento orcamento = orcamentoDAO.buscarPorNome(nomeSelecionado);
			if (orcamento != null) {
				orcamentoDAO.excluir(orcamento.getIdOrcamento());
				carregarNomesOrcamentos();
				exibirAlerta(AlertType.INFORMATION, "Exclusão", "Orçamento excluído com sucesso.");
			}
		} else {
			exibirAlerta(AlertType.WARNING, "Nenhum orçamento selecionado",
					"Por favor, selecione um orçamento para excluir.");
		}
	}
	
	@FXML 
	private void addOrcamento() {
		
	}

	@FXML
	private void baixarPDF() {
		String nomeSelecionado = listaCurriculos.getSelectionModel().getSelectedItem();

		if (nomeSelecionado != null) {
			Orcamento orcamento = orcamentoDAO.buscarPorNome(nomeSelecionado);

			if (orcamento != null) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Salvar Orçamento como PDF");
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo PDF", "*.pdf"));

				File arquivo = fileChooser.showSaveDialog(criarOrcamentoBTN.getScene().getWindow());

				if (arquivo != null) {
					try {
						gerarPDF(arquivo.getAbsolutePath());
						exibirAlerta(AlertType.INFORMATION, "PDF Gerado", "O arquivo foi salvo com sucesso!");
					} catch (Exception e) {
						exibirAlerta(AlertType.ERROR, "Erro ao gerar PDF",
								"Não foi possível salvar o arquivo.\nErro: " + e.getMessage());
					}
				}
			}
		}
	}

	private void gerarPDF(String caminhoArquivo) throws DocumentException, IOException {
		// Inicializa o documento
		Document document = new Document();

		try {
			// Cria o PdfWriter para escrever no arquivo especificado
			PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
			document.open();

			// Adicionando título
			Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Paragraph titulo = new Paragraph("Orçamento da Loja de Iluminação", tituloFont);
			titulo.setAlignment(Element.ALIGN_CENTER);
			document.add(titulo);

			// Espaçamento
			document.add(new Paragraph(" "));

			// Adicionando detalhes do orçamento
			Font detalheFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
			document.add(new Paragraph("Nome do Cliente: João Silva", detalheFont));
			document.add(new Paragraph("Data: " + LocalDate.now(), detalheFont));

			document.add(new Paragraph(" "));
			document.add(new Paragraph("Itens do Orçamento:", detalheFont));

			// Adicionando uma tabela com itens
			PdfPTable tabela = new PdfPTable(3); // 3 colunas: Produto, Quantidade, Preço
			tabela.setWidthPercentage(100);
			tabela.addCell(new PdfPCell(new Phrase("Produto", detalheFont)));
			tabela.addCell(new PdfPCell(new Phrase("Quantidade", detalheFont)));
			tabela.addCell(new PdfPCell(new Phrase("Preço (R$)", detalheFont)));

			// Exemplo de preenchimento da tabela
			tabela.addCell("Lâmpada LED");
			tabela.addCell("10");
			tabela.addCell("150.00");

			tabela.addCell("Fita LED");
			tabela.addCell("5");
			tabela.addCell("300.00");

			tabela.addCell("Plafon");
			tabela.addCell("3");
			tabela.addCell("450.00");

			document.add(tabela);

			// Adicionando o total
			document.add(new Paragraph(" "));
			Paragraph total = new Paragraph("Valor Total: R$ 900.00", detalheFont);
			total.setAlignment(Element.ALIGN_RIGHT);
			document.add(total);

		} finally {
			// Certifica-se de fechar o documento
			document.close();
		}
	}

	private void exibirAlerta(AlertType tipo, String titulo, String mensagem) {
		Alert alerta = new Alert(tipo);
		alerta.setTitle(titulo);
		alerta.setContentText(mensagem);
		alerta.showAndWait();
	}
}
