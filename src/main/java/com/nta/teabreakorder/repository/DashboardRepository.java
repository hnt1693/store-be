//package com.nta.teabreakorder.repository;
//
//import com.nta.teabreakorder.model.Dashboard;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//
//@Repository
//public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
//
//    List<Dashboard> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
//
//    Dashboard findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
//
//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true,
//            value = "Delete from hourly_summary h where h.period_id>=? ")
//    void deleteHourlySummaryAfterPeriodTime(long period);
//
//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true,
//            value = "Delete from daily_summary h where h.period_id>=? ")
//    void deleteDailySummaryAfterPeriodTime(long period);
//
//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true,
//            value = "Delete from monthly_summary h where h.period_id>=? ")
//    void deleteMonthlySummaryAfterPeriodTime(long period);
//
//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true,
//            value = "Delete from yearly_summary h where h.period_id>=? ")
//    void deleteYearlySummaryAfterPeriodTime(long period);
//
//}
