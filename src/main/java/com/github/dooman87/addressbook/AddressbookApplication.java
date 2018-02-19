package com.github.dooman87.addressbook;

import com.github.dooman87.addressbook.contact.ContactValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;

@SpringBootApplication
public class AddressbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddressbookApplication.class, args);
    }
}
