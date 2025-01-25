package verifications;

public class CPFVerification {

    public boolean verification(String cpf) {
        if (cpf.length() < 11) {
            System.out.println("Invalid CPF, need to be 11 digits.");
            return false;
        }

        if (!oneNumberVerification(cpf)){
            System.out.println("CPF have just one number.");
            return false;
        }

        if (!letterVerification(cpf)){
            System.out.println("CPF have some letter.");
            return false;
        }

        return true;
    }

    protected boolean oneNumberVerification(String cpf) {
        if (cpf.equals("00000000000")) {
            return false;
        } else if (cpf.equals("11111111111")) {
            return false;
        } else if (cpf.equals("22222222222")) {
            return false;
        } else if (cpf.equals("33333333333")) {
            return false;
        } else if (cpf.equals("44444444444")) {
            return false;
        } else if (cpf.equals("55555555555")) {
            return false;
        } else if (cpf.equals("66666666666")) {
            return false;
        } else if (cpf.equals("77777777777")) {
            return false;
        } else if (cpf.equals("88888888888")) {
            return false;
        } else if (cpf.equals("99999999999")) {
            return false;
        }

        return true;
    }

    protected boolean letterVerification(String cpf) {

        try {
            int cont = 0;

            for (int i = 0; i < cpf.length(); i++) {
                int number = Integer.parseInt(cpf.substring(i, i + 1));
                if (number >= 0 && number <= 9) {
                    cont++;
                } else {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

}

/*oneNumberVerification: verifica se o CPF digitado não foi somente um número
* letterVerification: verifica se há alguma letra digitada no CPF*/