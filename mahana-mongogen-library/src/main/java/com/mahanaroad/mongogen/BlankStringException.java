package com.mahanaroad.mongogen;


/**
 * Indicates that a string was either null, empty or consisted only of whitespace.
 */
public class BlankStringException extends RuntimeException {


    public static String throwIfBlank(String argumentValue, String argumentName) {

    	if (argumentValue == null || "".equals(argumentValue.trim())) {
    		throw new BlankStringException(argumentName);
    	}

        return argumentValue;

    }


    private final String argumentName;


    private BlankStringException(String argumentName) {

    	super("The [" + argumentName + "] argument cannot be null or blank.");
        this.argumentName = argumentName;

    }

    
    public String getArgumentName() {
    	return this.argumentName;
    }


}
