package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> idMap;
	private Simulatore sim;
	
	public Model() {
		
		dao = new ImdbDAO();
		idMap = new HashMap<Integer, Actor>();
		dao.listAllActors(idMap);
		sim = new Simulatore();
		
	}

	public List<String> getAllGeneri(){
		
		List<String> generi= dao.getAllGeneri();
		
		Collections.sort(generi);
		
		return generi;
	}
	
	public void creaGrafo(String genere) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		
		//vertici
		Graphs.addAllVertices(this.grafo, dao.getAttoriByGenere(genere, idMap));
		
		
		//archi
		for (Adiacenza a: dao.getAllAdiacenze(idMap, genere)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			
		}
		
		
		
	}
	
	public Integer getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Actor> getAttori(){
		
		List<Actor> attori = new ArrayList<>(this.grafo.vertexSet());
		
		Collections.sort(attori);
		return attori;
		
	}

	public List<Actor> cercaSimili(Actor a) {
		
		
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<Actor, DefaultWeightedEdge>(grafo);
		
		List<Actor> result = new ArrayList<>(ci.connectedSetOf(a));
		result.remove(a);
		
		Collections.sort(result);
		
		return result;
		
	}
	
	
	public void init(Integer giorni) {
		sim.init(giorni, grafo);
	}
	
	public void run() {
		sim.run();
	}
	
	
	public Integer getNPausa() {
		
		return sim.getNPausa();
		
	}
	
	public List<Actor> getAttoriIntervistati(){
		return sim.getAttoriIntervistati();
	}
	 
	
	
}
