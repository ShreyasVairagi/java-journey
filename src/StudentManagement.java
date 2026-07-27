import java.util.ArrayList;
import java.util.Scanner;

class Student{
    int studentId;
    String firstname;
    String lastname;
    int age;
    String course;
    String email;
    int marks;

    void printInfo(){
        System.out.println("Student Id: " + this.studentId + "\n" +
                            "Name: " + this.firstname +  this.lastname + "\n" +
                            "Age: " + this.age + "\n"+
                            "email: " + this.email + "\n"+
                            "Course: " + this.course + "\n" + "\n");
    }

}

public class StudentManagement {
    Scanner userInput = new Scanner(System.in);
    ArrayList<Student> Students = new ArrayList<Student>();

    int choice;

    public void menu(){
        System.out.println("=================================\n" +
                "     STUDENT MANAGEMENT SYSTEM\n" +
                "=================================\n" +
                "\n" +
                "1. Add Student\n" +
                "2. View All Students\n" +
                "3. Search Student\n" +
                "4. Update Student\n" +
                "5. Delete Student\n" +
                "6. Add Marks\n" +
                "7. View Student Report\n" +
                "8. Class Statistics\n" +
                "9. Exit");

        System.out.println("Enter choice:");
        String selection = userInput.nextLine();
        choice = Integer.parseInt(selection);
    }

    public void addStudent(){
        Student indiStudents = new Student();
        int randomId = (int)(Math.random() * 101);
        for (int i = 0; i <= Students.size();i++){
            if(indiStudents.studentId == randomId){
                randomId = (int) (Math.random() * 101);
            }
        } // needs solving
        indiStudents.studentId = randomId;
        System.out.println("Enter Student First Name:");
        indiStudents.firstname = userInput.nextLine();
        System.out.println("Enter Student Last Name:");
        indiStudents.lastname = userInput.nextLine();
        System.out.println("Enter Student Age:");
        String age = userInput.nextLine();
        indiStudents.age = Integer.parseInt(age);
        System.out.println("Enter Student Course:");
        indiStudents.course = userInput.nextLine();
        System.out.println("Enter Student Email:");
        indiStudents.email = userInput.nextLine();
        System.out.println("Enter Student Mark:");
        String mark = userInput.nextLine();
        indiStudents.marks = Integer.parseInt(mark);

        Students.add(indiStudents);
    }
    public void viewStudents(){
        for (int i = 0; i < Students.size(); i++){
            Student s = Students.get(i);
            s.printInfo();
        }
    }

    void searchStudent(String name){
        for (int i = 0;i < Students.size(); i++){
            if (Students.get(i).firstname.equals(name)){
                Student s = Students.get(i);
                s.printInfo();
            }
        }
    }

    public static void main(String[] arg){
        StudentManagement students = new StudentManagement();
        while(students.choice != 4){
            students.menu();
            switch (students.choice){
                case 1:
                    students.addStudent();
                    break;
                case 2:
                    students.viewStudents();
                    break;
                case 3:
                    String name = students.userInput.nextLine();
                    students.searchStudent(name);
                    break;
                case 4:
                    break;
            }
        }

    }
}
