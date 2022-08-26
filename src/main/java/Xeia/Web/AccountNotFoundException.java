package Xeia.Web;

import org.springframework.validation.ObjectError;

public class AccountNotFoundException extends ObjectError {
    public AccountNotFoundException(String objectName, String defaultMessage) {
        super(objectName, defaultMessage);
    }

    public AccountNotFoundException(String objectName, String[] codes, Object[] arguments, String defaultMessage) {
        super(objectName, codes, arguments, defaultMessage);
    }

    public AccountNotFoundException() {
        super("accountNotFound", "Username or password entered was incorrect");
    }
}
