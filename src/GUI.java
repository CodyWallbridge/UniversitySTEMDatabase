//------------------------------------------------------------------------------
// NAMES: Cody Wallbridge, Kajal Tomar
// COURSE: COMP3380, SECTION: A02
// INSTRUCTOR: Adam Pazdor
//
// REMARKS: This is the code for the GUI. Note that if oyu print the results
//          to a CSV it will be found in the src folder in a file
//          called 'STEM_DB_SAVED_RESULT.csv'
//-------------------------------------------------------------------------------

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;


public class GUI{
    private static Database db;
    private static int[] currentQuery;
   // private static int currentQueryType;
    private static JFrame f;


    public static void main(String[] args) {
        // setup
        db = new Database();
        currentQuery = new int[3];

        f = new JFrame("University of Manitoba STEM Database (2019/2020)");//creating instance of JFrame
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // show the welcome page
        startingPoint();

    }

    // -----------------------------------------------------------------------------------------
    // startingPoint
    //
    // PURPOSE: welcome page.
    // -----------------------------------------------------------------------------------------
    public static void startingPoint(){
        JPanel p = new JPanel();
        final JLabel welcomeLabel = new JLabel("Welcome to the University of Manitoba STEM database");
        final JLabel ourNames = new JLabel("Made by Cody Wallbridge and Kajal Tomar");
        final JRadioButton courses = new JRadioButton("Courses");
        final JRadioButton departments = new JRadioButton("Departments");
        final JRadioButton faculties = new JRadioButton("Faculties");

        final JLabel categories = new JLabel("Pick the category that you are curious about: ");

        p.setLayout(null);

        // set the parameters for all the components
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setSize(800,200);
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 20));

        ourNames.setHorizontalAlignment(JLabel.CENTER);
        ourNames.setSize(800,250);
        ourNames.setFont(new Font("Monospaced", Font.BOLD, 12));

        categories.setHorizontalAlignment(JLabel.CENTER);
        categories.setSize(800,400);
        categories.setFont(new Font("Monospaced", Font.BOLD, 15));

        courses.setBounds(200,225,100,30);
        departments.setBounds(200,250,100,30);
        faculties.setBounds(200,275,100,30);

        // add then to the panel
        p.add(welcomeLabel);
        p.add(ourNames);
        p.add(categories);

        p.add(courses);
        p.add(departments);
        p.add(faculties);

        // add them to the Jframe
        f.setContentPane(p);
        f.setLayout(null);
        f.setSize(800,800);
        f.setVisible(true);

        // handle what happens a radio button is selected
        departments.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(departments.isSelected()){
                    currentQuery[0] = 0;
                    departmentOptions();
                }
            }
        });

        faculties.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(faculties.isSelected()){
                    currentQuery[0] = 1;
                    facultyOptions();
                }
            }
        });

        courses.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(courses.isSelected()){
                    currentQuery[0] = 2;
                    courseOptions();
                }
            }
        });

    }

    // -----------------------------------------------------------------------------------------
    // departmentOptions
    //
    // PURPOSE: This page shows all the queries about the departments and asks the user to
    //          select one.
    // -----------------------------------------------------------------------------------------
    public static void departmentOptions(){
        JButton resultButton = new JButton("select");
        JButton backButton = new JButton("home");
        JPanel p = new JPanel();
        final JComboBox alloptions;
        final JLabel welcomeLabel = new JLabel("Select what you are curious about and we will show you the results!");

        String allQueries[]={"Show all the departments, their contact information and which faculty they are a part of.",
                "Show all the courses offered by each department.",
                "Show all the employees and which department they work in.",
                "Which departments do the 3 highest paid faculty members work for?",
                "Which departments do the 3 lowest paid faculty members work for?",
                "Which department has the most instructors?",
                "Which department has the least instructors?",
                "Which department paid out the most in compensation?",
                "Which department paid out the least in compensation?",
                "Which department paid the most on average?",
                "What is the total compensation awarded by X department?"};

        p.setLayout(null);

        alloptions = new JComboBox(allQueries);

        // set parameters
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setSize(800,200);
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 16));

        alloptions.setBounds(50, 200,600,30);
        resultButton.setBounds(655,200,100,30);
        backButton.setBounds(360,5,80,20);

        // add components to panels
        p.add(welcomeLabel);
        p.add(alloptions);
        p.add(resultButton);
        p.add(backButton);

        // add the panel to the jFrame
        f.setContentPane(p);
        f.setVisible(true);

        // handle what happens as the user interacts with the page

        alloptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentQuery[1] = alloptions.getSelectedIndex();
            }
        });

        resultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(currentQuery[1] == 10){
                    pickDepartment();
                }
                else {
                    displayResults();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startingPoint();
            }
        });
    }

    // -----------------------------------------------------------------------------------------
    // facultyOptions
    //
    // PURPOSE: This page shows all the queries about the faculties and asks the user to
    //          select one.
    // -----------------------------------------------------------------------------------------
    public static void facultyOptions(){
        JButton resultButton = new JButton("select");
        JButton backButton = new JButton("home");
        JPanel p = new JPanel();
        final JComboBox alloptions;
        final JLabel welcomeLabel = new JLabel("Select what you are curious about and we will show you the results!");

        String allQueries[]={"Show all the faculties and any other related information about each faculty.",
                "Show all the courses offered by each faculty.",
                "Show all the employees and which faculty they work in.",
                "Which faculty gave the most As?",
                "Which faculty gave the most Fs?",
                "Which faculty has the lowest tuition for international students  and how do I contact them?",
                "Which faculty has the lowest tuition for domestic students and how do I contact them? ",
                "Which faculty has the highest average grade for undergraduate students? How do I contact them? ",
                "Which faculty has the highest average grade for graduate students? How do I contact them?",
                "What is the total compensation awarded by X faculty?"};

        p.setLayout(null);

        alloptions = new JComboBox(allQueries);

        // set the parameters for the components
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setSize(800,200);
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 16));

        alloptions.setBounds(50, 200,600,30);
        resultButton.setBounds(655,200,100,30);
        backButton.setBounds(360,5,80,20);

        // add the components to the panel
        p.add(welcomeLabel);
        p.add(alloptions);
        p.add(resultButton);
        p.add(backButton);

        // set the panel in the JFrame
        f.setContentPane(p);
        f.setVisible(true);

        // handle user interactivity
        alloptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentQuery[1] = alloptions.getSelectedIndex();
            }
        });

        resultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(currentQuery[1]);
                // pick department if the currentQuery[1]
                if(currentQuery[1] == 9){
                    pickFaculty();
                }
                else {
                    displayResults();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                startingPoint();
            }
        });
    }

    // -----------------------------------------------------------------------------------------
    // courseOptions
    //
    // PURPOSE: This page shows all the queries about the courses and asks the user to
    //          select one.
    // -----------------------------------------------------------------------------------------
    public static void courseOptions(){
        JButton resultButton = new JButton("select");
        JButton backButton = new JButton("home");
        JPanel p = new JPanel();
        final JComboBox alloptions;
        final JLabel welcomeLabel = new JLabel("Select what you are curious about and we will show you the results!");

        String allQueries[]={"Show all the teachers and related information.",
                "Show all the available courses offered in 2019. ",
                "Show all the registration information about all of the courses offered. ",
                "Which courses are offered in all 3 terms?",
                "Which course had the most empty seats for the year?",
                "Which course from a department is most popular?",
                "Which course from a department is least popular?"};

        p.setLayout(null);

        alloptions = new JComboBox(allQueries);

        // set the parameters for the components
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setSize(800,200);
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 16));

        alloptions.setBounds(50, 200,600,30);
        resultButton.setBounds(655,200,100,30);
        backButton.setBounds(360,5,80,20);

        // add the component to the panel
        p.add(welcomeLabel);
        p.add(alloptions);
        p.add(resultButton);
        p.add(backButton);

        // set the pane in the jFrame
        f.setContentPane(p);
        f.setVisible(true);

        // handle user interactivity
        alloptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentQuery[1] = alloptions.getSelectedIndex();
            }
        });

        resultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(currentQuery[1]);

                if(currentQuery[1] == 5 || currentQuery[1] == 6){
                    pickDepartment();
                }
                else {
                    displayResults();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startingPoint();
            }
        });
    }


    // -----------------------------------------------------------------------------------------
    // pickDepartments
    //
    // PURPOSE: This page shows up when the user gets to pick which department they would like
    //          answer a certain query for. They get a drop down menu to pick a department from.
    // -----------------------------------------------------------------------------------------
    public static void pickDepartment(){
        JButton resultButton = new JButton("select");
        JButton backButton = new JButton("back");
        JPanel p = new JPanel();
        final JComboBox alloptions;
        final JLabel welcomeLabel = new JLabel("Pick the department to show the results for: ");

        String allDepartments[]={"Civil Engineering",
            "Electrical and Computer Engineering",
            "Mechanical",
            "Engineering",
            "Biological Sciences",
            "Chemistry",
            "Computer Science",
            "Mathematics",
            "Microbiology",
            "Physics and Astronomy",
            "Statistics"};

        p.setLayout(null);
        alloptions = new JComboBox(allDepartments);

        // set up parameters for the components
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setSize(800,200);
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 16));

        alloptions.setBounds(50, 200,600,30);
        resultButton.setBounds(655,200,100,30);
        backButton.setBounds(360,5,80,20);

        // add all the components to the panel
        p.add(welcomeLabel);
        p.add(alloptions);
        p.add(resultButton);
        p.add(backButton);

        // add the panel to the Jframe
        f.setContentPane(p);
        f.setVisible(true);

        // handle user interactivity
        alloptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentQuery[2] = alloptions.getSelectedIndex();
            }
        });

        resultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayResults();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentQuery[1] = 0;
                currentQuery[2] = 0;
                if(currentQuery[0] == 0){
                    departmentOptions();
                }
                else if(currentQuery[0] == 1){
                    facultyOptions();
                }
                else if(currentQuery[0] == 2){
                    courseOptions();
                }
                else {
                    startingPoint();
                }
            }
        });
    }

    // -----------------------------------------------------------------------------------------
    // pickFaculty
    //
    // PURPOSE: This page shows up when the user gets to pick which faculty they would like
    //          answer a certain query for. They get a drop down menu to pick a faculty.
    // -----------------------------------------------------------------------------------------
    public static void pickFaculty(){
        JButton resultButton = new JButton("select");
        JButton backButton = new JButton("back");
        JPanel p = new JPanel();
        final JComboBox alloptions;
        final JLabel welcomeLabel = new JLabel("Pick the faculty to show the results for: ");

        String allFaculties[]={"Engineering",
                "Science"};

        p.setLayout(null);
        alloptions = new JComboBox(allFaculties);

        // set up the parameters for the components

        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setSize(800,200);
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 16));

        alloptions.setBounds(50, 200,600,30);
        resultButton.setBounds(655,200,100,30);
        backButton.setBounds(360,5,80,20);

        // add all the components to the panel
        p.add(welcomeLabel);
        p.add(alloptions);
        p.add(resultButton);
        p.add(backButton);

        // set the panel to be the Jframe
        f.setContentPane(p);
        f.setVisible(true);

        // handle user interactivity
        alloptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentQuery[2] = alloptions.getSelectedIndex();
            }
        });

        resultButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayResults();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentQuery[1] = 0;
                currentQuery[2] = 0;
                if(currentQuery[0] == 0){
                    departmentOptions();
                }
                else if(currentQuery[0] == 1){
                    facultyOptions();
                }
                else if(currentQuery[0] == 2){
                    courseOptions();
                }
                else {
                    startingPoint();
                }
            }
        });
    }

    // -----------------------------------------------------------------------------------------
    // displayResults
    //
    // PURPOSE: This shows the results of the query in table. Also provides a way to print the
    //          results to a CSV.
    // -----------------------------------------------------------------------------------------
    public static void displayResults(){
        JButton homeButton = new JButton("home");
        JButton printButton = new JButton("Save the results to CSV file");
        JButton backButton = new JButton("back");
        final JLabel label = new JLabel("RESULTS");
        JPanel p = new JPanel();
        JScrollPane sp;
        JTable table;

        ArrayList<String[]> returnedList = db.executeQuery(currentQuery); // get the result of the current query
        String[] headers = db.getHeaders(currentQuery); // get the corresponding headers

        // create an array that is equal to the size of the retured array list
        int rowAmount = returnedList.size();
        int columnAmount = (returnedList.get(0)).length;
        String[][] data = new String[rowAmount][columnAmount];

        // to convert the array list into an array
        // because that's what JTable accepts
        for(int i = 0; i < rowAmount; i++){
            for(int j = 0; j < columnAmount; j++){
                data[i][j] = returnedList.get(i)[j];
            }
        }

        // set up parameters for the panel
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "UofM STEM Database", TitledBorder.CENTER, TitledBorder.TOP));

        // create scrollable table
        table = new JTable(data, headers);
        sp = new JScrollPane(table);

        // set up the parameters for the buttons and labels
        homeButton.setBounds(0,5,400,16);
        backButton.setBounds(400,5,400,16);

        homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        printButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setFont(new Font("Monospaced", Font.BOLD, 20));

        // add everything to the panel
        p.add(backButton);
        p.add(Box.createRigidArea(new Dimension(0,3))); // creates an invisible box (for spacing)
        p.add(homeButton);
        p.add(Box.createRigidArea(new Dimension(0,10)));
        p.add(label);
        p.add(Box.createRigidArea(new Dimension(0,5)));
        p.add(sp);
        p.add(Box.createRigidArea(new Dimension(0,5)));
        p.add(printButton);

        // setup JFrame to show panel
        f.setContentPane(p);
        f.setVisible(true);

        // handle user interactivity
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentQuery[1] = 0;
                currentQuery[2] = 0;
                if(currentQuery[0] == 0){
                    departmentOptions();
                }
                else if(currentQuery[0] == 1){
                    facultyOptions();
                }
                else if(currentQuery[0] == 2){
                    courseOptions();
                }
                else {
                    startingPoint();
                }
            }
        });

        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentQuery[0] = 0;
                currentQuery[1] = 0;
                currentQuery[2] = 0;
                startingPoint();
            }
        });

        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                db.printOutputCSV(headers, returnedList, "STEM_DB_SAVED_RESULT.csv");
            }
        });
    }
}

