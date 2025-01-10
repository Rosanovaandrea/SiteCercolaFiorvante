package com.example.SiteCercolaFioravante.reservation;

import java.util.Date;

public interface ReservationProjection {
    Date getDate();
    String getServiceName();

    String getName();

    String getSurname();

    boolean getIsCompleted();

    boolean getIsDeletable();

    int getHour();

}
