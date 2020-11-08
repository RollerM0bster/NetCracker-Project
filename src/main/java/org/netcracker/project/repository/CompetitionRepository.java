package org.netcracker.project.repository;

import org.netcracker.project.model.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface CompetitionRepository extends JpaRepository<Competition,Long> {
    Competition findByCompName(String compName);
    // @Query(...)
    Competition queryByStartDate(LocalDateTime startDate);

    Page<Competition> findAll(Pageable pageable);

    @Query("from Competition c where lower(c.description) like lower(:search) or lower(c.compName) like lower(:search)")
    Page<Competition> findAllBySearch(Pageable pageable, String search);

    Page<Competition> findAllByStartDateAfter(Pageable pageable, LocalDateTime startDate);

    Page<Competition> findAllByStartDateBefore(Pageable pageable, LocalDateTime startDate);

    @Query("from Competition c where c.startDate = :startDate and (lower(c.description) like lower(:search) or lower(c.compName) like lower(:search))")
    Page<Competition> findAllByStartDateEquals(Pageable pageable, LocalDateTime startDate, String search);

    @Query("from Competition c where c.startDate = :startDate and (lower(c.description) like lower(:search) or lower(c.compName) like lower(:search))")
    Page<Competition> findAllByEndDateEquals(Pageable pageable, LocalDateTime startDate, String search);

    @Query("from Competition c where c.startDate = :startDate and c.endDate = :endDate and (lower(c.description) like lower(:search) or lower(c.compName) like lower(:search))")
    Page<Competition> findAllByStartDateEqualsAndEndDateEquals(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, String search);

    @Query("from Competition c where c.startDate <= :beforeStart and c.startDate >= :afterStart and c.endDate <= :beforeEnd and c.endDate >= :afterEnd and (lower(c.description) like lower(:search) or lower(c.compName) like lower(:search))")
    Page<Competition> findAllByBounds(Pageable pageable, LocalDateTime beforeStart, LocalDateTime afterStart, LocalDateTime beforeEnd, LocalDateTime afterEnd, String search);
}
