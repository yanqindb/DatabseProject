package Logging;

public class Main {
	public  static boolean loadFile() {
		return false;
	}
	public static boolean loadLog() {
		// TODO Auto-generated method stub
		return true;
	}
	public boolean increaseTrigger(){
		return true;
	}
	public static void main(String[] args) {
		
		boolean loadOK=loadFile();
		if(!loadOK){
			loadLog();
		}
		
	}
	

}
