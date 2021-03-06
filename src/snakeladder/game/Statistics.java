// Created by thurs 11 - team 9
package snakeladder.game;
import java.util.HashMap;

public class Statistics {
	private HashMap<Integer, Integer> dieValuesCount = new HashMap<>();
	private HashMap<String, Integer> connectionsCount = new HashMap<>();
	
	public Statistics(int numberDie) {
		int i;
		for(i=numberDie; i<=6*numberDie; i++) {
			dieValuesCount.put(i, 0);
		}
		
		connectionsCount.put("up", 0);
		connectionsCount.put("down", 0);
	}
	
	public HashMap<Integer, Integer> getDieValuesCount(){
		return dieValuesCount;
	}
	
	public HashMap<String, Integer> getConnectionsCount(){
		return connectionsCount;
	}
	
	public void addDieRoll(int roll) {
		int current = dieValuesCount.get(roll);
		dieValuesCount.put(roll, current + 1);
	}
	
	public void addConnection(String direction) {
		int current = connectionsCount.get(direction);
		connectionsCount.put(direction, current + 1);
	}
	
}
