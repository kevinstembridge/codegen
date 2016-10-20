import ReactDOM from 'react-dom';
import React from 'react';
import client from './client';
import classNames from 'classnames';
import ValidationResult from './validationResult';


class SignupForm extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            firstName: '',
            lastName: '',
            emailAddress: '',
            password: '',
            validationMessages: {},
            formFieldClassNames: {
                firstName: 'form-group',
                lastName: 'form-group',
                emailAddress: 'form-group',
                password: 'form-group'
            },
            previouslySubmitted: false
        };
        this.onChangeFirstName = this.onChangeFirstName.bind(this);
        this.onChangeLastName = this.onChangeLastName.bind(this);
        this.onChangeEmailAddress = this.onChangeEmailAddress.bind(this);
        this.onChangePassword = this.onChangePassword.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.handleSuccessResponse = this.handleSuccessResponse.bind(this);
        this.handleErrorResponse = this.handleErrorResponse.bind(this);
    }


    onChangeFirstName(e) {
        this.setState({firstName: e.target.value}, this.validateFormIfPreviouslySubmitted);
    }


    onChangeLastName(e) {
        this.setState({lastName: e.target.value}, this.validateFormIfPreviouslySubmitted);
    }


    onChangeEmailAddress(e) {
        this.setState({emailAddress: e.target.value}, this.validateFormIfPreviouslySubmitted);
    }


    onChangePassword(e) {
        this.setState({password: e.target.value}, this.validateFormIfPreviouslySubmitted);
    }


    validateFormIfPreviouslySubmitted() {

        if (this.state.previouslySubmitted) {
            this.validateForm();
        }

    }


    validateForm() {

        const validationResult = new ValidationResult();
        this.validateFirstName(validationResult);
        this.validateLastName(validationResult);
        this.validateEmailAddress(validationResult);
        this.validatePassword(validationResult);

        this.updateStateWith(validationResult);

        return validationResult.hasErrors();

    }


    updateStateWith(validationResult) {

        const validationMessages = validationResult.getValidationMessages();
        const formFieldClassNames = this.determineFormFieldClassNames(validationResult);

        this.setState({
            validationMessages: validationMessages,
            formFieldClassNames: formFieldClassNames
        });

    }


    validateFirstName(validationResult) {

        if (this.state.firstName.trim() == '') {
            validationResult.addError('firstName', 'Required field.');
        }

    }


    validateLastName(validationResult) {

        if (this.state.lastName.trim() == '') {
            validationResult.addError('lastName', 'Required field.');
        }

    }


    validateEmailAddress(validationResult) {

        if (this.state.emailAddress.trim() == '') {
            validationResult.addError('emailAddress', 'Required field.');
        }

    }


    validatePassword(validationResult) {

        if (this.state.password.trim() == '') {
            validationResult.addError('password', 'Required field.');
        }

    }


    determineFormFieldClassNames(validationResult) {

        const firstNameClassNames = this.getClassNamesFor('firstName', validationResult);
        const lastNameClassNames = this.getClassNamesFor('lastName', validationResult);
        const emailAddressClassNames = this.getClassNamesFor('emailAddress', validationResult);
        const passwordClassNames = this.getClassNamesFor('password', validationResult);

        return {
            firstName: firstNameClassNames,
            lastName: lastNameClassNames,
            emailAddress: emailAddressClassNames,
            password: passwordClassNames
        };

    }


    getClassNamesFor(propertyName, validationResult) {

        if (validationResult.isFieldHasError(propertyName)) {
            return 'form-group has-error';
        } else if (validationResult.isFieldHasWarning(propertyName)) {
            return 'form-group has-warning';
        } else {
            return 'form-group';
        }

    }


    getFormFieldState() {

        return {
            firstName: this.state.firstName.trim(),
            lastName: this.state.lastName.trim(),
            emailAddress: this.state.emailAddress.trim(),
            password: this.state.password.trim()
        };

    }


    onSubmit(e) {

        e.preventDefault();

        var hasErrors = this.validateForm();

        if (hasErrors) {
            this.setState({previouslySubmitted: true});
            return;
        }

        var formFieldState = this.getFormFieldState();

        client({
            path: '/api/signup',
            entity: formFieldState,
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(
            this.handleSuccessResponse,
            this.handleErrorResponse
        ).catch(function(error) {
            console.log("An unexpected error: " + error);
        });

    }


    handleSuccessResponse(response) {

        console.log(response);

        this.setState({
            firstName: '',
            lastName: '',
            emailAddress: '',
            password: ''
        });

         window.location.assign("/signup/success");

    }


    handleErrorResponse(response) {

        console.log("Handling an error response: " + response);

        this.setState({previouslySubmitted: true});

        if (response.status.code == 400) {
            this.handleBadRequest(response);
        } else {
            console.log("No handler for error response: " + response);
        }

    }


    handleBadRequest(response) {

        var errors = response.entity.errors;

        const validationResult = new ValidationResult();

        for (var e of errors) {
            validationResult.addError(e.field, e.defaultMessage);
        }

        this.updateStateWith(validationResult);

    }


    render() {
        return (
            <form className="form-horizontal" onSubmit={this.onSubmit}>

                <div className={this.state.formFieldClassNames.firstName}>
                    <label htmlFor="firstName" className="col-sm-2 control-label">First Name</label>
                    <div className="col-sm-5">
                        <input
                            type="text"
                            className="form-control"
                            id="firstName"
                            placeholder="First Name"
                            value={this.state.firstName}
                            onChange={this.onChangeFirstName} />
                    </div>
                    { this.state.validationMessages.firstName ? <div className="col-sm-5"><span>{this.state.validationMessages.firstName.message}</span></div> : null }
                </div>

                <div className={this.state.formFieldClassNames.lastName}>
                    <label htmlFor="lastName" className="col-sm-2 control-label">Last Name</label>
                    <div className="col-sm-5">
                        <input
                            type="text"
                            className="form-control"
                            id="lastName"
                            placeholder="Last Name"
                            value={this.state.lastName}
                            onChange={this.onChangeLastName} />
                    </div>
                    { this.state.validationMessages.lastName ? <div className="col-sm-5"><span>{this.state.validationMessages.lastName.message}</span></div> : null }
                </div>

                <div className={this.state.formFieldClassNames.emailAddress}>
                    <label htmlFor="emailAddress" className="col-sm-2 control-label">Email</label>
                    <div className="col-sm-5">
                        <input
                            type="text"
                            className="form-control"
                            id="emailAddress"
                            placeholder="Email"
                            value={this.state.emailAddress}
                            onChange={this.onChangeEmailAddress} />
                    </div>
                    { this.state.validationMessages.emailAddress ? <div className="col-sm-5"><span>{this.state.validationMessages.emailAddress.message}</span></div> : null }
                </div>

                <div className={this.state.formFieldClassNames.password}>
                    <label htmlFor="password" className="col-sm-2 control-label">Password</label>
                    <div className="col-sm-5">
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            placeholder="Password"
                            value={this.state.password}
                            onChange={this.onChangePassword} />
                    </div>
                    { this.state.validationMessages.password ? <div className="col-sm-5"><span>{this.state.validationMessages.password.message}</span></div> : null }
                </div>

                <div className="form-group">
                    <div className="col-sm-offset-2 col-sm-10">
                        <button type="submit" className="btn btn-default">Submit</button>
                    </div>
                </div>

            </form>
        );
    }


}

ReactDOM.render(
    <SignupForm />,
    document.getElementById('signupFormDiv')
);