package com.example.SiteCercolaFioravante.customer.data_transfer_objects;

import com.example.SiteCercolaFioravante.customer.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-03T19:37:05+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.6 (JetBrains s.r.o.)"
)
@Component
public class MapperCustomerImpl implements MapperCustomer {

    @Override
    public void fromDtoEditAdminToCustomer(CustomerDtoEditAdmin customerDtoEditAdmin, Customer customer) {
        if ( customerDtoEditAdmin == null ) {
            return;
        }
    }

    @Override
    public void fromDtoCompleteToCustomer(CustomerDtoComplete customerDtoComplete, Customer customer) {
        if ( customerDtoComplete == null ) {
            return;
        }
    }

    @Override
    public void fromDtoSafeToCustomer(CustomerDtoSafe customerDtoSafe, Customer customer) {
        if ( customerDtoSafe == null ) {
            return;
        }
    }
}
