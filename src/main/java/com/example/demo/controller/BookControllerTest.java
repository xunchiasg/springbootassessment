package com.example.demo.controller;

import com.example.demo.Repository.BookRepository;
import com.example.demo.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;




public class BookControllerTest {




    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "/api/customers";

    private Book book1, book2;
    private List<Book> BookList = new ArrayList<>();

    // Run before each JUNIT test operation
    @BeforeEach
    void setUp() {

        // Delete all records in the database before starting
        bookRepository.deleteAll();

        // arrange (precondition)
        book1 = Book.builder()
                .id(Long.valueOf("001"))
                .title("To kill a Mockingbird")
                .author("Harper Lee")
                .build();

        // arrange (precondition)
        book1 = Book.builder()
                .id(Long.valueOf("002"))
                .title("Moby Dick")
                .author("Herman Melville")
                .build();


        bookRepository.add(book1);
        bookRepository.add(book2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("** JUNIT test: get all books from books Ctrl. **")
    void allBooks() throws Exception{

        // arrange - setup precondition
        bookRepository.saveAll(bookRepository.findAll());

        // act - action or behaviour to test
        ResultActions resultActions = MockMvc.perform(get(API_ENDPOINT));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(bookRepository.size())));
    }

    @Test
    @DisplayName("** JUNIT test: get book by Id")
    void setBook1() throws Exception{

        // arrange - setup precondition
        bookRepository.save(book1);

        // act - action or behaviour to test
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/{id}"), book1.getId()));

        // assert - verify the output
        ResultActions resultActions1 = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(book1.getId()))
                .andExpect(jsonPath("$.title").value(book1.getTitle())
                        .andExpect(jsonPath("$.author").value(book1.getAuthor())
                                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(book1.getTitle()));
    }

    @Test
    void saveCustomer() throws Exception {

        // arrange - setup precondition
        String requestBody = objectMapper.writeValueAsString(book1);

        // act - action or behaviour to test
        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the output
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(book1.getId()))
                .andExpect(jsonPath("$.title").value(book1.getTitle()))
                .andExpect(jsonPath("$.author").value(book1.getAuthor()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(book1.getTitle())));

    }

    @Test
    @DisplayName("** JUNIT test: update a book from Customer Ctrl")
    void updateCustomer() throws Exception {

        // arrange - setup precondition
        bookRepository.save(book1);

        Book updatebook1 = bookRepository.findById(book1.getId()).get();

        updatebook1.setId(Long.valueOf("Updated ID"));
        updatebook1.setTitle("Updated Title");
        updatebook1.setAuthor("Updated Author");

        String requestBody = objectMapper.writeValueAsString(updatebook1);

        // act - action or behaviour to test
        ResultActions resultActions = mockMvc.perform(put(API_ENDPOINT.concat("/{id}"), updatebook1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(updatebook1.getId()))
                .andExpect(jsonPath("$.title").value(updatebook1.getTitle()))
                .andExpect(jsonPath("$.author").value(updatebook1.getAuthor()));
    }

    @Test
    void deleteCustomer() throws Exception {

        // arrange - setup precondition
        bookRepository.save(book1);

        Book deleteBook1 = bookRepository.findById(book1.getId()).get();

        String expectedResponse = String.format("%s deleted successfully", deleteBook1.getName());

        // act - action or behaviour to test
        ResultActions resultActions = mockMvc.perform(delete(API_ENDPOINT.concat("/{id}"), deleteBook1.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                // Check that the body response matches the expected message
                .andExpect(result -> assertEquals(expectedResponse, result.getResponse().getContentAsString()));
    }

    @Test
    void countCustomer() throws Exception {

        // arrange - setup precondition
        bookRepository.saveAll(bookRepository.findAll());
        long count = bookRepository.count();

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("total", count);

        String expectedString = objectMapper.writeValueAsString(expectedResponse);

        // act - action or behaviour to test
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/count"))
                .contentType(MediaType.APPLICATION_JSON));

        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(result -> assertEquals(expectedString, result.getResponse().getContentAsString()));
    }


}
