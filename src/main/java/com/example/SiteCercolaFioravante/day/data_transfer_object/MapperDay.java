package com.example.SiteCercolaFioravante.day.data_transfer_object;

import com.example.SiteCercolaFioravante.day.Day;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,componentModel = "spring")
public interface MapperDay {
    void DayDtoCompleteToDay(CalendarDtoSingleComplete dayDto, @MappingTarget Day dayDB);
}
