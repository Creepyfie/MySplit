package OLD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
    List<Expense> allExpenses = new ArrayList<>();
    List<String> groupMembers = new ArrayList<>();
   // Map<String, Person> groupMembers = new HashMap<>();
    String groupName;
    Map<String, HashMap<String, Double>> balances = new HashMap<>();
    Map<String, Double> userBalance = new HashMap<>();

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public void balanceMoving() {
        groupMembers.forEach(member -> {
            HashMap<String, Double> cache;
            cache = balances.get(member);
            //получаем мапу : номер телефона/баланс с этим номером
            cache.forEach((phoneNumber, balance) -> {
                if (cache.get(phoneNumber) > 0) {
                    String plusphone = phoneNumber;
                    cache.forEach((phone2,balance2) -> {
                        if (balance2 < 0 &&  balance > 0) {
                            double thisBalance = balance;
                            double goesToAnother;
                            if (thisBalance + balance2 >= 0) {
                                thisBalance += balance2;
                                goesToAnother = -cache.get(phone2);
                                balance2 = 0d;
                            } else {
                                balance2 =cache.get(phone2) +  thisBalance;
                                goesToAnother =  thisBalance;
                                thisBalance = 0d;
                            }
                            cache.put(plusphone, thisBalance);
                            cache.put(phone2, balance2);
                            //меняем балансы у plusid и minusphone
                            //меняем балансы должнику, то есть тому, кто должен был текущему пользователю
                            HashMap<String, Double> cacheplusminus = balances.get(plusphone);
                            //уменьшаем долг перед member
                            cacheplusminus.put(member, cacheplusminus.get(member) + goesToAnother);
                            //увеличиваем долг перед тем, кому должен был member
                            cacheplusminus.put(phone2, cacheplusminus.get(phone2) - goesToAnother);
                            balances.put(plusphone, cacheplusminus);
                            //аналогично меняем балансы у того, кому должен был member
                            cacheplusminus = balances.get(phone2);
                            //уменьшаем долг member
                            cacheplusminus.put(member, cacheplusminus.get(member) - goesToAnother);
                            //увеличиваем долг должника member
                            cacheplusminus.put(plusphone, cacheplusminus.get(plusphone) + goesToAnother);
                            balances.put(phone2, cacheplusminus);
                        }

                    });

                }
            });
            balances.put(member, cache);
        });
    }
}
