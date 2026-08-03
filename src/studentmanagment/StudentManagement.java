package studentmanagment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class Subject{
    public String subjectName;
    private int marks;

    public Subject(String subjectName, int marks) {
        this.subjectName = subjectName;
        this.marks = marks;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public void printMarks(){
        System.out.println(this.subjectName);
        System.out.println(this.marks);
    }


}

class Student{
    private int studentId;
    private String firstname;
    private String lastname;
    private int age;
    private String course;
    private String email;
    ArrayList<Subject> subjects = new ArrayList<Subject>();

    public Student(int studentId, String firstname, String lastname, int age, String course, String email) {
        this.studentId = studentId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.course = course;
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public  void addMark(String sbjName, int mark){
        Subject subject = new Subject(sbjName,mark);
        subjects.add(subject);
    }

    public void printSubjects(){
        for(Subject i : subjects){
            System.out.println(i.subjectName + ": " + i.getMarks());
        }
    }

    public float printAverage(){
        float average = 0.0f;
        for(Subject i : subjects){
            average += i.getMarks();
        }
        return average/subjects.size();
    }




    void printInfo(){
        System.out.println("1. studentmanagment.Student Id: " + this.studentId + "\n" +
                            "2. Firstname: " + this.firstname +   "\n" +
                            "3. Lastname: "  +  this.lastname + "\n" +
                            "4. Age: " + this.age + "\n"+
                            "5. email: " + this.email + "\n"+
                            "6. Course: " + this.course + "\n" +
                            "\n");
    }


}

public class StudentManagement {
    Scanner scannerObj = new Scanner(System.in);
    ArrayList<Student> Students = new ArrayList<Student>();

    int choice;

    public void menu(){
        System.out.println("=================================\n" +
                "     STUDENT MANAGEMENT SYSTEM\n" +
                "=================================\n" +
                "\n" +
                "1. Add studentmanagment.Student\n" +
                "2. View All Students\n" +
                "3. Search studentmanagment.Student\n" +
                "4. Update studentmanagment.Student\n" +
                "5. Delete studentmanagment.Student\n" +
                "6. Add Marks\n" +
                "7. View studentmanagment.Student Report\n" +
                "8. Short Students\n" +
                "9. Exit");

        System.out.println("Enter choice:");
        String selection = scannerObj.nextLine();
        choice = Integer.parseInt(selection);
    }

    public void addStudent(){
        boolean idExists = true;
        int randomId = 0;
        while (idExists){
            randomId = (int)(Math.random() * 101);
            idExists = false;
            for (Student i : Students){
                if (i.getStudentId() == randomId){
                    idExists = true;
                }
            }
        }
        int id = randomId;
        System.out.println("Enter studentmanagment.Student First Name:");
        String firstname = scannerObj.nextLine();
        System.out.println("Enter studentmanagment.Student Last Name:");
        String lastname = scannerObj.nextLine();
        System.out.println("Enter studentmanagment.Student Age:");
        String entersAge = scannerObj.nextLine();
        int age = Integer.parseInt(entersAge); // add try catch
        System.out.println("Enter studentmanagment.Student Course:");
        String course = scannerObj.nextLine();
        System.out.println("Enter studentmanagment.Student Email:");
        String email = scannerObj.nextLine();
        Student studentObj = new Student(id, firstname, lastname, age,course,email);
        Students.add(studentObj);
    }
    public void viewStudents(){
        for (Student i : Students){
            i.printInfo();
        }
    }

    void searchStudent(int id){
        boolean found = false;
        for (Student i : Students){
            if(i.getStudentId() == id){
                i.printInfo();
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Incorrect ID");
        }
    }

    void updateStudentInfo(int id){
        boolean found = false;
        for (Student i : Students){
            if(i.getStudentId() == id) {
                found = true;
                i.printInfo();
                System.out.println("Enter the number of the line u want to change: ");
                String userWrite = scannerObj.nextLine();
                int userSelect = Integer.parseInt(userWrite);

                switch (userSelect){
                    case 1:
                        System.out.println("Enter studentmanagment.Student's new Name: ");
                        i.setFirstname(scannerObj.nextLine());
                        break;
                    case 2:
                        System.out.println("Enter studentmanagment.Student's new Surname: ");
                        i.setLastname(scannerObj.nextLine());
                        break;
                    case 3:
                        System.out.println("Enter studentmanagment.Student's new Age: ");
                        String entersAge = scannerObj.nextLine();
                        i.setAge(Integer.parseInt(entersAge));
                        break;
                    case 4:
                        System.out.println("Enter studentmanagment.Student's new course: ");
                        i.setCourse(scannerObj.nextLine());
                        break;
                    case 5:
                        System.out.println("Enter studentmanagment.Student's new Email: ");
                        i.setEmail(scannerObj.nextLine());
                        break;
                    default:
                        System.out.println("Select a number given above");
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Incorrect ID");
        }
    }

    public void deleteStudent(int id){
        boolean found = false;
        for (Student i : Students){
            if (i.getStudentId() == id){
                Students.remove(i);
                found = true;
                System.out.println("studentmanagment.Student deleted successfully.");
                break;
            }
        }

        if (!found) {
            System.out.println("Incorrect ID");
        }
    }

    public void addMarks(int id){
        System.out.println("Enter studentmanagment.Subject's Name: ");
        String subject = scannerObj.nextLine();
        System.out.println("Enter studentmanagment.Subject's mark: ");
        String markInput = scannerObj.nextLine();
        int mark = Integer.parseInt(markInput);
        boolean found = false;
        for (Student i : Students){
            if (i.getStudentId() == id){
                i.addMark(subject, mark);
                i.printSubjects();
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Incorrect ID");
        }
    }

    public void viewStudentReport(int id){
        boolean found = false;
        for (Student i : Students){
            if (i.getStudentId() == id){
                System.out.println("=================================\n" +
                        "     studentmanagment.Student Report\n" +
                        "=================================\n" +
                        "\n" +
                        "Name: \n" + i.getFirstname() + " "+ i.getLastname() + "\n" + "\n" +
                        "Course \n" + i.getCourse() + "\n");
                i.printSubjects();
                System.out.println("\n" + "Average: " + "\n" + i.printAverage() + "\n");
                if (i.printAverage() >= 70.0){
                    System.out.println("Grade: " + "\n" + "A");
                }else if(i.printAverage() < 70.0 && i.printAverage() >= 50.0){
                    System.out.println("Grade: " + "\n" + "B");
                }else {
                    System.out.println("Grade: " + "\n" + "C");
                }
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Incorrect ID");
        }


    }

    public void shortStudents(){
        System.out.println("How do u wanna short students?"  + "\n" +
                "1. By their name?"  + "\n" +
                "2. By their Average mark?" + "\n" +
                "3. studentmanagment.Student ID?");
        String userInput = scannerObj.nextLine();
        int input = Integer.parseInt(userInput);
        switch (input) {
            case 1:
                Students.sort(Comparator.comparing(Student::getFirstname));
                break;
            case 2:
                Students.sort(Comparator.comparingDouble(Student::printAverage));
                break;
            case 3:
                Students.sort(Comparator.comparingInt(Student::getStudentId));
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }

        viewStudents();
    }


    public int userInput(){
        System.out.println("Enter studentmanagment.Student's Id: ");
        String input = scannerObj.nextLine();
        return Integer.parseInt(input);
    }


    public static void main(String[] arg){
        StudentManagement students = new StudentManagement();
        while(students.choice != 8){
            students.menu();
            switch (students.choice){
                case 1:
                    students.addStudent();
                    break;
                case 2:
                    students.viewStudents();
                    break;
                case 3:
                    students.searchStudent(students.userInput());
                    break;
                case 4:
                    students.updateStudentInfo(students.userInput());
                    break;
                case 5:
                    students.deleteStudent(students.userInput());
                    break;
                case 6:
                    students.addMarks(students.userInput());
                    break;
                case 7:
                    students.viewStudentReport(students.userInput());
                    break;
                case 8:
                    students.shortStudents();
                    break;
                case 9:
                    break;
            }
        }

    }
}
