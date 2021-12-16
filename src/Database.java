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

public class Database {
    private Connection connection;
    private String fileName = null;

    public Database() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            this.connection = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
            this.createTables();
            this.readInData();
        } catch (ClassNotFoundException var2) {
            var2.printStackTrace(System.out);
        } catch (SQLException var3) {
            var3.printStackTrace(System.out);
        }
    }

    public ArrayList<String> executeQuery(int queryNum) {
        ArrayList<String> result = null;
        return result;
    }

    private void addTeacher(String first, String last) {
        try {
            PreparedStatement instruction = this.connection.prepareStatement("Select * From  Teacher where firstName = ? AND lastName = ?;");
            instruction.setString(1, first);
            instruction.setString(2, last);
            ResultSet result = instruction.executeQuery();
            if (!result.next()) {
                System.out.println("teacher was added outside of teacher table. need to handle this Cody!");
                PreparedStatement newInstruction = this.connection.prepareStatement("insert into Teacher (firstName, lastName, tTitle, compensationAmount, tPhone, tOffice) values (?, ?, ?, ?, ?, ?);");
                newInstruction.setString(1, first);
                newInstruction.setString(2, last);
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
                System.out.println("Department was added outside of its table. need to handle this Cody!");
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
        String ddl1 = "create table CourseOfferingInfo (CRN integer, cTitle varchar(80), section varchar(5), ID INTEGER, firstName varchar(15), lastName  varchar(20), term varchar(10)" +
                "primary key(CRN)" +
                "foreign key (firstName, lastName) references Teacher, " +
                "foreign key (cTitle, ID, section) references CourseStudentInfo );";

        try {
            this.connection.createStatement().executeUpdate(ddl1);

            String ddl2 = "create table CourseStudentInfo ( cTitle varchar(80), ID INTEGER, section varchar(5), capacity INTEGER, waitlistCapacity INTEGER, waitlistActual INTEGER, actualRegistered INTEGER, creditHours integer, subject varchar(15)" +
                    "primary key(cTitle, ID, section) );";
            this.connection.createStatement().executeUpdate(ddl2);

            String ddl3 = "create table Department ( dName VARCHAR(40), dPhone varchar(12),  dOffice varchar(75),  fName varchar(15), primary key (dName), foreign key (fName) references Faculty);";
            this.connection.createStatement().executeUpdate(ddl3);

            String ddl4 = "create table dOffers ( CRN integer, dName VARCHAR(40), primary key (CRN), foreign key (dName) references Department);";
            this.connection.createStatement().executeUpdate(ddl4);

            String ddl5 = "create table dWorksIn ( firstName varchar(15), lastName varchar(20), dName VARCHAR(40),  primary key (firstName, lastName, dName), foreign key (dName) references Department, foreign key (firstName, lastName) references Teacher);";
            this.connection.createStatement().executeUpdate(ddl5);

            String ddl6 = "create table Faculty ( fName varchar(15), fullTimeEnrolled integer, fOffice varchar(75), fPhone varchar(12), tuitionDomestic integer, tuitionInternational integer, uAveGrade real, uAmountOfGradesGiven integer, uFPercentage real, uAPercentage real, gAveGrade real, gAmountOfGradesGiven integer, gFPercentage real, gAPercentage real, primary key (fName) );";
            this.connection.createStatement().executeUpdate(ddl6);

            String ddl7 = "create table fOffers ( CRN integer, fName VARCHAR(15), primary key (CRN), foreign key (fName) references Faculty );";
            this.connection.createStatement().executeUpdate(ddl7);

            String ddl8 = "create table fWorksIn ( firstName varchar(15), lastName varchar(20), fName VARCHAR(15), primary key (firstName, lastName, fName), foreign key (firstName, lastName) references Teacher );";
            this.connection.createStatement().executeUpdate(ddl8);

            String ddl9 = "create table dOffers ( firstName varchar(15), lastName varchar(20), tTitle varchar(15), compensationAmount real, tPhone varchar(12),  tOffice varchar(75), primary key (firstName, lastName) );";
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
                this.addDepartment(columnValues[2]);
                this.addTeacher(columnValues[0], columnValues[1]);
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
                String[] columnValues = line.split(",");
                this.addTeacher(columnValues[0], columnValues[1]);
                this.addFaculty(columnValues[2]);
                PreparedStatement instruction = this.connection.prepareStatement("insert into Teacher (firstName, lastName, tTitle, compensationAmount, tPhone, tOffice) values (?, ?, ?, ?, ?, ?);");
                instruction.setString(1, columnValues[0]);
                instruction.setString(2, columnValues[1]);
                instruction.setString(3, columnValues[2]);
                instruction.setDouble(4, Double.parseDouble(columnValues[3]));
                instruction.setString(5, columnValues[4]);
                instruction.setString(6, columnValues[5]);
                instruction.executeUpdate();
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
                instruction.setInt(3, Integer.parseInt(columnValues[1]));
                instruction.setString(2, columnValues[2]);
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
                this.addTeacher(columnValues[4], columnValues[5]);
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
                PreparedStatement instruction = this.connection.prepareStatement("insert into fOffers (CRN, dName) values (?, ?);");
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