package OLD;

import ru.krutov.SplitWise.models.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SplitMain {

    public static void main(String[] args) {
        ArrayList<Group> groupList = new ArrayList<>();
        HashMap <String, Person> membersList = new HashMap<>();


        groupList = AddGroup("ГруппаА", groupList);
        Group chosenGroup = groupList.get(0);
        AddMember("Антон", "Крутов", "89773805859", membersList);
        AddToGroup("89773805859",chosenGroup);
        AddMember("Катя", "Покатаева", "811111111", membersList);
        AddToGroup("811111111",chosenGroup);
        AddExpence(220d, chosenGroup.groupMembers, "811111111", "Пироги", chosenGroup);
        AddExpence(300d, chosenGroup.groupMembers, "89773805859", "Пиhроги", chosenGroup);
        AddMember("Клим","Cаввин", "89999999777", membersList);
        AddToGroup("89999999777",chosenGroup);
        AddExpence(780d, chosenGroup.groupMembers, "89999999777", "Богатый Клим платит", chosenGroup);

        ShowMyBalance(chosenGroup, "89773805859", membersList);
        ShowMyBalance(chosenGroup, "811111111", membersList);
        ShowMyBalance(chosenGroup, "89999999777",membersList);
        System.out.println("################");
        System.out.println("################");
        System.out.println("################");

        recalculateAndShow(chosenGroup,membersList);

    }
    static void recalculateAndShow(Group group, HashMap <String,Person> membersList){
        group.balanceMoving();
        group.groupMembers.forEach(member -> ShowMyBalance(group,member,membersList));
    }
    public static ArrayList<Group> AddGroup(String groupName, ArrayList<Group> groupList) {
        groupList.add(new Group(groupName));
        return groupList;
    }
    public static void AddToGroup(String phone, Group groupName) {
    HashMap<String, Double> memberBalances = new HashMap<>();
    groupName.balances.put(phone, memberBalances);
    groupName.balances.forEach((member, memberMap) -> {
    memberMap.put(phone,0d);
    });
    groupName.groupMembers.add(phone);
    groupName.balances.forEach((member, memberMap) -> {
        memberBalances.put(member,0d);
        });
    groupName.balances.put(phone, memberBalances);
    groupName.userBalance.put(phone,0d);
    }
    public static void AddMember(String memberName, String surname, String phone, HashMap<String,Person> memberList) {
        String regex = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
        if (Pattern.matches(regex,phone)){
            Person newPerson = new Person(memberName,phone);
            memberList.put(phone,newPerson);
        }
    }

    public static void AddExpence(double amount, List<String> members,
                                  String whoPaid, String description, Group groupName) {
        Expense newExpense = new Expense(amount, members, whoPaid, description);
        groupName.allExpenses.add(newExpense);
        members.forEach(person -> {
            groupName.balances.computeIfPresent(whoPaid, (name, balances) -> {
                balances.put(person, balances.get(person) + amount / members.size());
                return balances;
            });
            groupName.balances.computeIfPresent(person, (name, balances) ->
            {
                balances.put(whoPaid, balances.get(whoPaid) - amount / members.size());
                return balances;
            });
            groupName.userBalance.computeIfPresent(person, (name, balance) -> balance -= amount / members.size());
        });


        groupName.userBalance.computeIfPresent(whoPaid, (name, balance) -> balance += amount);
    }

    public static void ShowMyBalance(Group groupName, String phoneNumber, HashMap<String,Person> memberList) {
        System.out.println(memberList.get(phoneNumber).getName() + ", Ваш баланс  = ".concat(groupName.userBalance.get(phoneNumber).toString()));
        HashMap<String,Double> balanceWithOthers = groupName.balances.get(phoneNumber);
        groupName.groupMembers.forEach(member -> {
            String midPart;
            if (!member.equals(phoneNumber)) {
                Double memberBalance = balanceWithOthers.get(member);
                if (memberBalance != 0) {
                    if (memberBalance > 0) {
                        midPart = " должен Вам " + memberBalance;
                        System.out.println(memberList.get(member).getName().concat(midPart));
                    } else {
                        midPart = "Вы должны " + memberBalance + " пользователю ";
                        System.out.println(midPart.concat(memberList.get(member).getName()));
                    }
                }
            }
        });
    }

}


