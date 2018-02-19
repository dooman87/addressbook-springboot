package com.github.dooman87.addressbook.contact;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContactRepo extends PagingAndSortingRepository<Contact, Long> {
}
