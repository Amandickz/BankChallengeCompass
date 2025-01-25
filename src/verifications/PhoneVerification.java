package verifications;

public class PhoneVerification {

    public boolean verificationValidFormat(String phone){

        if(phone.length() < 10 || phone.length() > 12){
            System.out.println("Invalid phone number! Try again.");
            return false;
        }

        if (!verificationEqualsNumbers(phone)) {
            return false;
        }

        return true;
    }

    public String formatPhone(String phone){

        String formattedPhone = "";

        if(phone.length() == 10){
            formattedPhone = "(" + phone.substring(0,2) + ") 9" + phone.substring(2,6)
                            + "-" + phone.substring(6,10);
        } else if (phone.length() == 11){
            if (phone.charAt(0) == '0'){
                formattedPhone = "(" + phone.substring(1,3) + ") 9" + phone.substring(3,7)
                        + "-" + phone.substring(7,11);
            } else {
                formattedPhone = "(" + phone.substring(0,2) + ") " + phone.substring(2,7)
                        + "-" + phone.substring(7,11);
            }
        } else {
            formattedPhone = "(" + phone.substring(1,3) + ") " + phone.substring(3,8)
                    + "-" + phone.substring(8,12);
        }

        return formattedPhone;
    }

    protected boolean verificationEqualsNumbers(String phone){
        if(phone.equals("0000000000") || phone.equals("00000000000") || phone.equals("0000000000000")){
            return false;
        } else if (phone.equals("1111111111") || phone.equals("11111111111") || phone.equals("1111111111111")){
            return false;
        } else if (phone.equals("2222222222") || phone.equals("22222222222") || phone.equals("2222222222222")) {
            return false;
        } else if (phone.equals("3333333333") || phone.equals("33333333333") || phone.equals("3333333333333")) {
            return false;
        } else if (phone.equals("4444444444") || phone.equals("44444444444") || phone.equals("4444444444444")) {
            return false;
        } else if (phone.equals("5555555555") || phone.equals("55555555555") || phone.equals("5555555555555")) {
            return false;
        } else if (phone.equals("6666666666") || phone.equals("66666666666") || phone.equals("6666666666666")) {
            return false;
        } else if (phone.equals("7777777777") || phone.equals("77777777777") || phone.equals("7777777777777")) {
            return false;
        } else if (phone.equals("8888888888") || phone.equals("88888888888") || phone.equals("8888888888888")) {
            return false;
        } else if (phone.equals("9999999999") || phone.equals("99999999999") || phone.equals("9999999999999")) {
            return false;
        } else if (phone.equals("1234567890") || phone.equals("0123456789")) {
            return false;
        }
        return true;
    }

}
