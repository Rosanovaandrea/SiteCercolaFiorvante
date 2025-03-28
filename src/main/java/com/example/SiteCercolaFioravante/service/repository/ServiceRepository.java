package com.example.SiteCercolaFioravante.service.repository;

import com.example.SiteCercolaFioravante.service.Service;
import com.example.SiteCercolaFioravante.service.data_transfer_object.ServiceDtoComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository  extends JpaRepository<Service, Long > {

    @Query( "SELECT serv.serviceName as serviceName FROM Service serv WHERE UPPER( serv.serviceName ) LIKE CONCAT('%', UPPER( :serviceName ), '%' ) " )
    List<String> getServiceByName(@Param( "serviceName" ) String serviceName );

    @Query( "SELECT serv.serviceName as serviceName FROM Service serv " )
    List<String> getServiceListByNames();

    @Query("SELECT serv FROM Service serv WHERE serv.serviceName = :serviceName")
    Service getServiceDtoCompleteByName( @Param( "serviceName" ) String serviceName);

    @Query("SELECT serv FROM Service serv WHERE serv.serviceName = :serviceName")
    Service getServiceDbByName(@Param( "serviceName" ) String serviceName );
}
