package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statitics {
    StatiticsbyDay statiticsbyDay;
    StatiticsbyWeek statiticsbyWeek;
    StatiticsbyMonth statiticsbyMonth;
    Integer pendingorderamount;

}
