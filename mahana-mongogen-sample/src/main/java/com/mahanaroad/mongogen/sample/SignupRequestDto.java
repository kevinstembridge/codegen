package com.mahanaroad.mongogen.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mahanaroad.mongogen.sample.types.FirstName;
import com.mahanaroad.mongogen.sample.types.LastName;
import org.apache.tools.ant.taskdefs.email.EmailAddress;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class SignupRequestDto {


    @NotNull
    @NotEmpty
    private final String firstName;

    @NotNull
    @NotEmpty
    private final String lastName;

    @NotNull
    @NotEmpty
    @Email
    private final String emailAddress;

    @NotNull
    @NotEmpty
    private final String password;


    @JsonCreator
    public SignupRequestDto(@JsonProperty("firstName") final String firstName, @JsonProperty("lastName") final String lastName, @JsonProperty("emailAddress") final String emailAddress, @JsonProperty("password") final String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;

    }


    public FirstName getFirstName() {

        return new FirstName(this.firstName);

    }


    public LastName getLastName() {

        return new LastName(this.lastName);

    }


    public EmailAddress getEmailAddress() {

        return new EmailAddress(this.emailAddress);

    }


    @Override
    public String toString() {

        return "SignupRequestDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                '}';

    }


}

