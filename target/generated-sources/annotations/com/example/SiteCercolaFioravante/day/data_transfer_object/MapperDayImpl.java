package com.example.SiteCercolaFioravante.day.data_transfer_object;

import com.example.SiteCercolaFioravante.day.Day;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-03T19:37:05+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.6 (JetBrains s.r.o.)"
)
@Component
public class MapperDayImpl implements MapperDay {

    @Override
    public void DayDtoCompleteToDay(CalendarDtoSingleComplete dayDto, Day dayDB) {
        if ( dayDto == null ) {
            return;
        }
    }
}
