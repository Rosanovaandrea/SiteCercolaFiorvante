package com.example.SiteCercolaFioravante.customer.repository;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerSafeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    List<CustomerDtoListProjection> getCustomerByNameOrSurname( String query );

    Optional<Customer> findCustomerByEmail( String email );

    Optional<Customer> findCustomerByRole( CustomerRole role );

    @Query("SELECT ctm.id as id, ctm.surname as surname, ctm.name as name, ctm.phoneNumber From Customer ctm WHERE ctm.id = :id AND ctm.role !='ADMIN' " )
    Optional<CustomerSafeProjection> findCustomerDtoSafeByID(@Param("id") Long id);
}
