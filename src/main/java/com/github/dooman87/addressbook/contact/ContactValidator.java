package com.github.dooman87.addressbook.contact;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ContactValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Contact.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Contact contact = (Contact) target;
        ValidationUtils.rejectIfEmpty(errors, "name", "EMPTY_CONTACT_NAME", "Contact name is mandatory");
    }
}
