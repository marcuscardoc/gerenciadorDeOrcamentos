package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class InformacoesController {
	
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
	ComboBox<String> formaDePagamentoBOX;

	@FXML
	private void initialize() {
		formaDePagamentoBOX.getItems().add("DINHEIRO");
		formaDePagamentoBOX.getItems().add("DEBITO");
		formaDePagamentoBOX.getItems().add("CREDITO");
		formaDePagamentoBOX.getItems().add("PIX");
	}
}
