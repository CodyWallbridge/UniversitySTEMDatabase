//backend for database for comp3380 project
//written Dec 2021

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.Types.REAL;
import static java.sql.Types.VARCHAR;

public class Database {
    private Connection connection;
    private String fileName = null;

    public Database() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            this.connection = DriverManager.getConnection("jdbc:hsqldb:mem:.", "SA", "");
            this.createTables();
            this.readInData();
        } catch (ClassNotFoundException var2) {
            var2.printStackTrace(System.out);
        } catch (SQLException var3) {
            var3.printStackTrace(System.out);
        }
    }

    public ArrayList<String[]> executeQuery(int queryNum) {
        ArrayList<String[]> result = null;
        if(queryNum == 0){
            result = selectStarTeacher();
        }
        else if(queryNum == 1){
            result = selectStarFOffers();
        }
        else if(queryNum == 2){
            result = selectStarDOffers();
        }
        else if(queryNum == 3){
            result = selectStarCourseStudentInfo();
        }
        else if(queryNum == 4){
            result = selectStarCourseOfferingInfo();
        }
        else if(queryNum == 5){
            result = selectStarDepartment();
        }
        else if(queryNum == 6){
            result = selectStarFaculty();
        }
        else if(queryNum == 7){
            result = selectStardWorksIn();
        }
        else if(queryNum == 8){
            result = selectStarfWorksIn();
        }
        else if(queryNum == 9){
            result = whichCoursesOfferedInAllTerms();
        }
        else if(queryNum == 10){
            result = whichCoursehadMostEmpty();
        }
        else if(queryNum == 11){
            result = highestPaid();
        }
        else if(queryNum == 12){
            result = lowestPaid();
        }
        else if(queryNum == 13){
            result = mostF();
        }
        else if(queryNum == 14){
            result = mostA();
        }
        return result;
    }

    private ArrayList<String[]> mostA() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select fName, uAmountOfGradesGiven*uAPercentage/100 as underGradF, gAmountOfGradesGiven*gAPercentage/100 as gradF\nfrom Faculty\norder by underGradF+gradF desc\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[3];
                row[0] = (result.getString("fName"));
                row[1] = (result.getString("underGradF"));
                row[2] = (result.getString("gradF"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> mostF() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select fName, uAmountOfGradesGiven*uFPercentage/100 as underGradF, gAmountOfGradesGiven*gFPercentage/100 as gradF\nfrom Faculty\norder by underGradF+gradF desc\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[3];
                row[0] = (result.getString("fName"));
                row[1] = (result.getString("underGradF"));
                row[2] = (result.getString("gradF"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> lowestPaid() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select tTitle, dName from Teacher natural join dWorksIn where compensationAmount >0\norder by compensationAmount\nlimit 3";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("tTitle"));
                row[1] = (result.getString("dName"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> highestPaid() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select tTitle, dName from Teacher natural join dWorksIn where compensationAmount >0\norder by compensationAmount desc\nlimit 3";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("tTitle"));
                row[1] = (result.getString("dName"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> whichCoursehadMostEmpty() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select cTitle, sum(capacity-actualRegistered) as emptySeats from CourseStudentInfo\ngroup by cTitle\norder by emptySeats, cTitle\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("cTitle"));
                row[1] = (result.getString("emptySeats"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> whichCoursesOfferedInAllTerms() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select cTitle\nfrom CourseOfferingInfo\ngroup by  cTitle\nhaving count(distinct term) > 2\norder by cTitle";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[1];
                row[0] = (result.getString("cTitle"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    //has 477, TBA still included for now
    private ArrayList<String[]> selectStarTeacher() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from Teacher;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[6];
                row[0] = (result.getString("firstName"));
                row[1] = (result.getString("lastName"));
                row[2] = (result.getString("tTitle"));
                row[3] = (result.getString("compensationAmount"));
                row[4] = (result.getString("tPhone"));
                row[5] = (result.getString("tOffice"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> selectStarFOffers() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from fOffers;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("CRN"));
                row[1] = (result.getString("fName"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> selectStarDOffers() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from dOffers;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("CRN"));
                row[1] = (result.getString("dName"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> selectStarCourseStudentInfo() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from CourseStudentInfo;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[9];
                row[0] = (result.getString("cTitle"));
                row[1] = (result.getString("ID"));
                row[2] = (result.getString("section"));
                row[3] = (result.getString("capacity"));
                row[4] = (result.getString("waitlistCapacity"));
                row[5] = (result.getString("waitlistActual"));
                row[6] = (result.getString("actualRegistered"));
                row[7] = (result.getString("creditHours"));
                row[8] = (result.getString("subject"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> selectStarCourseOfferingInfo() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from CourseOfferingInfo;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[7];
                row[0] = (result.getString("CRN"));
                row[1] = (result.getString("cTitle"));
                row[2] = (result.getString("section"));
                row[3] = (result.getString("ID"));
                row[4] = (result.getString("firstName"));
                row[5] = (result.getString("lastName"));
                row[6] = (result.getString("term"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> selectStarDepartment() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from Department;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[4];
                row[0] = (result.getString("dName"));
                row[1] = (result.getString("dPhone"));
                row[2] = (result.getString("dOffice"));
                row[3] = (result.getString("fName"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> selectStarFaculty() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from Faculty;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[14];
                row[0] = (result.getString("fName"));
                row[1] = (result.getString("fullTimeEnrolled"));
                row[2] = (result.getString("fOffice"));
                row[3] = (result.getString("fPhone"));
                row[4] = (result.getString("tuitionDomestic"));
                row[5] = (result.getString("tuitionInternational"));
                row[6] = (result.getString("uAveGrade"));
                row[7] = (result.getString("uAmountOfGradesGiven"));
                row[8] = (result.getString("uFPercentage"));
                row[9] = (result.getString("uAPercentage"));
                row[10] = (result.getString("gAveGrade"));
                row[11] = (result.getString("gAmountOfGradesGiven"));
                row[12] = (result.getString("gFPercentage"));
                row[13] = (result.getString("gAPercentage"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> selectStardWorksIn() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from dWorksIn;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[3];
                row[0] = (result.getString("firstName"));
                row[1] = (result.getString("lastName"));
                row[2] = (result.getString("dName"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> selectStarfWorksIn() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from fWorksIn;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            int counter = 0;
            while(result.next()) {
                String[] row = new String[3];
                row[0] = (result.getString("firstName"));
                row[1] = (result.getString("lastName"));
                row[2] = (result.getString("fName"));
                resultList.add(row);
                counter++;
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private void addTeacher(String first, String last) {
        try {
            PreparedStatement instruction = this.connection.prepareStatement("Select * From  Teacher where firstName = ? AND lastName = ?;");
            instruction.setString(1, first);
            instruction.setString(2, last);
            ResultSet result = instruction.executeQuery();
            if (!result.next()) {
                PreparedStatement newInstruction = this.connection.prepareStatement("insert into Teacher (firstName, lastName, tTitle, compensationAmount, tPhone, tOffice) values (?, ?, ?, ?, ?, ?);");
                newInstruction.setString(1, first);
                newInstruction.setString(2, last);
                newInstruction.setNull(3, VARCHAR);
                newInstruction.setNull(4, REAL);
                newInstruction.setNull(5, VARCHAR);
                newInstruction.setNull(6, VARCHAR);
                newInstruction.executeUpdate();
                newInstruction.close();
            }

            result.close();
            instruction.close();
        } catch (SQLException error) {
            error.printStackTrace(System.out);
        }
    }

    private void addCourseStudentInfo(String title, String section, int id){
        try {
            PreparedStatement instruction = this.connection.prepareStatement("Select * From  CourseStudentInfo where cTitle = ? AND ID = ? AND section = ?;");
            instruction.setString(1, title);
            instruction.setInt(2, id);
            instruction.setString(3, section);
            ResultSet result = instruction.executeQuery();
            if (!result.next()) {
                System.out.println("CourseStudentInfo was added outside of its table. need to handle this Cody!");
                PreparedStatement newInstruction = this.connection.prepareStatement("insert into CourseStudentInfo (cTitle, ID, section, capacity, waitlistCapacity, waitlistActual, actualRegistered, creditHours, subject) values (?, ?, ?, ?, ?, ?, ?, ?, ?);");
                newInstruction.setString(1, title);
                newInstruction.setInt(2, id);
                newInstruction.setString(3, section);
                newInstruction.executeUpdate();
                newInstruction.executeUpdate();
                newInstruction.close();
            }

            result.close();
            instruction.close();
        } catch (SQLException error) {
            error.printStackTrace(System.out);
        }
    }

    private void addCourseOfferingInfo(int crn){
        try {
            PreparedStatement instruction = this.connection.prepareStatement("Select * From  CourseOfferingInfo where CRN = ?;");
            instruction.setInt(1, crn);
            ResultSet result = instruction.executeQuery();
            if (!result.next()) {
                System.out.println("CourseOfferingInfo was added outside of its table. need to handle this Cody!");
                PreparedStatement newInstruction = this.connection.prepareStatement("insert into CourseOfferingInfo  (CRN, cTitle, section, ID, firstName, lastName, term) values (?, ?, ?, ?, ?, ?, ?);");
                newInstruction.setInt(1, crn);
                newInstruction.executeUpdate();
                newInstruction.close();
            }

            result.close();
            instruction.close();
        } catch (SQLException error) {
            error.printStackTrace(System.out);
        }
    }

    private void addFaculty(String name){
        try {
            PreparedStatement instruction = this.connection.prepareStatement("Select * From Faculty where fName = ?;");
            instruction.setString(1, name);
            ResultSet result = instruction.executeQuery();
            if (!result.next()) {
                System.out.println("Faculty was added outside of its table. need to handle this Cody!");
                PreparedStatement newInstruction = this.connection.prepareStatement("insert into Faculty (fName, fullTimeEnrolled, fOffice, fPhone, tuitionDomestic, tuitionInternational, uAveGrade, uAmountOfGradesGiven, uFPercentage, uAPercentage, gAveGrade, gAmountOfGradesGiven, gFPercentage, gAPercentage) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                newInstruction.setString(1, name);
                newInstruction.executeUpdate();
                newInstruction.close();
            }

            result.close();
            instruction.close();
        } catch (SQLException error) {
            error.printStackTrace(System.out);
        }
    }

    private void addDepartment(String name){
        try {
            PreparedStatement instruction = this.connection.prepareStatement("Select * From  Department where dName = ?;");
            instruction.setString(1, name);
            ResultSet result = instruction.executeQuery();
            if (!result.next()) {
                System.out.println("Department " + name + " was added outside of its table. need to handle this Cody!");
                PreparedStatement newInstruction = this.connection.prepareStatement("insert into Department (dName, dPhone, dOffice, fName) values (?, ?, ?, ?);");
                newInstruction.setString(1, name);
                newInstruction.executeUpdate();
                newInstruction.close();
            }

            result.close();
            instruction.close();
        } catch (SQLException error) {
            error.printStackTrace(System.out);
        }
    }

    private void createTables() {
        String ddl1 = "create table Faculty ( fName varchar(15), fullTimeEnrolled integer, fOffice varchar(75), fPhone varchar(12), tuitionDomestic integer, tuitionInternational integer, uAveGrade real, uAmountOfGradesGiven integer, uFPercentage real, uAPercentage real, gAveGrade real, gAmountOfGradesGiven integer, gFPercentage real, gAPercentage real, primary key (fName) );";

        try {
            this.connection.createStatement().executeUpdate(ddl1);

            String ddl2 = "create table Department ( dName VARCHAR(40), dPhone varchar(12),  dOffice varchar(75),  fName varchar(15), primary key (dName), foreign key (fName) references Faculty);";
            this.connection.createStatement().executeUpdate(ddl2);

            String ddl3 = "create table Teacher ( firstName varchar(25), lastName varchar(30), tTitle varchar(60), compensationAmount real, tPhone varchar(12),  tOffice varchar(75), primary key (firstName, lastName) );";
            this.connection.createStatement().executeUpdate(ddl3);

            String ddl4 = "create table CourseStudentInfo (cTitle VARCHAR(100), ID INTEGER, section VARCHAR(5), capacity INTEGER, waitlistCapacity INTEGER, waitlistActual INTEGER, actualRegistered INTEGER, creditHours INTEGER, subject VARCHAR(15), primary key (cTitle, ID, section) );";
            this.connection.createStatement().executeUpdate(ddl4);

            String ddl5 = "create table fWorksIn ( firstName varchar(15), lastName varchar(20), fName VARCHAR(15), primary key (firstName, lastName, fName), foreign key (firstName, lastName) references Teacher );";
            this.connection.createStatement().executeUpdate(ddl5);

            String ddl6 = "create table dWorksIn ( firstName varchar(25), lastName varchar(20), dName VARCHAR(40),  primary key (firstName, lastName, dName), foreign key (dName) references Department, foreign key (firstName, lastName) references Teacher);";
            this.connection.createStatement().executeUpdate(ddl6);

            String ddl7 = "create table CourseOfferingInfo (CRN INTEGER, cTitle VARCHAR(100), section VARCHAR(5), ID INTEGER, firstName VARCHAR(25), lastName  VARCHAR(30), term VARCHAR(10), primary key(CRN), foreign key (firstName, lastName) references Teacher(firstName, lastName), foreign key (cTitle, ID, section) references CourseStudentInfo(cTitle, ID, section) );";
            this.connection.createStatement().executeUpdate(ddl7);

            String ddl8 = "create table dOffers ( CRN integer, dName VARCHAR(40), primary key (CRN), foreign key (dName) references Department);";
            this.connection.createStatement().executeUpdate(ddl8);

            String ddl9 = "create table fOffers ( CRN integer, fName VARCHAR(15), primary key (CRN), foreign key (fName) references Faculty );";
            this.connection.createStatement().executeUpdate(ddl9);

        } catch (SQLException error) {
            error.printStackTrace(System.out);
        }
    }

    private void readInData() {
        FileReader fileRdr;
        BufferedReader inFile;
        try {
            //add Faculty rows
            fileName = "Faculty.csv";
            fileRdr = new FileReader(fileName);//create a file reader
            inFile = new BufferedReader(fileRdr);//wrap the file reader in a buffered reader

            for(String line = inFile.readLine(); line != null; line = inFile.readLine()) {
                String[] columnValues = line.split(",");
                PreparedStatement instruction = this.connection.prepareStatement("insert into Faculty (fName, fullTimeEnrolled, fOffice, fPhone, tuitionDomestic, tuitionInternational, uAveGrade, uAmountOfGradesGiven, uFPercentage, uAPercentage, gAveGrade, gAmountOfGradesGiven, gFPercentage, gAPercentage) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                instruction.setString(1, columnValues[0]);
                instruction.setInt(2, Integer.parseInt(columnValues[1]));
                instruction.setString(3, columnValues[2]);
                instruction.setString(4, columnValues[3]);
                instruction.setInt(5, Integer.parseInt(columnValues[4]));
                instruction.setInt(6, Integer.parseInt(columnValues[5]));
                instruction.setDouble(7, Double.parseDouble(columnValues[6]));
                instruction.setInt(8, Integer.parseInt(columnValues[7]));
                instruction.setDouble(9, Double.parseDouble(columnValues[8]));
                instruction.setDouble(10, Double.parseDouble(columnValues[9]));
                instruction.setDouble(11, Double.parseDouble(columnValues[10]));
                instruction.setInt(12, Integer.parseInt(columnValues[11]));
                instruction.setDouble(13, Double.parseDouble(columnValues[12]));
                instruction.setDouble(14, Double.parseDouble(columnValues[13]));
                instruction.executeUpdate();
                instruction.close();
            }
            inFile.close();
            fileRdr.close();

            //add Department rows
            fileName = "Department.csv";
            fileRdr = new FileReader(fileName);//create a file reader
            inFile = new BufferedReader(fileRdr);//wrap the file reader in a buffered reader

            for(String line = inFile.readLine(); line != null; line = inFile.readLine()) {
                String[] columnValues = line.split(",");
                this.addFaculty(columnValues[3]);
                PreparedStatement instruction = this.connection.prepareStatement("insert into Department (dName, dPhone, dOffice, fName) values (?, ?, ?, ?);");
                instruction.setString(1, columnValues[0]);
                instruction.setString(2, columnValues[1]);
                instruction.setString(3, columnValues[2]);
                instruction.setString(4, columnValues[3]);
                instruction.executeUpdate();
                instruction.close();
            }
            inFile.close();
            fileRdr.close();

            //add Teacher rows
            fileName = "Teacher.csv";
            fileRdr = new FileReader(fileName);//create a file reader
            inFile = new BufferedReader(fileRdr);//wrap the file reader in a buffered reader

            for(String line = inFile.readLine(); line != null; line = inFile.readLine()) {
                boolean hadComp = false;
                String[] columnValues = line.split(",");
                //System.out.println("adding " + columnValues[0] + " " + columnValues[1]);
                int last = 0;
                PreparedStatement instruction = this.connection.prepareStatement("insert into Teacher (firstName, lastName, tTitle, compensationAmount, tPhone, tOffice) values (?, ?, ?, ?, ?, ?);");
                int thing = 0;
                if(columnValues[0].equalsIgnoreCase("Olesya")){
                    thing++;
                }
                for(int i = 1;i<=columnValues.length;i++){
                    if(i == 4) {
                        if(columnValues[i-1].isEmpty() == false && Double.parseDouble(columnValues[i-1]) > 0) {
                            instruction.setDouble(i, Double.parseDouble(columnValues[i - 1]));
                            hadComp = true;
                        }
                        else{
                            instruction.setNull(i, REAL);
                        }
                    }
                    else {
                        instruction.setString(i, columnValues[i - 1]);
                    }
                    last = i;
                }

                for(int i = last; i<=6;i++){
                    if(i == 3){
                        instruction.setNull(i, VARCHAR);
                    }
                    if(i == 4 && !hadComp){
                        instruction.setNull(i, REAL);
                    }
                    if(i == 5){
                        instruction.setNull(i, VARCHAR);
                    }
                    if(i == 6){
                        instruction.setNull(i, VARCHAR);
                    }
                }
                instruction.executeUpdate();
                //int thing = instruction.executeUpdate();
                //System.out.println(thing);
                instruction.close();
            }
            inFile.close();
            fileRdr.close();

            //add courseStudentInfo rows
            fileName = "CourseStudentInfo.csv";
            fileRdr = new FileReader(fileName);//create a file reader
            inFile = new BufferedReader(fileRdr);//wrap the file reader in a buffered reader

            for(String line = inFile.readLine(); line != null; line = inFile.readLine()) {
                String[] columnValues = line.split(",");
                PreparedStatement instruction = this.connection.prepareStatement("insert into CourseStudentInfo (cTitle, ID, section, capacity, waitlistCapacity, waitlistActual, actualRegistered, creditHours, subject) values (?, ?, ?, ?, ?, ?, ?, ?, ?);");
                instruction.setString(1, columnValues[0]);
                instruction.setInt(2, Integer.parseInt(columnValues[1]));
                instruction.setString(3, columnValues[2]);
                instruction.setInt(4, Integer.parseInt(columnValues[3]));
                instruction.setInt(5, Integer.parseInt(columnValues[4]));
                instruction.setInt(6, Integer.parseInt(columnValues[5]));
                instruction.setInt(7, Integer.parseInt(columnValues[6]));
                instruction.setInt(8, Integer.parseInt(columnValues[7]));
                instruction.setString(9, columnValues[8]);
                instruction.executeUpdate();
                instruction.close();
            }
            inFile.close();
            fileRdr.close();

            //add fWorksIn rows
            fileName = "fWorksIn.csv";
            fileRdr = new FileReader(fileName);//create a file reader
            inFile = new BufferedReader(fileRdr);//wrap the file reader in a buffered reader

            for(String line = inFile.readLine(); line != null; line = inFile.readLine()) {
                String[] columnValues = line.split(",");
                this.addTeacher(columnValues[0], columnValues[1]);
                this.addFaculty(columnValues[2]);
                PreparedStatement instruction = this.connection.prepareStatement("insert into fWorksIn (firstName, lastName, fName) values (?, ?, ?);");
                instruction.setString(1, columnValues[0]);
                instruction.setString(2, columnValues[1]);
                instruction.setString(3, columnValues[2]);
                instruction.executeUpdate();
                instruction.close();
            }
            inFile.close();
            fileRdr.close();

            //add dWorksIn rows
            fileName = "dWorksIn.csv";
            fileRdr = new FileReader(fileName);//create a file reader
            inFile = new BufferedReader(fileRdr);//wrap the file reader in a buffered reader

            for(String line = inFile.readLine(); line != null; line = inFile.readLine()) {
                String[] columnValues = line.split(",");
                this.addDepartment(columnValues[2]);
                this.addTeacher(columnValues[0], columnValues[1]);
                PreparedStatement instruction = this.connection.prepareStatement("insert into dWorksIn (firstName, lastName, dName) values (?, ?, ?);");
                instruction.setString(1, columnValues[0]);
                instruction.setString(2, columnValues[1]);
                instruction.setString(3, columnValues[2]);
                instruction.executeUpdate();
                instruction.close();
            }
            inFile.close();
            fileRdr.close();

            //add courseOfferingInfo rows
            fileName = "CourseOfferingInfo.csv";
            fileRdr = new FileReader(fileName);//create a file reader
            inFile = new BufferedReader(fileRdr);//wrap the file reader in a buffered reader

            for(String line = inFile.readLine(); line != null; line = inFile.readLine()) {
                String[] columnValues = line.split(",");
               // if(!columnValues[4].equalsIgnoreCase("TBA")) {
                    this.addTeacher(columnValues[4], columnValues[5]);
               // }
                this.addCourseStudentInfo(columnValues[1], columnValues[2], Integer.parseInt(columnValues[3]));
                PreparedStatement instruction = this.connection.prepareStatement("insert into CourseOfferingInfo  (CRN, cTitle, section, ID, firstName, lastName, term) values (?, ?, ?, ?, ?, ?, ?);");
                instruction.setInt(1, Integer.parseInt(columnValues[0]));
                instruction.setString(2, columnValues[1]);
                instruction.setString(3, columnValues[2]);
                instruction.setInt(4, Integer.parseInt(columnValues[3]));
                instruction.setString(5, columnValues[4]);
                instruction.setString(6, columnValues[5]);
                instruction.setString(7, columnValues[6]);
                instruction.executeUpdate();
                instruction.close();
            }
            inFile.close();
            fileRdr.close();

            //add dOffers rows
            fileName = "dOffers.csv";
            fileRdr = new FileReader(fileName);//create a file reader
            inFile = new BufferedReader(fileRdr);//wrap the file reader in a buffered reader

            for(String line = inFile.readLine(); line != null; line = inFile.readLine()) {
                String[] columnValues = line.split(",");
                this.addDepartment(columnValues[1]);
                this.addCourseOfferingInfo(Integer.parseInt(columnValues[0]));
                PreparedStatement instruction = this.connection.prepareStatement("insert into dOffers (CRN, dName) values (?, ?);");
                instruction.setInt(1, Integer.parseInt(columnValues[0]));
                instruction.setString(2, columnValues[1]);
                instruction.executeUpdate();
                instruction.close();
            }
            inFile.close();
            fileRdr.close();

            //add fOffers rows
            fileName = "fOffers.csv";
            fileRdr = new FileReader(fileName);//create a file reader
            inFile = new BufferedReader(fileRdr);//wrap the file reader in a buffered reader

            for(String line = inFile.readLine(); line != null; line = inFile.readLine()) {
                String[] columnValues = line.split(",");
                this.addFaculty(columnValues[1]);
                this.addCourseOfferingInfo(Integer.parseInt(columnValues[0]));
                PreparedStatement instruction = this.connection.prepareStatement("insert into fOffers (CRN, fName) values (?, ?);");
                instruction.setInt(1, Integer.parseInt(columnValues[0]));
                instruction.setString(2, columnValues[1]);
                instruction.executeUpdate();
                instruction.close();
            }
            inFile.close();
            fileRdr.close();

        } catch (IOException var9) {
            var9.printStackTrace();
        } catch (SQLException var10) {
            var10.printStackTrace(System.out);
        }
    }
}