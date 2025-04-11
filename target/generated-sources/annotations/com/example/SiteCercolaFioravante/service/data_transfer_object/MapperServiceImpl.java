package com.example.SiteCercolaFioravante.service.data_transfer_object;

import com.example.SiteCercolaFioravante.service.Service;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-10T20:44:26+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.14 (JetBrains s.r.o.)"
)
@Component
public class MapperServiceImpl implements MapperService {

    @Override
    public void ServiceDtoCompleteUploadToService(ServiceDtoCompleteUpload service, Service serviceDB) {
        if ( service == null ) {
            return;
        }

        if ( service.serviceName() != null ) {
            serviceDB.setServiceName( service.serviceName() );
        }
        serviceDB.setPrice( service.price() );
        if ( service.description() != null ) {
            serviceDB.setDescription( service.description() );
        }
    }

    @Override
    public void ServiceToServiceDtoComplete(Service serviceDB, ServiceDtoComplete service) {
        if ( serviceDB == null ) {
            return;
        }
    }
}
