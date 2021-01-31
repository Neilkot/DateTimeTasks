package com.epam.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Kostiantyn Morozov on 31.01.2021
 */
public class DateTimeTasks {

  private static final LocalDate STARTING_YEAR = LocalDate.of(2020, 01, 01);

  List<String> fridays13() {
    List<String> dates = new ArrayList<>();

    for (LocalDate start = STARTING_YEAR; start.isBefore(getTodayDate());
        start = start.plusDays(1)) {
      if (start.getDayOfMonth() == 13 && start.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
        String formattedDate = start.format(DateTimeFormatter.ofPattern("MMM yyyy"));
        dates.add(formattedDate);
      }
    }
    return dates;
  }

  public LocalDate getTodayDate(){
    return LocalDate.now();
  }

  List<YearMonth> endOnSundays() {
    List<YearMonth> dates = new ArrayList<>();

    for (YearMonth start = YearMonth.from(STARTING_YEAR); start.isBefore(YearMonth.now());
        start = start.plusMonths(1)) {
      int i = start.lengthOfMonth();
      if (LocalDate.of(start.getYear(), start.getMonth(), i).getDayOfWeek()
          .equals(DayOfWeek.SUNDAY)) {
        dates.add(start);
      }
    }
    return dates;
  }

  List<Year> birthdaysOnSaturdays(LocalDate birthday) {
    if(birthday == null || birthday.isAfter(LocalDate.now())){
      throw new IllegalArgumentException("Birthdate must be before today's date");
    }

    List<Year> years = new ArrayList<>();

    for (LocalDate start = birthday; start.isBefore(LocalDate.now()); start = start.plusYears(1)) {
      if (start.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
        years.add(Year.of(start.getYear()));
      }
    }
    return years;
  }

  List<MonthDay> daysNotWith24Hours(Year year) {
    List<MonthDay> monthDays = new ArrayList<>();

    LocalDate startDate = LocalDate.of(year.getValue(), 01, 01);
    for (LocalDate start = startDate; start.isBefore(startDate.plusYears(1));
        start = start.plusDays(1)) {
      ZonedDateTime zonedDateTimeStart = start.atStartOfDay()
          .atZone(TimeZone.getDefault().toZoneId());
      ZonedDateTime zonedDateTimeEnd = start.plusDays(1).atStartOfDay()
          .atZone(TimeZone.getDefault().toZoneId());
      int counter = 0;
      for (ZonedDateTime currentDay = zonedDateTimeStart; currentDay.isBefore(zonedDateTimeEnd);
          currentDay = currentDay.plusHours(1)) {
        counter++;
      }
      if (counter != 24) {
        monthDays.add(MonthDay.from(start));
      }
    }
    return monthDays;
  }
}
