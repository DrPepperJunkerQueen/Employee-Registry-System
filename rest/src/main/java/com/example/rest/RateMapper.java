package com.example.rest;

import com.example.rest.RateDTO;
import com.example.rest.ClassEmployee;
import com.example.rest.Rate;

public class RateMapper {

    public static RateDTO toDto(Rate rate) {
        RateDTO dto = new RateDTO();
        dto.setId(rate.getId());
        dto.setGrade(rate.getGrade());
        dto.setDate(rate.getDate());
        dto.setComment(rate.getComment());
        dto.setGroupId(rate.getGroup().getId());
        return dto;
    }

    public static Rate toEntity(RateDTO dto, ClassEmployee group) {
        return new Rate(dto.getGrade(), group, dto.getDate(), dto.getComment());
    }
}
