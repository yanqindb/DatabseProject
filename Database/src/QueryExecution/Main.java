package QueryExecution;

import java.util.ArrayList;

/**
 * This project implements join of two relations
 * @author Yan Wang and Zishan Qin
 *
 */
public class Main {

	public static void main(String[] args) {
		
		//Memory is large enough to hold numBlocks blocks of one relation
		int numBlocks = 10;
		
		//Indices of relation "left" and relation "right"
		//Indices of population
		int popLeft = 6;
		int popRight = 4;
		//Indices of country
		int countryLeft = 0;
		int countryRight = 2;
		//Index of returned attribute
		int indexResult = 1;
		
		ArrayList<String> joinResult = null;
		
		//Relation country--left relation
		Relation country = new Relation("Country.csv");
		//Relation city--right relation
		Relation city = new Relation("City.csv");
		
		//Relation try create and write to a relation
		Relation newRelation = new Relation("CityNew.csv");
		String newRecord1 = "\"Beijing\",\"100081\"";
		String newRecord2 = "\"Boston\",\"01609\"";
		newRelation.put(newRecord1);
		newRelation.put(newRecord2);
		
		//Join of two relations
		JoinRelation joinRelation = new JoinRelation(country, city, popLeft, popRight, countryLeft, countryRight, indexResult, numBlocks);
		joinResult = joinRelation.simpleJoin();
		
		//Show returned results
		System.out.println("Cities returned from query are:");
		for(int id = 0;id<joinResult.size();id++){
			System.out.println(id+joinResult.get(id));
		}

	}

}
