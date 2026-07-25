import java.util.ArrayList;
import java.util.Scanner;

public class ExpenseTracker {

    static ArrayList<String> expenses = new ArrayList<>();
    static ArrayList<Double> amounts = new ArrayList<>();
    static Scanner scannerObj = new Scanner(System.in);
    static int selection = 2;

    public static void menu() {

        System.out.println(
                "===== Expense Tracker =====\n" +
                        "\n" +
                        "1. Add Expense\n" +
                        "2. View Expenses\n" +
                        "3. Show Total\n" +
                        "4. Exit\n"+
                        "\n" +
                        "Choose option "
        );
        String input = scannerObj.nextLine();
        selection = Integer.parseInt(input);

    }
    public static void addExpense(String name, double cost){
        expenses.add(name);
        amounts.add(cost);
    }
    public static void viewExpenses(){
        for (int i = 0; i < expenses.size(); i++){
            System.out.println(expenses.get(i) + " " + amounts.get(i) + "\n");
        }
    }

    public static double calculateTotal(){
        double result = 0.0;
        for (int i = 0; i < amounts.size(); i++){
            result += amounts.get(i);
        }
        return result;
    }





    public static void main(String[] args) {
        while (selection != 4){
            menu();
            switch(selection){
                case 1:
                    String name = "";
                    double cost = 0.0;
                    System.out.println("Add the name of the item: ");
                    name =  scannerObj.nextLine();
                    System.out.println("Add the cost of the item: ");
                    String costInput = scannerObj.nextLine();
                    cost = Double.parseDouble(costInput);
                    addExpense(name , cost);
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    System.out.println(calculateTotal());

                    break;
                case 4:
                    break;
            }
        }
    }
}