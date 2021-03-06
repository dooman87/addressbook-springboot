package com.github.dooman87.addressbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dooman87.addressbook.contact.ContactRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sun.reflect.annotation.ExceptionProxy;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AddressbookApplicationTests {
    private static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
    }

    @Test
    public void userCanAddAndDeleteContact() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                    post("/contacts")
                    .content(contactJson("Batman", "0123-HELP"))
        )
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", containsString("/contacts")))
        .andReturn().getResponse();

        String contactLink = response.getHeader("Location");

        mockMvc.perform(get(contactLink))
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.name").value(equalTo("Batman")))
                .andExpect(jsonPath("@.phone").value(equalTo("0123-HELP")));

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("@._embedded.contacts[0].name").value(equalTo("Batman")))
                .andExpect(jsonPath("@._embedded.contacts[0].phone").value(equalTo("0123-HELP")));

        mockMvc.perform(delete(contactLink)).andExpect(status().isNoContent());

        mockMvc.perform(get(contactLink)).andExpect(status().isNotFound());
    }

    @Test
    public void userCantCreateContactWithoutName() throws Exception {
        mockMvc.perform(
                post("/contacts")
                        .content(contactJson("", "mr.invisible"))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void userCanAddContactsToAddressBook() throws Exception {
        MockHttpServletResponse addressBookResponse = mockMvc.perform(
                post("/addressBooks")
                        .content(addressBookJson("When need help"))
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/addressBooks")))
                .andReturn().getResponse();

        MockHttpServletResponse contactResponse = mockMvc.perform(
                post("/contacts")
                        .content(contactJson("Batman", "0123-HELP"))
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/contacts")))
                .andReturn().getResponse();

        String addressBookLink = addressBookResponse.getHeader("Location");
        String contactLink = contactResponse.getHeader("Location");

        //Adding contact to address book
        mockMvc.perform(
                put(contactLink + "/addressBook")
                    .header("Content-Type", "text/uri-list")
                    .content(addressBookLink)
        ).andExpect(status().isNoContent());

        //Testing that address book has added contact
        mockMvc.perform(
                get(addressBookLink + "/contacts")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("@._embedded.contacts[0].name").value(equalTo("Batman")));

        //Deleting contact and address book
        mockMvc.perform(delete(contactLink)).andExpect(status().isNoContent());
        mockMvc.perform(delete(addressBookLink)).andExpect(status().isNoContent());
    }

    private String contactJson(String name, String phone) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("phone", phone);

        return mapper.writeValueAsString(map);
    }

    private String addressBookJson(String name) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);

        return mapper.writeValueAsString(map);
    }
}
