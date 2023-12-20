package OLD;

import java.util.List;

class Expense {
    double amount;
    List<String> members;
    String whoPaid;
    String Description;

    public Expense(double amount, List<String> members, String whoPaid, String description) {
        this.amount = amount;
        this.members = members;
        this.Description = description;
        this.whoPaid = whoPaid;
    }
}
