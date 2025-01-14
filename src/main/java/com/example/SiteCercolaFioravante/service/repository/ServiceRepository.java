package com.example.SiteCercolaFioravante.service.repository;

import com.example.SiteCercolaFioravante.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository  extends JpaRepository<Service, Long > {

    @Query( "SELECT serv.serviceName FROM Service serv WHERE UPPER( serv.serviceName ) LIKE CONCAT('%', UPPER( :serviceName ), '%' ) " )
    List<String> getServiceByName(@Param( "serviceName" ) String serviceName );
}
