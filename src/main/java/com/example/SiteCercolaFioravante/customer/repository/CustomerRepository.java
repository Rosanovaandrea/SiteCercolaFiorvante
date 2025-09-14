package com.example.SiteCercolaFioravante.customer.repository;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerSafeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @NativeQuery("(SELECT ctm.id as id, ctm.surname as surname, ctm.name as name  FROM customer ctm WHERE (LOWER(ctm.name) LIKE CONCAT(LOWER(:query), '%') AND ctm.role != 'ADMIN') LIMIT 3)" + " UNION " +
            "(SELECT ctm.id as id, ctm.surname as surname, ctm.name as name  FROM customer ctm WHERE (LOWER(ctm.surname) LIKE CONCAT(LOWER(:query), '%') AND ctm.role != 'ADMIN') LIMIT 3)" + " UNION " +
            "(SELECT ctm.id as id, ctm.surname as surname, ctm.name as name  FROM customer ctm WHERE (ctm.phone_number  LIKE CONCAT( :query, '%') AND ctm.role != 'ADMIN') LIMIT 3)"
    )
    List<CustomerDtoListProjection> getCustomerByNameOrSurname(@Param("query") String query);

    @Query("SELECT c FROM Customer c JOIN FETCH c.credentials WHERE c.credentials.customerId = :customerId")
    Optional<Customer> findCustomerByEmail(@Param("customerId") String email );

    Optional<Customer> findCustomerByRole( CustomerRole role );

    @Query("SELECT ctm.id as id, ctm.surname as surname, ctm.name as name, ctm.phoneNumber From Customer ctm WHERE ctm.id = :id AND ctm.role !='ADMIN' " )
    Optional<CustomerSafeProjection> findCustomerDtoSafeByID(@Param("id") Long id);
}
