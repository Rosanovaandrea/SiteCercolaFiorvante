package com.example.SiteCercolaFioravante.customer.repository;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @NativeQuery("(SELECT ctm.surname as surname, ctm.name as name, ctm.email as email FROM customer ctm WHERE ctm.role != 'ADMIN' AND LOWER( ctm.name ) LIKE CONCAT(LOWER(:query), '%')LIMIT 3)" + " UNION " +
                 "(SELECT ctm.surname as surname, ctm.name as name, ctm.email as email FROM customer ctm WHERE ctm.role != 'ADMIN' AND LOWER( ctm.surname ) LIKE CONCAT(LOWER(:query), '%') LIMIT 3)" + " UNION " +
                 "(SELECT ctm.surname as surname, ctm.name as name, ctm.email as email FROM customer ctm WHERE ctm.role != 'ADMIN' AND LOWER( ctm.phone_number ) LIKE CONCAT( LOWER(:query), '%') LIMIT 3)" + " UNION " +
                 "(SELECT ctm.surname as surname, ctm.name as name, ctm.email as email FROM customer ctm WHERE ctm.role != 'ADMIN' AND LOWER( ctm.email ) LIKE CONCAT( LOWER(:query), '%') LIMIT 3)"
                 )
    List<CustomerDtoListProjection> getCustomerByNameOrSurname(@Param("query") String query);

    @Query("SELECT new com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe" +
            " (ctm.surname, ctm.name, ctm.email, ctm.phoneNumber)" +
            " FROM Customer ctm WHERE  ctm.phoneNumber = :phoneNumber ")
    CustomerDtoSafe getCustomerByPhoneNumber(@Param("phoneNumber") String phoneNumber);



    @Query("SELECT ctm.id as id from Customer ctm WHERE ctm.email = :email")
    long getCustomerIdFromEmail(@Param("email") String email );

    @Query("SELECT ctm.password as password from Customer ctm WHERE ctm.email = :email")
    String getCustomerPasswordFromEmail(@Param("email") String email );

    @Transactional
    @Modifying
    @Query("UPDATE Customer ctm SET ctm.tokenRegistration = :token WHERE ctm.email = :email")
    void setToken(@Param("token")String token, @Param("email") String email);

    @Query("SELECT MAX(ctm.id) FROM Customer ctm")
    Optional<Long> getCurrentId();


    Optional<Customer> findCustomerByEmail(String email);

    Optional<Customer> findCustomerByRole(CustomerRole role);

    @Query("SELECT new com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe" +
            "(ctm.surname, ctm.name, ctm.email, ctm.phoneNumber) From Customer ctm WHERE ctm.email = :email AND ctm.role !='ADMIN' " )
    Optional<CustomerDtoSafe> findCustomerDtoSafeByEmail(@Param("email") String email);

}
