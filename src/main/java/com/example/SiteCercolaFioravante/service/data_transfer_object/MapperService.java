package com.example.SiteCercolaFioravante.service.data_transfer_object;

import com.example.SiteCercolaFioravante.service.Service;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel ="spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapperService {
    void ServiceDtoCompleteUploadToService(ServiceDtoCompleteUpload service, @MappingTarget Service serviceDB);
    void ServiceToServiceDtoComplete(  Service serviceDB, @MappingTarget  ServiceDtoComplete service);
}
