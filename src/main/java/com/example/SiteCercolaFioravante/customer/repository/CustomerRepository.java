package com.example.SiteCercolaFioravante.customer.repository;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoSafe;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerSafeProjection;
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

    //query ottimizzata per utilizzare gli indici funzionali
    @NativeQuery("(SELECT ctm.id as id, ctm.surname as surname, ctm.name as name  FROM customer ctm WHERE ctm.role != 'ADMIN' AND  ctm.name_lower LIKE CONCAT(LOWER(:query), '%')LIMIT 3)" + " UNION " +
                 "(SELECT ctm.id as id, ctm.surname as surname, ctm.name as name  FROM customer ctm WHERE ctm.role != 'ADMIN' AND ctm.surname_lower LIKE CONCAT(LOWER(:query), '%') LIMIT 3)" + " UNION " +
                 "(SELECT ctm.id as id, ctm.surname as surname, ctm.name as name  FROM customer ctm WHERE ctm.role != 'ADMIN' AND ctm.phone_number  LIKE CONCAT( :query, '%') LIMIT 3)"
                 )
    List<CustomerDtoListProjection> getCustomerByNameOrSurname(@Param("query") String query);

    Optional<Customer> findCustomerByEmail(String email);

    Optional<Customer> findCustomerByRole(CustomerRole role);

    @Query("SELECT ctm.id as id, ctm.surname as surname, ctm.name as name, ctm.phoneNumber From Customer ctm WHERE ctm.id = :id AND ctm.role !='ADMIN' " )
    Optional<CustomerSafeProjection> findCustomerDtoSafeByID(@Param("id") Long id);

}
