export default class ValidationResult {


    constructor() {

        this.validationMessages = [];

    }


    addWarning(propertyName, message) {

        this.validationMessages.push(new ValidationMessage(propertyName, 'warning', message));

    }


    addError(propertyName, message) {

        this.validationMessages.push(new ValidationMessage(propertyName, 'error', message));

    }


    hasErrors() {

        return this.validationMessages.some(v => v.severity == 'error');

    }


    getValidationMessages() {

        const result = {};

        for (var m of this.validationMessages) {
            result[m.propertyName] = {'severity': m.severity, 'message': m.message};
        }

        return result;

    }


    isFieldHasError(propertyName) {

        return this.validationMessages.some(m => m.propertyName == propertyName && m.severity == 'error');

    }


    isFieldHasWarning(propertyName) {

        return this.validationMessages.some(m => m.propertyName == propertyName && m.severity == 'warning');

    }

}


export class ValidationMessage {

    constructor(propertyName, severity, message) {

        this.propertyName = propertyName;
        this.severity = severity;
        this.message = message;

    }


}


export class ValidationUtil {

    constructor() {

        this.emailRegex = /^(?:[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]|[^\u0000-\u007F])+@(?:[a-zA-Z0-9]|[^\u0000-\u007F])(?:(?:[a-zA-Z0-9-]|[^\u0000-\u007F]){0,61}(?:[a-zA-Z0-9]|[^\u0000-\u007F]))?(?:\.(?:[a-zA-Z0-9]|[^\u0000-\u007F])(?:(?:[a-zA-Z0-9-]|[^\u0000-\u007F]){0,61}(?:[a-zA-Z0-9]|[^\u0000-\u007F]))?)*$/;

    }


    isInvalidEmail(email) {

        if (email.trim() == '' || this.emailRegex.test(email)) {

            return false;

        }

        return true;

    }


}
