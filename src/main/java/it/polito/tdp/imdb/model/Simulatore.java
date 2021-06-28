package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {
	
	/*
	 * si vogliono simulare gli attori che un produttore cinematografico
	 * intervista, in n giorni consecutivi, allo scopo di scegliere gli attori
	 * del suo prossimo film.
	 */

	
	
	//stato del mondo --> ogni giorno un produttore intervista un attore del grafo, n giorni
	
	private boolean doCasuale;
	private List<Actor> daIntervistare;
	
	
	
	//tipi di evento + coda?
	
	
	
	//parametri input
	
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private int giorni;
	private int nPausa;
	
	
	//parametri output
	
	List<Actor> attoriIntervistati;
	
	
	//inizializzo simulatore
	
	public void init(Integer giorni, Graph<Actor, DefaultWeightedEdge> grafo) {
		
		this.grafo = grafo;
		this.giorni = giorni;
		attoriIntervistati = new ArrayList<Actor>();
		daIntervistare = new ArrayList<Actor>(grafo.vertexSet());
		doCasuale = true;
		nPausa = 0;
		
		
		
	}
	
	public void run() {
		
		int pCasuale = (int) Math.random()*daIntervistare.size();
		double p60e40 = Math.random();
		double p90e10 = Math.random();
		
		for(int i=1; i<=giorni ; i++) {
			
			
			if(doCasuale == true) {
				
				//primo giorno o giorno dopo la pausa, sceglo
				//casualmente da quelli che devo ancora visitare
				
				doCasuale = false;
				attoriIntervistati.add(daIntervistare.get(pCasuale));
				daIntervistare.remove(pCasuale);
				
			}else {
				
				//sono nel caso 60-40 e devo dividere i due casi + controllo del genere
				
				if(attoriIntervistati.size() > 1) {
					
					//controllo di genere 
					
					Actor ultimo = attoriIntervistati.get(attoriIntervistati.size()-1);
					Actor penultimo = attoriIntervistati.get(attoriIntervistati.size()-2);
					
					if(ultimo.getGender().equals(penultimo.getGender())) {
						
						//stesso genere: 90 % pausa, 10% continua a intervistare --> 60 / 40
						
						if(p90e10<0.9) {
							
							nPausa ++;
							doCasuale = true;
							
						}
						
					}
					
					
					
				}
				
			if(!doCasuale) {
				
				
					if(p60e40 < 0.6) {
					
					//60% --> casuale tra quelli ancora da intervistare
					
						attoriIntervistati.add(daIntervistare.get(pCasuale));
						daIntervistare.remove(pCasuale);
					
					
					
					}else {
					
					//40% --> guarda quello intervistato prima e sceglie tra i suoi vicini
					
						Actor precedente = attoriIntervistati.get(attoriIntervistati.size()-1);
					
						Actor vicino = cercaVicino(precedente);
					
						if(vicino == null) {
						//non ho il vicino, scelgo casualmente
							
							attoriIntervistati.add(daIntervistare.get(pCasuale));
							daIntervistare.remove(pCasuale);
						
					}
					
					attoriIntervistati.add(vicino);
					daIntervistare.remove(vicino);
					
					
				}
				
			}
			
			
			
			
			
			}	
			
		}
		
		
		
	}

	private Actor cercaVicino(Actor precedente) {
		
		//Quest’ultimo suggerisce al produttore di intervistare il
		//collega con cui ha recitato più film di genere g
		//(ovvero il suo “vicino”, nel grafo, di “grado” massimo).
		
		Actor best = null;
		Integer pesoBest = 0;
		List<Actor> vicini = Graphs.neighborListOf(this.grafo, precedente);
		
		
		if(vicini.size()==0) {
			
			//non ho vicini --> devo scegliere uno casualmente 
			return null;
		}
		
		
		for(Actor a: vicini) {
			
			
			if(this.grafo.getEdge(precedente, a) != null) {
				
				if(grafo.getEdgeWeight(this.grafo.getEdge(precedente, a)) > pesoBest) {
				
					best = a;
					pesoBest = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(precedente, a));
				}
				
			}else if(this.grafo.getEdge(a, precedente) != null) {
				
				if(grafo.getEdgeWeight(this.grafo.getEdge(a, precedente)) > pesoBest) {
					
					best = a;
					pesoBest = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(precedente, a));
				}
				
				
			}
			
		}
		
		
		return best;
	}
	
	
	public Integer getNPausa() {
		
		return nPausa;
		
	}
	
	public List<Actor> getAttoriIntervistati(){
		return attoriIntervistati;
	}
	
}
