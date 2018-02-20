package com.github.dooman87.addressbook.contact;

import com.github.dooman87.addressbook.addressbook.AddressBook;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "addressbook_id")
    private AddressBook addressBook;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AddressBook getAddressBook() {
        return addressBook;
    }

    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }
}
