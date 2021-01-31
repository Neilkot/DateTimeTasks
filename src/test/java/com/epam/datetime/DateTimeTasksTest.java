package com.epam.datetime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by Kostiantyn Morozov on 31.01.2021
 */
@RunWith(MockitoJUnitRunner.class)
public class DateTimeTasksTest {

  private static final LocalDate BIRTHDAY = LocalDate.of(1897, 12, 24);
  private static final Year YEAR = Year.of(2020);

  @Spy
  private DateTimeTasks task;

  @Before
  public void beforeEach() {
    MockitoAnnotations.initMocks(this);

  }

  @Test
  public void shouldReturnListWithFriday13MonthAndYearSinceProvidedDate() {
    when(task.getTodayDate()).thenReturn(LocalDate.of(2021, 01, 30));

    List<String> expected = Arrays.asList("Mar 2020", "Nov 2020");
    List<String> actual = task.fridays13();

    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnListWithYearMonthWhereLastDayOfMonthIsSunday() {
    List<YearMonth> expected = Arrays.asList(YearMonth.of(2020, 05));
    List<YearMonth> actual = task.endOnSundays();

    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnListOfYearsWhereInputDateDayIsSaturday() {
    List<Year> actual = task.birthdaysOnSaturdays(BIRTHDAY);

    Optional<LocalDate> first = actual.stream()
        .map(year -> LocalDate
            .of(year.getValue(), BIRTHDAY.getMonth(), BIRTHDAY.getDayOfMonth()))
        .filter(date -> !date.getDayOfWeek().equals(DayOfWeek.SATURDAY))
        .findFirst();
    assertFalse(first.isPresent());
  }

  @Test
  public void shouldReturnDaysWhereNot24HoursInADay() {
    List<MonthDay> actual = task.daysNotWith24Hours(YEAR);

    boolean test = actual.stream()
        .map(year -> LocalDate.of(YEAR.getValue(), year.getMonthValue(), year.getDayOfMonth()))
        .allMatch(isNot24Hours());

    assertTrue(test);
  }

  private Predicate<LocalDate> isNot24Hours() {
    return p -> processDateTime(p) != 24;
  }

  private int processDateTime(LocalDate start) {
    ZonedDateTime zonedDateTimeStart = start.atStartOfDay()
        .atZone(TimeZone.getDefault().toZoneId());
    ZonedDateTime zonedDateTimeEnd = start.plusDays(1).atStartOfDay()
        .atZone(TimeZone.getDefault().toZoneId());
    int counter = 0;
    for (ZonedDateTime currentDay = zonedDateTimeStart; currentDay.isBefore(zonedDateTimeEnd);
        currentDay = currentDay.plusHours(1)) {
      counter++;
    }
    return counter;
  }

}