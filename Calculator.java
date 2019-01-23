// Peter Sepulveda (pss4hk)
// Sources
/*
 * http://www.fredosaurus.com/notes-java/GUI/components/10labels/12labelfontcolor.html
 * http://www.java2s.com/Tutorial/Java/0240__Swing/HorizontalAlignmentCENTER.htm
 * https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html
 * https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
 * http://web.mit.edu/6.005/www/sp14/psets/ps4/java-6-tutorial/components.html
 */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;


public class Calculator extends JFrame {

	JPanel inputPanel; 		// Holds input boxes for name, grade, hours, status
	JPanel outputPanel;		// Holds items for GPA and target GPA
	
	// Fields for Course Page
	JLabel instructions;
	JComboBox courseStatus;
	JTextField courseHours;
	JTextField courseGrade;
	JTextField courseName;
	JButton addCourse;
	JButton addFifteen;
	JButton removeAll;
	JButton remove;
	JLabel classListName;	// Header for course list
	JPanel titleList;		// Contains classListName
	// For class list
	JList classList;
	DefaultListModel listModel;
	JScrollPane classListPane;
	
	// Fields for Summary  Page
	JLabel current;
	JLabel target;
	JTextField targetBox;
	JLabel reqInst;
	JLabel message;
	JButton calculate;
	JLabel reqLabel;
	
	// Fields for both pages
	
	// ArrayLists that contain data used for compiling GPA information
	ArrayList<course> courses = new ArrayList<course>(); 		// Contains all courses
	ArrayList<course> blankCourses = new ArrayList<course>();	// Contains classes with no grades
	ArrayList<course> gradedCourses = new ArrayList<course>();	// Contain classes with grades
	
	// Buttons to switch between pages
	JButton courseButton;
	JButton summaryButton;
	

	public static void main(String[] args) {
		
		
		new Calculator();
		
	}
	
	// Calculates GPA based on courses added with grades
	public String gpaCalculator() {
		int credits = 0;
		double totalPoints = 0;
		for(course c : gradedCourses) {
			try {
				totalPoints += c.getHours() * c.letterToPoints(c.getGrade());
				credits += c.getHours();
			}
			catch(NullPointerException E) {
				
			}
		}
		DecimalFormat df = new DecimalFormat("##.###");
		return df.format(totalPoints / credits);
		// If gradedCourses in empty, NaN will be displayed in the summary page - indicating that no graded classes have been added
	
	}
	
	public double targetCalculator(double target) {
		int credits = 0;
		for(course c : gradedCourses) {
			credits += c.getHours();
		}
		int blankCredits = 0;
		for(course b : blankCourses) {
			blankCredits += b.getHours();
		}
		// Rewritten equation to solve for required GPA
		double gpaPerCredit = (target * (credits + blankCredits) - 
				Double.parseDouble(gpaCalculator()) * credits) / blankCredits;
		// As there are too many factors to consider with trying to calculate the GPA needed for a 3 credit class vs a 4 credit class
		// as in order to solve for the GPA for each a system of equations would be required that once different credit hours for classes
		// are added, there are too many factors and combinations to determine the GPA for each class of a specific credit hour.
		// Therefore, there will only be required GPA for all courses, regardless of credit hours.
		return gpaPerCredit;
	}
	
	public Calculator() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setTitle("GPA Calculator");
	    int heightTotal = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int widthTotal = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	    this.setSize(widthTotal / 2, heightTotal - heightTotal / 10);
	    // Sets the frame to a specific size that will adjust according to the screen resolution and size
	    // Assume that computers with resolutions from the early 2000s will probably not be used, as most present day
	    // students will use computers that are only a few years old and not their parents laptops from the '90s.
	    this.setLocationRelativeTo(null);
	    // Will open at center of screen
	    int width = widthTotal / 2;
	    int height = heightTotal - heightTotal / 10;
	    // These redefine the frame length and width with respect to width and length of the frame, not the screen

	    // Buttons give the user the ability to toggle back and forth between course and summary pages
	    courseButton = new JButton("Course Page"); 
	    summaryButton = new JButton("Summary Page");
	    courseButton.setSize(width / 3, height / 30);
	    summaryButton.setSize(width / 3, height / 30);
	    courseButton.setFont(new Font("Arial", Font.BOLD, 36));
	    summaryButton.setFont(new Font("Arial", Font.PLAIN, 36));
	    // When opened, the GUI will be on the course management component, so the course button text will be bolded
	    courseButton.setLocation(width / 2 - width / 5 - courseButton.getWidth() / 2, height / 20);
	    summaryButton.setLocation(width / 2 + width / 5 - summaryButton.getWidth() / 2, height / 20);
	    // Located towards the top
	    courseButton.addActionListener(new addCoursePageListener());
		summaryButton.addActionListener(new addSummaryPageListener());
	    this.add(courseButton);
	    this.add(summaryButton);
	    
	    // Instructions Label
	    instructions = new JLabel("Class Status:                  Course Credits:          "
	    		+ "Course Grade (Optional):         Course Name (Optional):");
	    // Just below course and summary buttons that instructs that hours and status must be selected, and if there is no name
	    // or grade, those Text Fields should not be touched by the user
	    instructions.setSize(width, height / 50);
	    instructions.setFont(new Font("Arial", Font.BOLD, 28));
	    instructions.setLocation(width / 7, height / 8);
	    instructions.setVisible(true);
	    this.add(instructions);
	    
	    // Panel that contains all the components used to input information regarding a course
	    inputPanel = new JPanel();
	    inputPanel.setLayout(new FlowLayout());		// Puts data input components into horizontal row
	    inputPanel.setSize(width, height / 20);
	    inputPanel.setLocation(0, height / 7);
	    String[] statuses = {"Previously Taken    ", "Currently Taking    ", "Anticipated    "};   // Spaces added to widen ComboBox
	    courseStatus = new JComboBox(statuses); 	// Ability to select from various class statuses
	    courseHours = new JTextField("                           ");
	    courseGrade = new JTextField("                              ");
	    courseName = new JTextField("                                         ");
	    courseStatus.setFont(new Font("Arial", Font.PLAIN, 38));
	    courseHours.setFont(new Font("Arial", Font.PLAIN, 38)); 
	    courseGrade.setFont(new Font("Arial", Font.PLAIN, 38));
	    courseName.setFont(new Font("Arial", Font.PLAIN, 38));
	    inputPanel.add(courseStatus);
	    inputPanel.add(courseHours);
	    inputPanel.add(courseGrade);
	    inputPanel.add(courseName);
	    // All data variable inputs assigned in a specific order in a row
	    inputPanel.setVisible(true);
	    this.add(inputPanel);

	    // Panel 4 - Add Button
	    addCourse = new JButton("Add Course"); 					// Creates larger button
	    addCourse.setSize(width / 2, height / 30);				// Wide button
	    addCourse.setFont(new Font("Arial", Font.BOLD, 40));
	    addCourse.setLocation(width / 4, height / 5);			// Located directly below row of inputs
	    addCourse.addActionListener(new addCourseListener());
	    addCourse.setVisible(true);
	    this.add(addCourse);

	    // Class List Title
	    classListName = new JLabel("List of Courses:");
	    classListName.setSize(width / 2, height / 25);
	    classListName.setFont(new Font("Arial", Font.BOLD, 42));
	    classListName.setHorizontalAlignment(JLabel.CENTER);   		// Puts label at horizontal center of frame
	    // http://www.java2s.com/Tutorial/Java/0240__Swing/HorizontalAlignmentCENTER.htm
	    classListName.setVisible(true);

	    // TitlePanelList - Panel that contains the header for the course list
	    titleList = new JPanel();
	    titleList.setSize(width / 2, height / 25);
	    titleList.setLocation(width / 4, height / 4 + height / 40);
	    titleList.add(classListName);
	    titleList.setVisible(true);
	    this.add(titleList);
	    
	    // List of Classes
	    // https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html
	    // https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
	    listModel = new DefaultListModel();			// Will contain the courses put into the list
	    classList = new JList(listModel);	
	    classList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); 	
	    // Only one class can be selected at a time - For remove course button
	    classList.setLayoutOrientation(JList.VERTICAL);
	    // Courses arranged in vertical list
	    classList.setVisibleRowCount(-1);
	    // Will show all courses the list can without having to scroll (i.e. number of courses exceeding the size of the list)
	    classList.setFont(new Font("Arial", Font.PLAIN, 36));
	    classListPane = new JScrollPane(classList);
	    // Permits for addition of a scroll bar so that the list of classes can be navigated
	    // ASSUMPTION - HW instructions say no scrolling - interpreted as no scrolling for the frame itself, but for components it is OK because 
	    // in order to hold 40-50 courses, there has to be a way to manipulate space
	    classListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    // Initiates scroll bar
	    classListPane.setWheelScrollingEnabled(true);
	    // You can use the mouse wheel to navigate! - Found through looking at methods after '.' is typed
	    classListPane.setSize(width - width / 4, height / 3 + height / 15);
	    classListPane.setLocation(width / 8, height / 4 + height / 15);
	    classListPane.setVisible(true);
	    this.add(classListPane);
	    
	    
	    // Remove, RemoveAll, AddFifteen all added one after another vertically after the list
	    // Remove Button - Intended to remove the course that is selected from the list
	    remove = new JButton("Remove Selected Course");
	    remove.setSize(width / 2, height / 30);
	    remove.setFont(new Font("Arial", Font.BOLD, 40));
	    remove.setLocation(width / 4, height - height / 4);
	    remove.addActionListener(new removeCourseListener());
	    remove.setVisible(true);
	    this.add(remove);
	    
	    // RemoveAll Button - Will clear all courses from list and ArrayLists
	    removeAll = new JButton("Remove All Courses");
	    removeAll.setSize(width / 2, height / 30);
	    removeAll.setFont(new Font("Arial", Font.BOLD, 40));
	    removeAll.setLocation(width / 4, height - height / 5);
	    removeAll.addActionListener(new removeAllCoursesListener());
	    removeAll.setVisible(true);
	    this.add(removeAll);
	    
	    // Add 15 Blank Courses
	    // ASSUMPTION - As 3 credit courses are the most common types - 
	    // assumption made that the button will add 5 blank courses worth 3 credits each
	    addFifteen = new JButton("Add 5 Blank 3-Credit Courses");
	    addFifteen.setSize(width / 2, height / 30);
	    addFifteen.setFont(new Font("Arial", Font.BOLD, 40));
	    addFifteen.setLocation(width / 4, height - height / 7);
	    addFifteen.addActionListener(new addFifteenCoursesListener());
	    addFifteen.setVisible(true);
	    this.add(addFifteen);
	    
	    // Instructions for summary page on target and required GPA
	 	reqInst = new JLabel("Below is your current GPA. To calculate required GPA "
	 			+ "for blank courses, enter target GPA and click 'Calculate Requirements'");
	 	reqInst.setSize(width, height / 50);
	 	reqInst.setFont(new Font("Arial", Font.BOLD, 28));
	 	reqInst.setLocation(width / 15, height / 10);
	 	reqInst.setVisible(false);
	 	this.add(reqInst);
	    
	    // Output Panel - Contains label for current GPA and textfield for target GPA
	    outputPanel = new JPanel();
	    outputPanel.setLayout(new FlowLayout());	// Components in a specific order
	    outputPanel.setSize(width, height / 20);
	    outputPanel.setLocation(0, height / 7);
	    outputPanel.setVisible(false);
	    // NOTE - as summary page is not visible at first, all the components of the summary page will be set to not visible initially
	    
	    // Individual components for output panel
	    current = new JLabel("Current GPA:   " + gpaCalculator() + "               ");		// Extra space puts some room between components
	    target = new JLabel("Target GPA: ");			// Label for TextField where the target GPA in inputed
	    targetBox = new JTextField(width / 200);
	    current.setFont(new Font("Arial", Font.PLAIN, 40));
	    target.setFont(new Font("Arial", Font.PLAIN, 40));
	    targetBox.setFont(new Font("Arial", Font.PLAIN, 40));
	    outputPanel.add(current);
	    outputPanel.add(target);
	    outputPanel.add(targetBox);
	    this.add(outputPanel);

	    // Button that will process the target GPA and display the required GPA
	    calculate = new JButton("Calculate Requirements"); 
	    calculate.setSize(width / 2, height / 30);
	    calculate.setFont(new Font("Arial", Font.BOLD, 40));
	    calculate.setLocation(width / 4, height / 5);
	    // Below output panel
	    calculate.addActionListener(new calculateGpaListener());
	    calculate.setVisible(false);
	    this.add(calculate);
	    // Instructions, output, and calculate button mirror locations on the course page for aesthetic purposes
	    
	    // Required GPA label - Blank initially - will pop up when classes have been added and a valid target GPA has been entered
	    reqLabel = new JLabel("");
	    reqLabel.setFont(new Font("Arial", Font.BOLD, 48));
	    reqLabel.setSize(width / 2, height / 30);
	    reqLabel.setLocation(width / 4, height / 4 + height / 40);
	    reqLabel.setHorizontalAlignment(JLabel.CENTER);
	    reqLabel.setVisible(false);
	    this.add(reqLabel);
	    
		// Message - Will display a message regarding the situations described in the instruction sheet
	    // Will also handle some exception handling such as incorrectly inserted data.
		message = new JLabel("");
		message.setFont(new Font("Arial", Font.PLAIN, 40));
		message.setSize(width - width / 10, height / 10);
		message.setLocation(width / 20, height / 2);
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setVisible(false);
		this.add(message);
		
	    this.setVisible(true);   

	}
	
	private class addCoursePageListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// When course page clicked - summary components visibility turned off and course components turned on
			outputPanel.setVisible(false);
			reqInst.setVisible(false);
			calculate.setVisible(false);
			reqLabel.setVisible(false);
			message.setVisible(false);
			courseButton.setFont(new Font("Arial", Font.BOLD, 36));
			summaryButton.setFont(new Font("Arial", Font.PLAIN, 36));
			// When course page active, course button text set to bold and summary plain
			// BOLD designates current page
			instructions.setVisible(true);
			inputPanel.setVisible(true);
			addCourse.setVisible(true);
			classListName.setVisible(true);
			titleList.setVisible(true);
			classListPane.setVisible(true);
			remove.setVisible(true);
			removeAll.setVisible(true);
			addFifteen.setVisible(true);
			classList.setVisible(true);
			
		}
		
	}
	
	private class addSummaryPageListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// When summary page clicked - course components visibility turned off and summary components turned on
			summaryButton.setFont(new Font("Arial", Font.BOLD, 36));
			courseButton.setFont(new Font("Arial", Font.PLAIN, 36));
			// As mentioned above - use of bold
			instructions.setVisible(false);
			inputPanel.setVisible(false);
			addCourse.setVisible(false);
			classListName.setVisible(false);
			classList.setVisible(false);
			remove.setVisible(false);
			removeAll.setVisible(false);
			addFifteen.setVisible(false);
			titleList.setVisible(false);
			classListPane.setVisible(false);
			outputPanel.setVisible(true);
			reqInst.setVisible(true);
			calculate.setVisible(true);
			reqLabel.setVisible(true);
			message.setVisible(true);
			
		}
		
	}
	
	private class addCourseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {	
			// Exception handling in case incorrect formatting in entered
			String coursegrade;
			String coursename;
			String coursestatus;
			int coursehours;
			try {
				coursegrade = courseGrade.getText().trim();
			}
			finally {}
			try {
				coursename = courseName.getText().trim();
			}
			finally {}
			try {
				coursestatus = courseStatus.getSelectedItem().toString().trim();
			}
			finally {}
			try {
				coursehours = Integer.parseInt(courseHours.getText().trim());
			}
			finally {}
				
				// If only the bare minimum is inputed
				if((coursegrade.equals("")) && coursename.equals("")) {
					course c = new course(coursestatus, coursehours);
					// create new course from data
					listModel.addElement(c.toString());
					// Add to displayed list
					courses.add(c);
					// Add to all courses
					blankCourses.add(c);
					// Since no grade - is a blank course
					// blankCourses used for future GPA calculations
					courseHours.setText("                           ");
					// Resets initial text in TextField to avoid confusion
				}
				// If name but no grade
				else if(coursegrade.equals("")) {
					course c = new course(coursestatus, coursename, coursehours);
					// Utilizes different constructor to handle the different combinations of inputed data
					listModel.addElement(c.toString());
					courses.add(c);
					blankCourses.add(c);
					courseHours.setText("                           ");
					courseName.setText("                                         ");
				}
				// If grade but no name
				else if(coursename.equals("")) {
					course c = new course(coursestatus, coursehours,
							courseGrade.getText().trim());
					listModel.addElement(c.toString());
					courses.add(c);
					gradedCourses.add(c);
					courseHours.setText("                           ");
					courseGrade.setText("                              ");
					current.setText("Current GPA:   " + gpaCalculator() +"               ");
					// Even though the component is no visible, this refreshes the GPA from behind the scenes
				}
				else {
					// If all components have inputed data
					course c = new course(coursestatus, coursehours,coursegrade, coursename);
					listModel.addElement(c.toString());
					courses.add(c);
					gradedCourses.add(c);
					courseHours.setText("                           ");
					courseGrade.setText("                              ");
					courseName.setText("                                         ");
					current.setText("Current GPA:   " + gpaCalculator() + "               ");
				}
			
			
		}
			
		}
		
	
	private class removeCourseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int rem = classList.getSelectedIndex();
				listModel.remove(rem);
				course c = courses.get(rem);
				// Must use integer locations as c is of type course while listModel is of type Object
				// This method of locating and removing can be done as listModel and courses will have the same order of classes as
				// they are both affected in the same way at the same time;
				courses.remove(c);
				// As order is not conserved, conditional blocks must be used to determine which ArrayList the course is in
				if(gradedCourses.contains(c)) {
					gradedCourses.remove(c);
				}
				if(blankCourses.contains(c)) {
					blankCourses.remove(c);
				}
				current.setText("Current GPA:   " + gpaCalculator() + "               ");
				// Once again, resets GPA from behind the scenes
			}
			catch(ArrayIndexOutOfBoundsException E) {
				
			}
		}
	}
	
	private class removeAllCoursesListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			listModel.removeAllElements();
			courses.clear();
			blankCourses.clear();
			gradedCourses.clear();
			// Very straightforward - Get rid of everything literally means get rid of everything.
			current.setText("Current GPA:   " + gpaCalculator() + "               ");
			// Will reset GPA to NaN from behind the scenes
		}
		
	}
	
	private class addFifteenCoursesListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int i = 0;
			while(i < 5) {
				course c = new course("Anticipated", 3);
				listModel.addElement(c);
				courses.add(c);
				blankCourses.add(c);
				i++;
				// Adds the identical course 5 times
			}
		}
		
	}
	
	private class calculateGpaListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			message.setText("");
			try {
				double targetGpa = Double.parseDouble(targetBox.getText().trim());
				System.out.println(targetGpa);
				if(targetGpa > 4 || targetGpa < 0) {
					message.setText("Enter a valid target GPA value");
				}
				double gpa = targetCalculator(targetGpa);
				System.out.println(gpa);
				DecimalFormat df = new DecimalFormat("#.###");
				if(gpa > 4.0) {
					reqLabel.setText("Required GPA Per Course: " + df.format(gpa));
					// Changes the content of reqLabel from a blank to actual text with the information
					reqLabel.setForeground(Color.RED);
					// As a GPA > 4.0 is not possible in reality - color change to red to indicate problem with users idea
					// http://www.fredosaurus.com/notes-java/GUI/components/10labels/12labelfontcolor.html
					message.setText("You need to take more credit hours to get your GPA to your target");
					// Blunt statement about what needs to be done to fix the GPA
				}
				if(gpa < 2.0) {
					reqLabel.setForeground(Color.RED);
					message.setText("You should take fewer credit hours.");
					reqLabel.setText("Required GPA Per Course: " + df.format(gpa));
				}
				if (gpa >= 2.0 && gpa <= 4.0){
					reqLabel.setText("Required GPA Per Course: " + df.format(gpa));
					reqLabel.setForeground(Color.BLACK);				
					message.setText("\"C's get degrees!!!!\" - Person with low GPA");
				}
				}
			catch(NumberFormatException E) {
				message.setText("Enter a valid target GPA");
			}
			
		}
		
	}

}


