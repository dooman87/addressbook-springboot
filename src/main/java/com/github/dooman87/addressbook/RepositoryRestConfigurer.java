package com.github.dooman87.addressbook;

import com.github.dooman87.addressbook.contact.ContactValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;

@Configuration
public class RepositoryRestConfigurer extends RepositoryRestConfigurerAdapter {
    public static final Validator CONTACT_VALIDATOR = new ContactValidator();

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", CONTACT_VALIDATOR);
        validatingListener.addValidator("beforeSave", CONTACT_VALIDATOR);

    }
}
