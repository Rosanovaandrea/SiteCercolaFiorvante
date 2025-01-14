package com.example.SiteCercolaFioravante.reservation.repository;

import java.util.Date;

public interface ReservationProjection {
    Date getDate();
    String getServiceName();

    String getName();

    String getSurname();

    int getHour();

    long getId();

}
