import java.util.ArrayList;
import java.util.Scanner;

public class ExpenseTracker {

    ArrayList<Expense> expenses = new ArrayList<>();
    Scanner scannerObj = new Scanner(System.in);
    int selection = 2;

    public void menu() {

        System.out.println(
                "===== Expense Tracker =====\n" +
                        "\n" +
                        "1. Add Expense\n" +
                        "2. View Expenses\n" +
                        "3. Show Total\n" +
                        "4. Delete Expense\n"+
                        "5. Exit\n"+
                        "\n" +
                        "Choose option "
        );
        String input = scannerObj.nextLine();
        selection = Integer.parseInt(input);

    }

    public class Expense {
        protected String name;
        protected double cost;

        public Expense(String inputName, double inputCost) {
            this.name  = inputName;
            this.cost = inputCost;
        }
        public String toString() {
            return name + " " + cost;
        }
    }

    public void addExpense(String name, double cost){
        Expense myNewExpense = new Expense(name, cost);
        expenses.add(myNewExpense);
    }
    public void viewExpenses(){
        for (int i = 0; i < expenses.size(); i++){
            System.out.println(expenses.get(i) + "\n");
        }
    }

    public double calculateTotal(){
        double result = 0.0;
        for (int i = 0; i < expenses.size(); i++){
            result += expenses.get(i).cost;
        }
        return result;
    }

    public void deleteExpense(){
        if (expenses.isEmpty()){
            System.out.println("There is nothing to delete");
        }
        System.out.println("Select what u wanna delete");
        for(int i =0; i < expenses.size(); i++){
            System.out.println((i+1) + "." + expenses.get(i) + "\n");
        }
        int selection = 0;
        boolean validInput = false;
        while (!validInput){
            String userInput = scannerObj.nextLine();
            try {
                selection = Integer.parseInt(userInput);

                if (selection >= 1 && selection <= expenses.size()){
                    validInput = true;
                }else{
                    System.out.println("Please enter a number between 1 and" + expenses.size() + ":");
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid input! Please enter a valid number:");
            }
        }

        expenses.remove(selection - 1);
    }

    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        while (tracker.selection != 5){
            tracker.menu();
            switch(tracker.selection){
                case 1:
                    String name = "";
                    double cost = 0.0;

                    System.out.println("Add the name of the item: ");
                    name =  tracker.scannerObj.nextLine();
                    boolean validInput = false;
                    while (!validInput) {
                        System.out.println("Add the cost of the item: ");
                        String costInput = tracker.scannerObj.nextLine();
                        try {
                            cost = Double.parseDouble(costInput);
                            validInput = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input! Please enter a valid number (e.g., 10.50).");
                        }
                    }
                    tracker.addExpense(name , cost);
                    break;
                case 2:
                    tracker.viewExpenses();
                    break;
                case 3:
                    System.out.println("Total Expenses: " + tracker.calculateTotal());
                    break;
                case 4:
                    tracker.deleteExpense();
                    break;
                case 5:
                    break;
            }
        }
    }
}