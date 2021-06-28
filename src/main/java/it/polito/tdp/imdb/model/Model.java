package it.polito.tdp.imdb.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> idMap;
	
	public Model() {
		
		dao = new ImdbDAO();
		idMap = new HashMap<Integer, Actor>();
		dao.listAllActors(idMap);
		
		
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
	
	
}
