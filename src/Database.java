//backend for database for comp3380 project
//written Dec 2021

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.sql.Types.*;

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

    public void printOutputCSV(String[] headers, ArrayList<String[]> finalTable, String outputFileName){
        try{
            FileWriter myWriter = new FileWriter(outputFileName);
            for(int i = 0;i<headers.length;i++){
                if(i>0){
                    myWriter.write(",");
                }
                myWriter.write(headers[i]);
            }
            myWriter.write("\n");
            for (String[] stringArray : finalTable) {
                int count = 0;
                for (String string : stringArray) {
                    if (count > 0) {
                        myWriter.write(",");
                    }
                    if ( string == null) {
                        myWriter.write("");
                    }
                    else{
                        myWriter.write(string);
                    }
                    count++;
                }
                myWriter.write("\n");
            }
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public ArrayList<String[]> executeQuery(int[] queryNum) {
        ArrayList<String[]> result = null;
        if(queryNum[0] == 0){
            if(queryNum[1] == 0){
                result = selectStarDepartment();
            }
            else if(queryNum[1] == 1){
                result = selectStarDOffers();
            }
            else if(queryNum[1] == 2){
                result = selectStardWorksIn();
            }
            else if(queryNum[1] == 3){
                result = highestPaid();
            }
            else if(queryNum[1] == 4){
                result = lowestPaid();
            }
            else if(queryNum[1] == 5){
                result = mostProfs();
            }
            else if(queryNum[1] == 6){
                result = leastProfs();
            }
            else if(queryNum[1] == 7){
                result = mostComp();
            }
            else if(queryNum[1] == 8){
                result = leastComp();
            }
            else if(queryNum[1] == 9){
                result = mostAvg();
            }
            else if(queryNum[1] == 10){
                result = totalByDep(queryNum[2]);
            }
        }

        else if ( queryNum[0] == 1){
            if(queryNum[1] == 0){
                System.out.println("here");
                result = selectStarFaculty();
            }
            else if(queryNum[1] == 1){
                result = selectStarFOffers();
            }
            else if(queryNum[1] == 2){
                result = selectStarfWorksIn();
            }
            else if(queryNum[1] == 3){
                result = mostA();
            }
            else if(queryNum[1] == 4){
                result = mostF();
            }
            else if(queryNum[1] == 5){
                result = lowestTuitionInter();
            }
            else if(queryNum[1] == 6){
                result = lowestTuitionDom();
            }
            else if(queryNum[1] == 7){
                result = highestAvgUG();
            }
            else if(queryNum[1] == 8){
                result = highestAvgG();
            }
            else if(queryNum[1] == 9){
                result = facultyHighest(queryNum[2]);
            }
        }

        else if(queryNum[0] == 2){
            if(queryNum[1] == 0){
                result = selectStarTeacher();
            }
            else if(queryNum[1] == 1){
                result = selectStarCourseOfferingInfo();
            }
            else if(queryNum[1] == 2){
                result = selectStarCourseStudentInfo();
            }
            else if(queryNum[1] == 3){
                result = whichCoursesOfferedInAllTerms();
            }
            else if(queryNum[1] == 4){
                result = whichCoursehadMostEmpty();
            }
            else if(queryNum[1] == 5){
                result = courseMostPopular(queryNum[2]);
            }
            else if(queryNum[1] == 6){
                result = courseLeastPopular(queryNum[2]);
            }
        }

        return result;
    }

    public String[] getHeaders(int[] headerNum) {
        String[] result = null;
        if (headerNum[0] == 0) {
            if (headerNum[1] == 0) {
                result = new String[]{"Department", "Phone Number", "Office", "Part of Faculty"};
            } else if (headerNum[1] == 1) {
                result = new String[]{"CRN", "Department"};
            } else if (headerNum[1] == 2) {
                result = new String[]{"First Name", "Last Name", "Department"};
            } else if (headerNum[1] == 3) {
                result = new String[]{"Title", "Department"};
            } else if (headerNum[1] == 4) {
                result = new String[]{"Title", "Department"};
            } else if (headerNum[1] == 5) {
                result = new String[]{"Department", "Number Of Instructors"};
            } else if (headerNum[1] == 6) {
                result = new String[]{"Department", "Number Of Instructors"};
            } else if (headerNum[1] == 7) {
                result = new String[]{"Department", "Total Paid (CDN $)"};
            } else if (headerNum[1] == 8) {
                result = new String[]{"Department", "Total Paid (CDN $)"};
            } else if (headerNum[1] == 9) {
                result = new String[]{"Department", "Average Pay (CDN $)"};
            } else if (headerNum[1] == 10) {
                result = new String[]{"Department", "Total Paid (CDN $)"};
            }
        } else if (headerNum[0] == 1) {
            if (headerNum[1] == 0) {
                result = new String[]{"Faculty", "Number of Full-time Students Enrolled", "Office", "Phone Number", "Domestic Student Tuition (CDN $)", "International Student Tuition (CDN $)", "Average Grade for Undergrad Students", "Number of Grades Given to Undergrad Students", "Percentage of F's for Undergrad Students", "Percentage of A's for Undergrad Students", "Average Grade for Graduate Students", "Number of Grades Given to Graduate Students", "Percentage of F's for Graduate Students", "Percentage of A's for Graduate Students"};
            } else if (headerNum[1] == 1) {
                result = new String[]{"CRN", "Faculty"};
            } else if (headerNum[1] == 2) {
                result = new String[]{"First Name", "Last Name", "Faculty"};
            } else if (headerNum[1] == 3) {
                result = new String[]{"Faculty", "Number of F's for Undergrad Students", "Number of F's for Graduate Students"};
            } else if (headerNum[1] == 4) {
                result = new String[]{"Faculty", "Number of F's for Undergrad Students", "Number of F's for Graduate Students"};
            } else if (headerNum[1] == 5) {
                result = new String[]{"Faculty", "Office", "Phone Number", "International Student Tuition (CDN $)"};
            } else if (headerNum[1] == 6) {
                result = new String[]{"Faculty", "Office", "Phone Number", "Domestic Student Tuition (CDN $)"};
            } else if (headerNum[1] == 7) {
                result = new String[]{"Faculty", "Office", "Phone Number", "Average Grade for Undergrad Students"};
            } else if (headerNum[1] == 8) {
                result = new String[]{"Faculty", "Office", "Phone Number", "Average Grade for Graduate Students"};
            } else if (headerNum[1] == 9) {
                result = new String[]{"Faculty", "Total Paid (CDN $)"};
            }
        } else if (headerNum[0] == 2) {
            if (headerNum[1] == 0) {
                result = new String[]{"First Name", "Last Name", "Course Name", "Compensation Amount (CDN $)", "Phone Number", "Office"};
            } else if (headerNum[1] == 1) {
                result = new String[]{"CRN", "Course Name", "section", "ID", "First Name", "Last Name", "Term of Offering"};
            } else if (headerNum[1] == 2) {
                result = new String[]{"Course Name", "ID", "section", "capacity", "Waitlist Capacity", "Waitlist Actual", "Number of Students Actually Registered", "Credit Hours", "Subject Code"};
            } else if (headerNum[1] == 3) {
                result = new String[]{"Course Name"};
            } else if (headerNum[1] == 4) {
                result = new String[]{"Course Name", "Number of Empty Seats"};
            } else if (headerNum[1] == 5) {
                result = new String[]{"Course Name", "Number of Students Registered"};
            } else if (headerNum[1] == 6) {
                result = new String[]{"Course Name", "Number of Students Registered"};
            }
        }
        return result;
    }

    private ArrayList<String[]> facultyHighest(int fac) {
        ArrayList<String[]> resultList = null;
        String faculty;
        if(fac == 0){
            faculty = "Engineering";
        }
        else{
            faculty = "Science";
        }
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select fName, sum(compensationAmount) as TotalPaid from Teacher natural join dWorksIn natural join Department where fName = '"+ faculty +"' group by fName";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("fName"));

                double d = result.getDouble("TotalPaid");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[1] = formattedDouble;
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> highestAvgG() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select fName,fOffice, fPhone, gAveGrade from Faculty\norder by gAveGrade desc\nlimit 1";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[4];
                row[0] = (result.getString("fName"));
                row[1] = (result.getString("fOffice"));
                row[2] = (result.getString("fPhone"));

                double d = result.getDouble("gAveGrade");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[3] = formattedDouble;
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> highestAvgUG() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select fName,fOffice, fPhone, uAveGrade from Faculty\norder by uAveGrade desc\nlimit 1";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[4];
                row[0] = (result.getString("fName"));
                row[1] = (result.getString("fOffice"));
                row[2] = (result.getString("fPhone"));

                double d = result.getDouble("uAveGrade");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[3] = formattedDouble;
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> lowestTuitionDom() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select fName,fOffice, fPhone, tuitionDomestic from Faculty\norder by tuitionDomestic\nlimit 1";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[4];
                row[0] = (result.getString("fName"));
                row[1] = (result.getString("fOffice"));
                row[2] = (result.getString("fPhone"));
                row[3] = (result.getString("tuitionDomestic"));
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> lowestTuitionInter() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select fName,fOffice, fPhone, tuitionInternational from Faculty\norder by tuitionInternational\nlimit 1";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[4];
                row[0] = (result.getString("fName"));
                row[1] = (result.getString("fOffice"));
                row[2] = (result.getString("fPhone"));
                row[3] = (result.getString("tuitionInternational"));
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> totalByDep(int dep) {
        ArrayList<String[]> resultList = null;
        String department;
        if(dep == 0){
            department = "Civil Engineering";
        }
        else if(dep == 1){
            department = "Electrical and Computer Engineering";
        }
        else if(dep == 2){
            department = "Mechanical";
        }
        else if(dep == 3){
            department = "Engineering";
        }
        else if(dep == 4){
            department = "Biological Sciences";
        }
        else if(dep == 5){
            department = "Chemistry";
        }
        else if(dep == 6){
            department = "Computer Science";
        }
        else if(dep == 7){
            department = "Mathematics";
        }
        else if(dep == 8){
            department = "Microbiology";
        }
        else if(dep == 9){
            department = "Physics and Astronomy";
        }
        else{
            department = "Statistics";
        }
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select dName, sum(compensationAmount) as totalPaid from Teacher natural join dWorksIn where dName = '"+ department +"'\ngroup by dName\norder by totalPaid desc\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("dName"));

                double d = result.getDouble("totalPaid");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[1] = formattedDouble;
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> mostAvg() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select dName, sum(compensationAmount)/count(compensationAmount) as payPerPerson from Teacher natural join dWorksIn\ngroup by dName\norder by payPerPerson desc\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("dName"));

                double d = result.getDouble("payPerPerson");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[1] = formattedDouble;
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> leastComp() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select dName, sum(compensationAmount) as totalPaid from Teacher natural join dWorksIn\ngroup by dName\norder by totalPaid\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("dName"));

                double d = result.getDouble("totalPaid");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[1] = formattedDouble;
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> mostComp() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select dName, sum(compensationAmount) as totalPaid from Teacher natural join dWorksIn\ngroup by dName\norder by totalPaid desc\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("dName"));

                double d = result.getDouble("totalPaid");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[1] = formattedDouble;
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> courseLeastPopular(int dep) {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String department;
            if(dep == 0){
                department = "Civil Engineering";
            }
            else if(dep == 1){
                department = "Electrical and Computer Engineering";
            }
            else if(dep == 2){
                department = "Mechanical";
            }
            else if(dep == 3){
                department = "Engineering";
            }
            else if(dep == 4){
                department = "Biological Sciences";
            }
            else if(dep == 5){
                department = "Chemistry";
            }
            else if(dep == 6){
                department = "Computer Science";
            }
            else if(dep == 7){
                department = "Mathematics";
            }
            else if(dep == 8){
                department = "Microbiology";
            }
            else if(dep == 9){
                department = "Physics and Astronomy";
            }
            else{
                department = "Statistics";
            }
            String instruction = "select cTitle,sum(actualRegistered) as totalRegistered from CourseStudentInfo natural join CourseOfferingInfo natural join dOffers d\nwhere dName = '" + department +"'\ngroup by cTitle\norder by totalRegistered\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("cTitle"));
                row[1] = (result.getString("totalRegistered"));
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> courseMostPopular(int dep) {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String department;
            if(dep == 0){
                department = "Civil Engineering";
            }
            else if(dep == 1){
                department = "Electrical and Computer Engineering";
            }
            else if(dep == 2){
                department = "Mechanical";
            }
            else if(dep == 3){
                department = "Engineering";
            }
            else if(dep == 4){
                department = "Biological Sciences";
            }
            else if(dep == 5){
                department = "Chemistry";
            }
            else if(dep == 6){
                department = "Computer Science";
            }
            else if(dep == 7){
                department = "Mathematics";
            }
            else if(dep == 8){
                department = "Microbiology";
            }
            else if(dep == 9){
                department = "Physics and Astronomy";
            }
            else{
                department = "Statistics";
            }
            String instruction = "select cTitle,sum(actualRegistered) as totalRegistered from CourseStudentInfo natural join CourseOfferingInfo natural join dOffers d\nwhere dName = '" + department +"'\ngroup by cTitle\norder by totalRegistered desc\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("cTitle"));
                row[1] = (result.getString("totalRegistered"));
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> leastProfs() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select dName, count(distinct firstName||lastName) as NumberOfInstructors from dWorksIn\ngroup by dName\norder by count(distinct firstName||lastName) \nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("dName"));
                row[1] = (result.getString("NumberOfInstructors"));
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> mostProfs() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select dName, count(distinct firstName||lastName) as NumberOfInstructors from dWorksIn\ngroup by dName\norder by count(distinct firstName||lastName) desc\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("dName"));
                row[1] = (result.getString("NumberOfInstructors"));
                resultList.add(row);
            }
            result.close();
            statemnt.close();
        } catch (SQLException var4) {
            var4.printStackTrace(System.out);
        }
        return resultList;
    }

    private ArrayList<String[]> mostA() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "select fName, uAmountOfGradesGiven*uAPercentage/100 as underGradF, gAmountOfGradesGiven*gAPercentage/100 as gradF\nfrom Faculty\norder by underGradF+gradF desc\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[3];
                row[0] = (result.getString("fName"));

                double d = result.getDouble("underGradF");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[1] = formattedDouble;

                d = result.getDouble("gradF");
                formattedDouble = decimalFormat.format(d);
                row[2] = formattedDouble;
                resultList.add(row);
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

            while(result.next()) {
                String[] row = new String[3];
                row[0] = (result.getString("fName"));

                double d = result.getDouble("underGradF");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[1] = formattedDouble;

                d = result.getDouble("gradF");
                formattedDouble = decimalFormat.format(d);
                row[2] = formattedDouble;
                resultList.add(row);
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

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("tTitle"));
                row[1] = (result.getString("dName"));
                resultList.add(row);
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

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("tTitle"));
                row[1] = (result.getString("dName"));
                resultList.add(row);
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
            String instruction = "select cTitle, sum(capacity-actualRegistered) as emptySeats from CourseStudentInfo\ngroup by cTitle\norder by emptySeats desc, cTitle\nlimit 1\n";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("cTitle"));
                row[1] = (result.getString("emptySeats"));
                resultList.add(row);
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

    private ArrayList<String[]> selectStarTeacher() {
        ArrayList<String[]> resultList = null;
        try {
            resultList = new ArrayList<String[]>();
            String instruction = "Select * from Teacher;";
            Statement statemnt = this.connection.createStatement();
            ResultSet result = statemnt.executeQuery(instruction);

            while(result.next()) {
                String[] row = new String[6];
                row[0] = (result.getString("firstName"));
                row[1] = (result.getString("lastName"));
                row[2] = (result.getString("tTitle"));

                double d = result.getDouble("compensationAmount");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[3] = formattedDouble;
                row[4] = (result.getString("tPhone"));

                row[5] = (result.getString("tOffice"));
                resultList.add(row);
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

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("CRN"));
                row[1] = (result.getString("fName"));
                resultList.add(row);
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

            while(result.next()) {
                String[] row = new String[2];
                row[0] = (result.getString("CRN"));
                row[1] = (result.getString("dName"));
                resultList.add(row);
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

            while(result.next()) {
                String[] row = new String[4];
                row[0] = (result.getString("dName"));
                row[1] = (result.getString("dPhone"));
                row[2] = (result.getString("dOffice"));
                row[3] = (result.getString("fName"));
                resultList.add(row);
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

            while(result.next()) {
                String[] row = new String[14];
                row[0] = (result.getString("fName"));
                row[1] = (result.getString("fullTimeEnrolled"));
                row[2] = (result.getString("fOffice"));
                row[3] = (result.getString("fPhone"));
                row[4] = (result.getString("tuitionDomestic"));
                row[5] = (result.getString("tuitionInternational"));

                double d = result.getDouble("uAveGrade");
                String pattern = "#.##";
                DecimalFormat decimalFormat =  new DecimalFormat(pattern);
                String formattedDouble = decimalFormat.format(d);
                row[6] = formattedDouble;

                row[7] = (result.getString("uAmountOfGradesGiven"));

                d = result.getDouble("uFPercentage");
                formattedDouble = decimalFormat.format(d);
                row[8] = formattedDouble;

                d = result.getDouble("uAPercentage");
                formattedDouble = decimalFormat.format(d);
                row[9] = formattedDouble;

                d = result.getDouble("gAveGrade");
                formattedDouble = decimalFormat.format(d);
                row[10] = formattedDouble;
                row[11] = (result.getString("gAmountOfGradesGiven"));

                d = result.getDouble("gFPercentage");
                formattedDouble = decimalFormat.format(d);
                row[12] = formattedDouble;

                d = result.getDouble("gAPercentage");
                formattedDouble = decimalFormat.format(d);
                row[13] = formattedDouble;
                resultList.add(row);
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

            while(result.next()) {
                String[] row = new String[3];
                row[0] = (result.getString("firstName"));
                row[1] = (result.getString("lastName"));
                row[2] = (result.getString("dName"));
                resultList.add(row);
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

            while(result.next()) {
                String[] row = new String[3];
                row[0] = (result.getString("firstName"));
                row[1] = (result.getString("lastName"));
                row[2] = (result.getString("fName"));
                resultList.add(row);
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
                boolean hadComp = false, hadOffice = false, hadPhone = false;
                String[] columnValues = line.split(",");
                int last = 0;
                PreparedStatement instruction = this.connection.prepareStatement("insert into Teacher (firstName, lastName, tTitle, compensationAmount, tPhone, tOffice) values (?, ?, ?, ?, ?, ?);");
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
                    else if(i == 5 && columnValues[i-1].isEmpty() == false){
                        hadPhone = true;
                        instruction.setString(i, columnValues[i - 1]);
                    }
                    else if(i == 6 && columnValues[i-1].isEmpty() == false){
                        hadOffice = true;
                        instruction.setString(i, columnValues[i - 1]);
                    }
                    else {
                        instruction.setString(i, columnValues[i - 1]);
                    }
                    last = i;
                }

                for(int i = last+1; i<=6;i++){
                    if(i == 3){
                        instruction.setNull(i, VARCHAR);
                    }
                    if(i == 4 && !hadComp){
                        instruction.setNull(i, REAL);
                    }
                    if(i == 5 && !hadPhone){
                        instruction.setNull(i, VARCHAR);
                    }
                    if(i == 6 && !hadOffice){
                        instruction.setNull(i, VARCHAR);
                    }
                }
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