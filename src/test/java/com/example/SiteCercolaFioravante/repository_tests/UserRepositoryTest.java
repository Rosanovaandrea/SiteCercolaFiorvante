package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.SiteCercolaFioravanteApplication;
import com.example.SiteCercolaFioravante.SiteCercolaFioravanteApplicationTests;
import com.example.SiteCercolaFioravante.user.User;
import com.example.SiteCercolaFioravante.user.repository.UserProjectionList;
import com.example.SiteCercolaFioravante.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;



@DataJpaTest
public class UserRepositoryTest {

    private User testUser;

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        testUser = new User();
        testUser.setEmail("ardemus@gnail.com");
        testUser.setName("andres");
        testUser.setSurname("rosanova");
        testUser.setRole("ADMIN");
        testUser.setPhoneNumber(3333333333L);
        userRepository.save(testUser);
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundById() {
        User savedUser = userRepository.findById(testUser.getEmail()).orElse(null);
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        assertEquals(testUser.getName(), savedUser.getName());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundByEmail() {
        UserProjectionList savedUser = userRepository.getUsersByEmail(testUser.getEmail()).get(0);
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        assertEquals(testUser.getName(), savedUser.getName());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundBynameUsername() {
        UserProjectionList savedUser = userRepository.getUserByNameOrSurname(testUser.getName()).get(0);
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        assertEquals(testUser.getName(), savedUser.getName());
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundByPhoneNumber() {
        UserProjectionList savedUser = userRepository.getUserByPhoneNumber(testUser.getPhoneNumber()).get(0);
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        assertEquals(testUser.getName(), savedUser.getName());
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        userRepository.delete(testUser);
    }
}
