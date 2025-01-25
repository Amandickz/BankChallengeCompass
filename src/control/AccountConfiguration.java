package control;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class AccountConfiguration {

    public Date dateFormatStringtoSQL(String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date convertionDate1 = sdf.parse(date);

        Date convetionDate2 = new Date(convertionDate1.getTime());

        return convetionDate2;
    }

    public int createNumberAccount() {
        Random random = new Random();

        int numberAccount = random.nextInt(9999) + 1;

        return numberAccount;
    }

}
