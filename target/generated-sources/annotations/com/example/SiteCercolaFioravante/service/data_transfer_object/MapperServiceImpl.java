package com.example.SiteCercolaFioravante.service.data_transfer_object;

import com.example.SiteCercolaFioravante.service.Service;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-03T19:37:05+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.6 (JetBrains s.r.o.)"
)
@Component
public class MapperServiceImpl implements MapperService {

    @Override
    public void ServiceDtoCompleteUploadToService(ServiceDtoCompleteUpload service, Service serviceDB) {
        if ( service == null ) {
            return;
        }
    }

    @Override
    public void ServiceToServiceDtoComplete(Service serviceDB, ServiceDtoComplete service) {
        if ( serviceDB == null ) {
            return;
        }
    }
}
