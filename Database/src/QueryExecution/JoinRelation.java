package QueryExecution;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to get the join of two relations
 * @author Yan Wang and Zishan Qin
 *
 */
public class JoinRelation {

	//Relation that participate in join
	Relation left, right;
	//Indices for seletion
	int popLeft, popRight;
 	//Indices for join
	int countryLeft;
	int countryRight;
	//Index of returned attribute
	int indexResult;
	//Number of blocks memory can hold
	int numBlocks;
	ArrayList<String> results = new ArrayList<String>();
	
	/**
	 * Construct of the class
	 * @param left
	 * @param right
	 * @param popLeft
	 * @param popRight
	 * @param countryLeft
	 * @param countryRight
	 * @param indexResult
	 * @param numBlocks
	 */
	public JoinRelation(Relation left, Relation right, int popLeft, int popRight,int countryLeft, int countryRight, int indexResult, int numBlocks){
		this.left = left;
		this.right = right;
		this.popLeft = popLeft;
		this.popRight = popRight;
		this.countryLeft = countryLeft;
		this.countryRight = countryRight;
		this.indexResult = indexResult;
		this.numBlocks = numBlocks;
	}
	
	/**
	 * Go through two relation, and simple join
	 * @return
	 */
	public ArrayList<String> simpleJoin(){
		
		//Current record for comparison
		String [] leftRecordCurrent, rightRecordCurrent;
		
		//If not the end blocks of left relation
		while(left.open(numBlocks)==0)
		{
			//Point to the beginning of relation right
			right.resetBlock();
			//If not the end blocks of right relation
			while(right.open(numBlocks)==0){
				//Point to the beginning of block of left
				left.resetNext();
				while ((leftRecordCurrent = left.getNext())!=null)
				{
					//Point to the beginning of block of right
					right.resetNext();
					while((rightRecordCurrent = right.getNext())!=null)
					{
						//Join
						if(leftRecordCurrent[countryLeft].equals(rightRecordCurrent[countryRight])){
							String leftCompare = leftRecordCurrent[popLeft].substring(1,leftRecordCurrent[popLeft].length()-1);
							String rightCompare = rightRecordCurrent[popRight].substring(1,rightRecordCurrent[popRight].length()-1);
							//Select
							if(Integer.parseInt(leftCompare)*0.4< Integer.parseInt(rightCompare))
							{
								results.add(leftRecordCurrent[indexResult]);
							}
						}
					}
				}
			}
			
			// If right.open(numBlocks)==1, it comes to an end, but we still need to go through the last part of right
			left.resetNext();
			while ((leftRecordCurrent = left.getNext())!=null)
			{
				right.resetNext();
				while((rightRecordCurrent = right.getNext())!=null)
				{
					if(leftRecordCurrent[countryLeft].equals(rightRecordCurrent[countryRight])){
						String leftCompare = leftRecordCurrent[popLeft].substring(1,leftRecordCurrent[popLeft].length()-1);
						String rightCompare = rightRecordCurrent[popRight].substring(1,rightRecordCurrent[popRight].length()-1);
						if(Integer.parseInt(leftCompare)*0.4< Integer.parseInt(rightCompare))
						{
							results.add(leftRecordCurrent[indexResult]);
						}
					}
				}
			}
		}
		
		// If left.open(numBlocks)==1, it comes to an end, but we still need to go through the last part of left
		//Below is actually one iteration of the loop above
		right.resetBlock();
		while(right.open(numBlocks)==0){
			left.resetNext();
			while ((leftRecordCurrent = left.getNext())!=null)
			{
				right.resetNext();
				while((rightRecordCurrent = right.getNext())!=null)
				{
					if(leftRecordCurrent[countryLeft].equals(rightRecordCurrent[countryRight])){
						String leftCompare = leftRecordCurrent[popLeft].substring(1,leftRecordCurrent[popLeft].length()-1);
						String rightCompare = rightRecordCurrent[popRight].substring(1,rightRecordCurrent[popRight].length()-1);
						if(Integer.parseInt(leftCompare)*0.4< Integer.parseInt(rightCompare))
						{
							results.add(leftRecordCurrent[indexResult]);
						}
					}
				}
			}
		}
	
		left.resetNext();
		while ((leftRecordCurrent = left.getNext())!=null)
		{
			right.resetNext();
			while((rightRecordCurrent = right.getNext())!=null)
			{
				if(leftRecordCurrent[countryLeft].equals(rightRecordCurrent[countryRight])){
					String leftCompare = leftRecordCurrent[popLeft].substring(1,leftRecordCurrent[popLeft].length()-1);
					String rightCompare = rightRecordCurrent[popRight].substring(1,rightRecordCurrent[popRight].length()-1);
					if(Integer.parseInt(leftCompare)*0.4< Integer.parseInt(rightCompare))
					{
						results.add(leftRecordCurrent[indexResult]);
					}
				}
			}
		}
		return results;
	}
}
