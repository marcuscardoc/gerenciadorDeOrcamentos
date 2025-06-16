package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Pessoa {
    private StringProperty nome;
    private StringProperty telefone;
    
	public Pessoa(String nome, String telefone) {
		super();
		this.nome = new SimpleStringProperty(nome);
		this.telefone = new SimpleStringProperty(telefone);
	}
	
	public String getNome() {
        return nome.get();
    }

    public StringProperty NomeProperty() {
        return nome;
    }
    
    public String getTelefone() {
        return telefone.get();
    }

    public StringProperty TelefoneProperty() {
        return telefone;
    }

	


    
    
    
}
