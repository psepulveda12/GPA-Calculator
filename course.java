// Peter Sepulveda (pss4hk)

import java.util.TreeMap;

public class course {

	protected String status;
	protected int hours;
	protected String grade;
	protected String name;
	
	// Getters for fields - No need for setters because never used
	public String getStatus() {
		return status;
	}
	public int getHours() {
		return this.hours;
	}
	public String getGrade() {
		return grade;
	}
	public String getName() {
		return name;
	}

	
	// Using 4 different constructors to accommodate for all of the combinations of mandatory and optional inputs
	public course(String status, int hours, String grade, String name) {
		this.status = status;
		this.hours = hours;
		this.grade = grade;
		this.name = name;
	}
	
	public course(String status, int hours) {
		this.status = status;
		this.hours = hours;
	}
	
	public course(String status, String name, int hours) {
		this.status = status;
		this.name = name;
		this.hours = hours;
	}
	
	public course(String status, int hours, String grade) {
		this.status = status;
		this.hours = hours;
		this.grade = grade;
	}
	
	// Method that will be called to convert a letter grade to a decimal value
	public double letterToPoints(String grade) {
		TreeMap<String,Double> pointScale = new TreeMap<String,Double>();
		pointScale.put("A+", 4.0);
		// Looks good but no numerical effect
		pointScale.put("A", 4.0);
		pointScale.put("A-", 3.7);
		pointScale.put("B+", 3.3);
		pointScale.put("B", 3.0);
		pointScale.put("B-", 2.7);
		pointScale.put("C+", 2.3);
		pointScale.put("C", 2.0);
		pointScale.put("C-", 1.7);
		pointScale.put("D+", 1.3);
		pointScale.put("D", 1.0);
		pointScale.put("D-", 0.7);
		pointScale.put("F", 0.0);
		// no such thing as F+????
		double points = pointScale.get(grade);
		return points;
	}
	
	public String toString() {
		// Four different constructors to accommodate the four different combinations of inputs
		if(this.grade == null && this.name == null) {
			return  "Credits: " + this.hours + " |  " + this.status;
		}
		else if(this.grade == null) {
			return "Credits: " + this.hours  + " |  " + this.name + " |  " + this.status;
		}
		else if(this.name == null) {
			return "Credits: " + this.hours  + " |  " + this.grade + " |  " + this.status;
		}
		else {
			return "Credits: " + this.hours  + " |  " + this.name + " |  " + this.grade + " |  " + this.status;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
