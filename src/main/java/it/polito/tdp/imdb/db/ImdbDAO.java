package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer, Actor> idMap){
		String sql = "SELECT * FROM actors";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(!idMap.containsKey(res.getInt("id"))) {
					
					Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
							res.getString("gender"));
					
					idMap.put(actor.getId(), actor);
					
					
				}

			
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAllGeneri(){
		
		
		String sql = "SELECT DISTINCT genre "
				+ "FROM movies_genres ";
		
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getString("genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public List<Actor> getAttoriByGenere(String genere, Map<Integer, Actor> idMap){
		
		String sql="SELECT DISTINCT r.actor_id as a "
				+ "FROM roles r, movies_genres mg "
				+ "WHERE r.movie_id = mg.movie_id AND "
				+ "mg.genre = ? ";
		
		
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				if(idMap.containsKey(res.getInt("a"))) {
					result.add(idMap.get(res.getInt("a")));
				}
				
					
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Adiacenza> getAllAdiacenze(Map<Integer, Actor> idMap, String genere){
		
		
		String sql= "SELECT r1.actor_id, r2.actor_id, count(*) as peso "
				+ "FROM roles r1, roles r2, movies_genres mg1, movies_genres mg2 "
				+ "WHERE r1.movie_id = mg1.movie_id AND "
				+ "r2.movie_id = mg2.movie_id AND "
				+ "r1.actor_id > r2.actor_id AND "
				+ "mg1.genre = mg2.genre AND "
				+ "r1.movie_id = r2.movie_id "
				+ "AND mg1.genre=? "
				+ "GROUP BY r1.actor_id, r2.actor_id ";
		
		
		List<Adiacenza> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(idMap.containsKey(res.getInt("r1.actor_id")) && idMap.containsKey(res.getInt("r2.actor_id"))) {
					
					result.add(new Adiacenza(idMap.get(res.getInt("r1.actor_id")), idMap.get(res.getInt("r2.actor_id")), res.getInt("peso")));
					
				}
				

				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
	
	
	
	
}
