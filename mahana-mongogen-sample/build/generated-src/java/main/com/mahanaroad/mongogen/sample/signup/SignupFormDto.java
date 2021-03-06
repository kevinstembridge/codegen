// This source was generated by the Mahana Mongogen generator
// Renderer class: class com.mahanaroad.mongogen.generator.dtos.DtoRenderer
// Rendered at: 2016-09-30T22:30:59.775Z

package com.mahanaroad.mongogen.sample.signup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mahanaroad.mongogen.sample.contact.EmailAddress;
import com.mahanaroad.mongogen.sample.types.FirstName;
import com.mahanaroad.mongogen.sample.types.LastName;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


public class SignupFormDto {


    @Email
    @NotEmpty
    @NotNull
    private final String emailAddress;

    @NotEmpty
    @NotNull
    private final String firstName;

    @NotEmpty
    @NotNull
    private final String lastName;

    @NotEmpty
    @NotNull
    private final String password;


    @JsonCreator
    public SignupFormDto(
            @JsonProperty("emailAddress") final String emailAddress,
            @JsonProperty("firstName") final String firstName,
            @JsonProperty("lastName") final String lastName,
            @JsonProperty("password") final String password) {


        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;

    }


    public EmailAddress getEmailAddress() {

        return new EmailAddress(this.emailAddress);

    }


    public FirstName getFirstName() {

        return new FirstName(this.firstName);

    }


    public LastName getLastName() {

        return new LastName(this.lastName);

    }


    public String getPassword() {

        return this.password;

    }


    @Override
    public String toString() {

        return "SignupFormDto{" +
                "firstName = '" + this.firstName + '\'' + ", " + 
                "lastName = '" + this.lastName + '\'' + ", " + 
                "emailAddress = '" + this.emailAddress + '\'' + ", " + 
                "password = 'MASKED'" +
                "}";

    }


}
