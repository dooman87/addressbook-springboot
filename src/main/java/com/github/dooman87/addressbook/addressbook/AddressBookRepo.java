package com.github.dooman87.addressbook.addressbook;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressBookRepo extends PagingAndSortingRepository<AddressBook, Long> {
}
