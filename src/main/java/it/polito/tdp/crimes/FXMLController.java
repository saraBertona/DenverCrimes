/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	if(boxArco.getValue()==null) {
        	txtResult.appendText("Devi creare fare analisi quartieri e selezionare un arco");
        	return;

    	}
    	List<String> percorso=new LinkedList<>(model.calcolaPercorso(boxArco.getValue().getV1(), boxArco.getValue().getV2()));
		txtResult.appendText(percorso.toString());
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();

    	if(boxCategoria.getValue()!=null &&boxMese.getValue()!=null) {
    		List<Adiacenza> archi=new LinkedList<>(model.creaGrafo(boxCategoria.getValue(), boxMese.getValue()));
    		int nArchi=model.getNArchi();
    		int ver=model.getNVert();
    		txtResult.setText("Grafo Creato!\nNumero vertici: "+ver+"\nNumero archi: "+nArchi+"\n");
    		boxArco.getItems().addAll(archi);
    		


    	}
    	else {
    		txtResult.setText("Devi selezionare categoria e mese");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxCategoria.getItems().addAll(model.getCategories());
    	List<Integer> mesi=new ArrayList<>();
    	for(int i=1; i<=12; i++) {
    		mesi.add(i);
    		
    	}
    	boxMese.getItems().addAll(mesi);
    	
    	
    }
}
