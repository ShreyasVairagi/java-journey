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
    Scanner scannerObj = new Scanner(System.in);
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
        String selection = scannerObj.nextLine();
        choice = Integer.parseInt(selection);
    }

    public void addStudent(){
        Student studentObj = new Student();

        boolean idExists = true;
        int randomId = 0;
        while (idExists){
            randomId = (int)(Math.random() * 101);
            idExists = false;
            for (Student i : Students){
                if (i.studentId == randomId){
                    idExists = true;
                }
            }
        }
        studentObj.studentId = randomId;
        System.out.println("Enter Student First Name:");
        studentObj.firstname = scannerObj.nextLine();
        System.out.println("Enter Student Last Name:");
        studentObj.lastname = scannerObj.nextLine();
        System.out.println("Enter Student Age:");
        String age = scannerObj.nextLine();
        studentObj.age = Integer.parseInt(age); // add try catch
        System.out.println("Enter Student Course:");
        studentObj.course = scannerObj.nextLine();
        System.out.println("Enter Student Email:");
        studentObj.email = scannerObj.nextLine();
        System.out.println("Enter Student Mark:");
        String mark = scannerObj.nextLine();
        studentObj.marks = Integer.parseInt(mark);

        Students.add(studentObj);
    }
    public void viewStudents(){
        for (Student i : Students){
            i.printInfo();
            break;
        }
    }

    void searchStudent(int id){
        for (Student i : Students){
            if(i.studentId == id){
                i.printInfo();
                break;
            }else {
                System.out.println("Incorrect ID");
                menu();
            }
        }
    }

    void updateStudentInfo(int id){
        for (Student i : Students){
            if(i.studentId == id) {
                i.printInfo();
                String userWrite = scannerObj.nextLine();
                int userSelect = Integer.parseInt(userWrite);

                switch (userSelect){
                    case 1:
                        System.out.println("Enter Student's new Name: ");
                        i.firstname = scannerObj.nextLine();
                        break;
                    case 2:
                        System.out.println("Enter Student's new Surname: ");
                        i.lastname = scannerObj.nextLine();
                        break;
                    case 3:
                        System.out.println("Enter Student's new Age: ");
                        String entersAge = scannerObj.nextLine();
                        i.age = Integer.parseInt(entersAge);
                        break;
                    case 4:
                        System.out.println("Enter Student's new course: ");
                        i.course = scannerObj.nextLine();
                        break;
                    case 5:
                        System.out.println("Enter Student's new Email: ");
                        i.email = scannerObj.nextLine();
                        break;
                    case 6:
                        System.out.println("Enter Student's new Marks: ");
                        String entersMarks = scannerObj.nextLine();
                        i.marks = Integer.parseInt(entersMarks);
                        break;
                    default:
                        System.out.println("Select a number given above");
                }
            }else {
                System.out.println("Incorrect ID");
                menu();
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
                    String idInput = students.scannerObj.nextLine();
                    int id = Integer.parseInt(idInput);
                    students.searchStudent(id);
                    break;
                case 4:
                    break;
            }
        }

    }
}
