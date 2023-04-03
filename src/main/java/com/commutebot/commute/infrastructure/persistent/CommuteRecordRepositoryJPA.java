package com.commutebot.commute.infrastructure.persistent;

import com.commutebot.commute.domain.CommuteRecord;
import com.commutebot.commute.domain.CommuteRecordRepository;
import com.commutebot.commute.dto.projection.DailyReportProjection;
import com.commutebot.commute.dto.projection.MonthlyReportProjection;
import com.commutebot.commute.dto.projection.WeeklyReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CommuteRecordRepositoryJPA extends JpaRepository<CommuteRecord, Integer>, CommuteRecordRepository {

    // Save Commute Data
    default void store(final CommuteRecord commuteRecord){
        save(commuteRecord);
    }


    // Check Arrive Record
    default boolean checkAlreadyCommuteRecord(String username, LocalDate date){
        return existsByUsernameAndDate(username, date);
    }
    boolean existsByUsernameAndDate(String username, LocalDate date);


    // Get Today Commute Record
    default CommuteRecord findTodayCommuteRecord(String username, LocalDate date) {
        return findByUsernameAndDate(username, date);
    }
    CommuteRecord findByUsernameAndDate(String username, LocalDate date);


    // Find Didn't Check Members To Leave
    @Override
    default List<CommuteRecord> findNotLeaveMembers(LocalDate date) {
        return findAllByDateAndStatus(date, "근무");
    }
    List<CommuteRecord> findAllByDateAndStatus(LocalDate date, String status);


    // Get Yesterday Member Commute Report For Leader
    @Override
    default List<CommuteRecord> getYesterdayCommuteRecord(LocalDate date, int teamCode){
        return findByDateAndTeamCode(date, teamCode);
    }
    List<CommuteRecord> findByDateAndTeamCode(LocalDate date, int teamCode);


    // Get Daily Analysis Report
    @Override
    default List<DailyReportProjection> getDailyCommuteAnalysisReport(String username, int startDate, int lastDate){
        return getDailyReport(username, startDate, lastDate);
    }
    @Query(nativeQuery = true, value = "SELECT DATE(date) AS `date`, \n" +
                                        "       `check_in` AS `checkIn`, \n" +
                                        "       `check_out` AS `checkOut`, \n" +
                                        "       `worked_minute` AS `dailyWorkedTime` \n" +
                                        "FROM commute_record \n" +
                                        "WHERE `date` BETWEEN :startDate AND :lastDate AND username  = :username " +
                                        "GROUP BY `date`;")
    List<DailyReportProjection> getDailyReport(@Param("username") String username, @Param("startDate") int startDate, @Param("lastDate") int lastDate);


    // Get Weekly Analysis Report
    @Override
    default List<WeeklyReportProjection> getWeeklyCommuteAnalysisReport(String username, int startDate, int lastDate){
        return getWeeklyReport(username, startDate, lastDate);
    }
    @Query(nativeQuery = true, value = "SELECT CONCAT(DATE_FORMAT(DATE_ADD(`date` , INTERVAL(1-DAYOFWEEK(`date`)) DAY),\"%Y-%m-%d\"),\n" +
                                                "\" ~ \", \n" +
                                                "DATE_FORMAT(DATE_ADD(`date` , INTERVAL(7-DAYOFWEEK(`date`)) DAY),\"%Y-%m-%d\")) AS `dateRange`, \n" +
                                                "SUM(worked_minute) AS `weeklyWorkedTime`, \n" +
                                                "AVG(worked_minute) AS `weeklyWorkedTimeAverage` \n" +
                                        "FROM commute_record \n" +
                                        "WHERE `date` BETWEEN :startDate AND :lastDate AND username = :username \n" +
                                        "GROUP BY CONCAT(YEAR(`date`), \"/\", WEEK(`date`));")
    List<WeeklyReportProjection> getWeeklyReport(@Param("username") String username, @Param("startDate") int startDate, @Param("lastDate") int lastDate);


    // Get Monthly Analysis Report
    @Override
    default List<MonthlyReportProjection> getMonthlyCommuteAnalysisReport(String username, int startDate, int lastDate){
        return getMonthlyReport(username, startDate, lastDate);
    }
    @Query(nativeQuery = true, value = "SELECT MONTH(`date`) AS `month`,\n" +
                                                "SUM(worked_minute) AS `monthlyWorkedTime`, \n" +
                                                "AVG(worked_minute) AS `monthlyWorkedTimeAverage` \n" +
                                        "FROM commute_record \n" +
                                        "WHERE `date` BETWEEN :startDate AND :lastDate AND username = :username")
    List<MonthlyReportProjection> getMonthlyReport(@Param("username") String username, @Param("startDate") int startDate, @Param("lastDate") int lastDate);
}

