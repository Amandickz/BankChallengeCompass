package verifications;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateVerification {

    public boolean verificationBirthday(Date date, LocalDateTime today) throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int yearBirthday = Integer.parseInt(date.toString().substring(0, 4));
        int actualYear = Integer.parseInt(formatter.format(today).split("-")[0]);
        int monthBirthday = Integer.parseInt(date.toString().substring(5, 7));
        int actualMonth = Integer.parseInt(formatter.format(today).split("-")[1]);
        int dayBirthday = Integer.parseInt(date.toString().substring(8, 10));
        int actualDay = Integer.parseInt(formatter.format(today).split("-")[2]);

        if ((actualYear - yearBirthday) < 17) {
            return false;
        } else if ((actualYear - yearBirthday) == 18 && monthBirthday > actualMonth) {
            return false;
        } else if ((actualYear - yearBirthday) == 18 && monthBirthday <= actualMonth && dayBirthday > actualDay) {
            return false;
        }

        return true;
    }

}
