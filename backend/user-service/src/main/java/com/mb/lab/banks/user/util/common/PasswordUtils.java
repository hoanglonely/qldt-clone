package com.mb.lab.banks.user.util.common;

import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;

public class PasswordUtils {

    private static final Rule[] PASSWORD_RULES = new Rule[] {
            // length between 8 and 30 characters
            new LengthRule(8, 30),

            // at least one upper-case character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),

            // at least one lower-case character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),

            // at least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1),

            // at least one symbol (special character)
            new CharacterRule(EnglishCharacterData.Special, 1) };
    
    private static final CharacterRule[] PASSWORD_GENERATE_RULES = new CharacterRule[] {

            // at least one upper-case character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),

            // at least one lower-case character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),

            // at least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1),

            // at least one symbol (special character)
            new CharacterRule(new SpecialCharacterData(), 1) };
    
    private static PasswordValidator PASSWORD_VALIDATOR = new PasswordValidator(PASSWORD_RULES);
    
    private static PasswordGenerator PASSWORD_GENERATOR = new PasswordGenerator();

    public static boolean isValidPassword(String password) {
        RuleResult result = PASSWORD_VALIDATOR.validate(new PasswordData(new String(password)));
        return result.isValid();
    }
    
    public static boolean isWeakPassword(String password, List<String> weakPasswordPatterns) {
        for (String weakPassword : weakPasswordPatterns) {
            if(password.equals(weakPassword)) {
                return true;
            }
        }
        return false;
    }
    
    public static String generatePassword() {
        return PASSWORD_GENERATOR.generatePassword(10, PASSWORD_GENERATE_RULES);
    }
    
    private static class SpecialCharacterData implements org.passay.CharacterData {
        
        private static final String ERROR_CODE = "INSUFFICIENT_SPECIAL";

        private static final String CHARACTERS = "#$%&+-<=>?@";

        @Override
        public String getErrorCode() {
            return ERROR_CODE;
        }

        @Override
        public String getCharacters() {
            return CHARACTERS;
        }
        
    }
    
}
