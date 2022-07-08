package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	private List<String> best; //per cammino migliore, vertici sono String
	//peso migliore è la size della lista, non è pesato
	
	public Model() {
		dao=new EventsDao();
	}
	public List<Adiacenza> creaGrafo(String categoria, int mese) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Adiacenza> archi= new LinkedList<>();
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		
		//aggiungo archi
		for(Adiacenza a:dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(), a.getPeso());
			archi.add(a);
			
		}
		
		System.out.println("vert "+ this.grafo.vertexSet().size());
		System.out.println("archi "+ this.grafo.edgeSet().size());
		
		return archi;

	}
	
	public List<Adiacenza> getArchiMaggPesoMedio(){
		//scorro archi e calcolo peso medio
		double pesoTot=0.0;
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			pesoTot+= this.grafo.getEdgeWeight(e);
		}
		double avg=pesoTot/this.grafo.edgeSet().size();
		System.out.println("peso medio: "+avg);

		//riscorro per trovare maggiori
		List<Adiacenza> result=new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>avg) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e),(int) this.grafo.getEdgeWeight(e)));
			}
		}
		return result;
				
		
		
	}
	

	//metodo che lancia le cose
	public List<String> calcolaPercorso(String sorgente, String destinazione){
		//arriva il mio arco selezionato
		best=new LinkedList<String>();
		List<String> parziale=new LinkedList<>();
		parziale.add(sorgente); //sicuramente è il primo passo
		cerca(parziale, destinazione); //ho già messo a livello 0 sorgente- tolgo livello non serve
		return best;
	}
	private void cerca(List<String> parziale, String destinazione) {
		//condizione di terminazione=arrivo a destinazione
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			//è la soluzione migliore?
			if(parziale.size()>best.size()) {
				best=new LinkedList<>(parziale); //sovrascrivo con parziale che è migliore
			}
			return;
		}
		
		//al max la soluzione è l'arco stesso
		
		
		//ora faccio ricorsione
		//scorro i vicini dell'ultimo inserito e provo le varie strade
		//come recupero i vicini di un nodo nel grafo? neighborListOf
		for(String v: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			//aciclico
			if(!parziale.contains(v)) { 			
				parziale.add(v);
				cerca(parziale, destinazione);
				
				//backtracking
				parziale.remove(parziale.size()-1);
			}
		}
		
			
		
	}
	public List<String> getCategories(){
		return dao.getCategories();
	}
	
	public int getNVert() {
		return 		this.grafo.vertexSet().size();

	}
	public int getNArchi() {
		return 		this.grafo.edgeSet().size();

	}
	
	
}
