package main.java.controller;

	import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

	public class LoginController {
//PANE
		@FXML
		private AnchorPane paneLogin;
		
//BUTTON
	    @FXML
	    private Button btnEntrar;

//PASSWORD E TEXTFIELD
	    @FXML
	    private PasswordField tfPassword;

	    @FXML
	    private TextField tfUsuario;

	    
	    
	    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
            Alert alerta = new Alert(tipo);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(mensagem);
            alerta.showAndWait();
        }	

	    @FXML
	    private void Login(){
	    	
	    	try {
	        String usuarioo = tfUsuario.getText();
	        String senhaa = tfPassword.getText();

	        if (usuarioo.equals("admin") && senhaa.equals("admin")) {
	        	Stage stage = (Stage) btnEntrar.getScene().getWindow();
	        	Parent root = FXMLLoader.load(getClass().getResource("/main/java/view/pageprincipal.fxml"));
	        	stage.setScene(new Scene(root));
	        	stage.show();
	        } else {
	           mostrarAlerta("Erro", "Usuario ou senha incorreto", Alert.AlertType.ERROR);
	        }
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		mostrarAlerta("Erro", "Erro inesperado", Alert.AlertType.ERROR);	    		
	    	}	        	        
	    }
	}



