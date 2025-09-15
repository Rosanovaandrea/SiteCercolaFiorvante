package com.example.SiteCercolaFioravante.repository_tests;

import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.service.repository.ServiceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;

@DataJpaTest
public class ServiceRepositoryTest {
    private Service service;
    private Service service2;
    private final ServiceRepository serviceRepository;

    public ServiceRepositoryTest(@Autowired ServiceRepository serviceRepository){
        this.serviceRepository= serviceRepository;
    }

    @BeforeEach
    void init() {
        service = new Service();
        service.setServiceName("massaggio");
        service.setPrice(200.0d);
        HashSet<String> images = new HashSet<String>();
        images.add("/first");
        service.setDescription("massaggio antani");
        serviceRepository.saveAndFlush(service);

        service2 = new Service();
        service2.setServiceName("shampoo");
        service2.setPrice(100.0d);
        images = new HashSet<String>();
        images.add("/second");
        service2.setDescription("shampoo antani");
        serviceRepository.saveAndFlush(service2);
    }

    @Test
    void getServiceByNameTest(){
       String name = serviceRepository.getServiceByName(service.getServiceName().substring(0,3)).get(0);
        Assertions.assertEquals(service.getServiceName(),name);
    }

    @Test
    void getAllServicesTest(){
       List<String> names = serviceRepository.getServiceListByNames();
       Assertions.assertNotNull(names);
       Assertions.assertEquals(names.get(0),service.getServiceName());
       Assertions.assertEquals(names.get(1),service2.getServiceName());
    }

    @AfterEach
    void detatch(){
        serviceRepository.delete(service);
    }
}
