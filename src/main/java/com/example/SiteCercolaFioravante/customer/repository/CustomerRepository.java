package com.example.SiteCercolaFioravante.customer.repository;

import com.example.SiteCercolaFioravante.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT ctm.surname as surname, ctm.name as name, ctm.email as email FROM Customer ctm WHERE UPPER( ctm.name ) LIKE CONCAT('%', UPPER( :nameSurname ), '%' ) OR  UPPER( ctm.surname ) LIKE CONCAT('%', UPPER( :nameSurname ), '%' ) ")
    ArrayList<CustomerProjectionList> getCustomerByNameOrSurname(@Param("nameSurname") String nameSurname);

    @Query("SELECT ctm.surname as surname, ctm.name as name, ctm.email as email FROM Customer ctm WHERE  ctm.phoneNumber = :phoneNumber  ")
    ArrayList<CustomerProjectionList> getCustomerByPhoneNumber(@Param("phoneNumber") long phoneNumber);

    @Query("SELECT ctm.surname as surname, ctm.name as name, ctm.email as email FROM Customer ctm WHERE UPPER( ctm.email ) LIKE CONCAT('%', UPPER( :email ), '%' )  ")
    ArrayList<CustomerProjectionList> getCustomerByEmail(@Param("email") String email);

}
