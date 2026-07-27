import java.util.ArrayList;
import java.util.Scanner;

class Student{
    int studentId;
    String firstname;
    String lastname;
    int age;
    String Course;
    String email;
    int marks;

    void Println(){
        System.out.println("Student Id: " + this.studentId + "\n" +
                            "Name: " + this.firstname +  this.lastname + "\n" +
                            "Age: " + this.age + "\n"+
                            "email: " + this.email);
    }
}

public class StudentManagement {
    Scanner userInput = new Scanner(System.in);
    ArrayList<Student> Students = new ArrayList<Student>();
    Student indiStudents = new Student();
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

        int randomId = (int)(Math.random() * 101);
        for (int i = 0; i < Students.size();i++){
            if(indiStudents.studentId == randomId){
                randomId = (int) (Math.random() * 101);
            }
        }
        indiStudents.studentId = randomId;
        System.out.println("Enter Student First Name:");
        indiStudents.firstname = userInput.nextLine();
        System.out.println("Enter Student Last Name:");
        indiStudents.lastname = userInput.nextLine();
        System.out.println("Enter Student Age:");
        String age = userInput.nextLine();
        indiStudents.age = Integer.parseInt(age);
        System.out.println("Enter Student Course:");
        indiStudents.Course = userInput.nextLine();
        System.out.println("Enter Student Email:");
        indiStudents.email = userInput.nextLine();
        System.out.println("Enter Student Mark:");
        String mark = userInput.nextLine();
        indiStudents.marks = Integer.parseInt(mark);

    }

    public static void main(String[] arg){
        StudentManagement students = new StudentManagement();
        while(students.choice != 3){
            students.menu();
            switch (students.choice){
                case 1:
                    students.addStudent();
                    break;
                case 2:
                    students.indiStudents.Println();
                    break;
                case 3:
                    break;
            }
        }

    }
}
