package com.example.SiteCercolaFioravante.customer.repository;

import com.example.SiteCercolaFioravante.customer.Customer;
import com.example.SiteCercolaFioravante.customer.CustomerRole;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerDtoListProjection;
import com.example.SiteCercolaFioravante.customer.data_transfer_objects.CustomerSafeProjection;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Profile("test")
@Repository
public interface CustomerRepositoryH2 extends CustomerRepository {

    @NativeQuery("(SELECT ctm.id as id, ctm.surname as surname, ctm.name as name  FROM customer ctm WHERE (ctm.name LIKE CONCAT(LOWER(:query), '%') AND ctm.role != 'ADMIN') LIMIT 3)" + " UNION " +
                 "(SELECT ctm.id as id, ctm.surname as surname, ctm.name as name  FROM customer ctm WHERE (ctm.surname LIKE CONCAT(LOWER(:query), '%') AND ctm.role != 'ADMIN') LIMIT 3)" + " UNION " +
                 "(SELECT ctm.id as id, ctm.surname as surname, ctm.name as name  FROM customer ctm WHERE (ctm.phone_number  LIKE CONCAT( :query, '%') AND ctm.role != 'ADMIN') LIMIT 3)"
                 )
    List<CustomerDtoListProjection> getCustomerByNameOrSurname(@Param("query") String query);

}
