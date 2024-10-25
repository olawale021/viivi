package com.example.viivi.models.userActivity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivityModel, Long> {
    List<UserActivityModel> findByUserId(Long userId);


}
