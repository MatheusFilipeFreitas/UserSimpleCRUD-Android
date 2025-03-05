package com.example.myapplication.utils.validator;

public class DocumentValidatorImpl implements DocumentValidator{

    @Override
    public boolean validateCPF(String cpf) {
        if (cpf == null) return false;

        String cleanCPF = cpf.replaceAll("[^0-9]", "");

        if (cleanCPF.length() != 11 || cleanCPF.matches("(\\d)\\1{10}")) return false;

        try {
            int sum = 0, weight = 10;
            for (int i = 0; i < 9; i++) {
                sum += (cleanCPF.charAt(i) - '0') * weight--;
            }
            int firstDigit = (sum % 11 < 2) ? 0 : 11 - (sum % 11);

            sum = 0;
            weight = 11;
            for (int i = 0; i < 10; i++) {
                sum += (cleanCPF.charAt(i) - '0') * weight--;
            }
            int secondDigit = (sum % 11 < 2) ? 0 : 11 - (sum % 11);

            return cleanCPF.endsWith("" + firstDigit + secondDigit);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validateTelefone(String telefone) {
        if (telefone == null) return false;
        String cleanPhone = telefone.replaceAll("[^0-9]", "");
        return cleanPhone.matches("^\\d{10,11}$");
    }
}
