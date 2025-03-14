package com.example.SiteCercolaFioravante.customer.data_transfer_objects;

import com.example.SiteCercolaFioravante.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,componentModel = "spring")
public interface MapperCustomer {
    void fromDtoEditAdminToCustomer(CustomerDtoEditAdmin customerDtoEditAdmin, @MappingTarget Customer customer);
    void fromDtoCompleteToCustomer(CustomerDtoComplete customerDtoComplete, @MappingTarget Customer customer);
    void fromDtoSafeToCustomer(CustomerDtoSafe customerDtoSafe, @MappingTarget Customer customer);


}
