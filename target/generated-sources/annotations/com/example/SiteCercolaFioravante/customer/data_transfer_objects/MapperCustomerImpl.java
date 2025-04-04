package com.example.SiteCercolaFioravante.customer.data_transfer_objects;

import com.example.SiteCercolaFioravante.customer.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-04T17:39:24+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.6 (JetBrains s.r.o.)"
)
@Component
public class MapperCustomerImpl implements MapperCustomer {

    @Override
    public void fromDtoEditAdminToCustomer(CustomerDtoEditAdmin customerDtoEditAdmin, Customer customer) {
        if ( customerDtoEditAdmin == null ) {
            return;
        }

        if ( customerDtoEditAdmin.surname() != null ) {
            customer.setSurname( customerDtoEditAdmin.surname() );
        }
        if ( customerDtoEditAdmin.name() != null ) {
            customer.setName( customerDtoEditAdmin.name() );
        }
        if ( customerDtoEditAdmin.email() != null ) {
            customer.setEmail( customerDtoEditAdmin.email() );
        }
        if ( customerDtoEditAdmin.role() != null ) {
            customer.setRole( customerDtoEditAdmin.role() );
        }
        if ( customerDtoEditAdmin.phoneNumber() != null ) {
            customer.setPhoneNumber( customerDtoEditAdmin.phoneNumber() );
        }
    }

    @Override
    public Customer fromDtoCompleteToCustomer(CustomerDtoComplete customerDtoComplete) {
        if ( customerDtoComplete == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( customerDtoComplete.id() );
        customer.setSurname( customerDtoComplete.surname() );
        customer.setName( customerDtoComplete.name() );
        customer.setPassword( customerDtoComplete.password() );
        customer.setEmail( customerDtoComplete.email() );
        customer.setPhoneNumber( customerDtoComplete.phoneNumber() );

        return customer;
    }

    @Override
    public Customer fromDtoSafeToCustomer(CustomerDtoSafe customerDtoSafe) {
        if ( customerDtoSafe == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setSurname( customerDtoSafe.surname() );
        customer.setName( customerDtoSafe.name() );
        customer.setEmail( customerDtoSafe.email() );
        customer.setPhoneNumber( customerDtoSafe.phoneNumber() );

        return customer;
    }
}
