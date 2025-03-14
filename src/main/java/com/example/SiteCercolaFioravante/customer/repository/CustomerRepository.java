package com.example.SiteCercolaFioravante.customer.repository;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoList;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT ctm.surname as surname, ctm.name as name, ctm.email as email FROM Customer ctm WHERE UPPER( ctm.name ) LIKE CONCAT('%', UPPER( :nameSurname ), '%' ) OR  UPPER( ctm.surname ) LIKE CONCAT('%', UPPER( :nameSurname ), '%' ) ")
    List<CustomerDtoList> getCustomerByNameOrSurname(@Param("nameSurname") String nameSurname);

    @Query("SELECT ctm.surname as surname, ctm.name as name, ctm.email as email, ctm.phoneNumber as phoneNumber FROM Customer ctm WHERE  ctm.phoneNumber = :phoneNumber  ")
    CustomerDtoSafe getCustomerByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT ctm.surname as surname, ctm.name as name, ctm.email as email FROM Customer ctm WHERE UPPER( ctm.email ) LIKE CONCAT('%', UPPER( :email ), '%' )  ")
    List<CustomerDtoList> getCustomerByEmail(@Param("email") String email);

    @Query("SELECT ctm.id as id from Customer ctm WHERE ctm.email = :email")
    long getCustomerIdFromEmail(@Param("email") String email );

    @Query("SELECT ctm.password as password from Customer ctm WHERE ctm.email = :email")
    String getCustomerPasswordFromEmail(@Param("email") String email );

    CustomerDtoSafe getCustomerSafe(String email);

    @Query("UPDATE Customer ctm SET ctm.tokenRegistration = :token WHERE ctm.email = :email")
    void setToken(@Param("token")String token, @Param("email") String email);

    @Query("SELECT MAX(ctm.id) FROM Customer ctm")
    long getCurrentId();

    Customer getCustomerFromEmail(String email);

}
