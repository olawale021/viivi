package com.example.viivi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.viivi.models.userActivity.UserActivityModel;
import com.example.viivi.models.userActivity.UserActivityRepository;
import com.example.viivi.models.users.UserModel;
import com.example.viivi.models.users.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActivityService {

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Save user activity with relevant filters, preferences, and activity data
     */
    public void saveUserActivity(Long userId, Long productId, String activityType, Long productCategoryId,
                                 Double minPriceFilter, Double maxPriceFilter, Long categoryFilter,
                                 Double activityDuration, String searchFilter) {

        // Create a new UserActivityModel instance
        UserActivityModel activity = new UserActivityModel();

        // Fetch user by userId
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Set the user and activity information
        activity.setUserId(userId);
        activity.setProductId(productId);
        activity.setActivityType(activityType);
        activity.setActivityTimestamp(LocalDateTime.now());

        // Set product category if available
        if (productCategoryId != null) {
            activity.setProductCategoryId(productCategoryId);
        }

        // Set filters: minPriceFilter and maxPriceFilter
        activity.setMinPriceFilter(minPriceFilter);
        activity.setMaxPriceFilter(maxPriceFilter);

        // Set category filter
        activity.setCategoryFilter(categoryFilter);

        // Set the duration of the activity (session length)
        if (activityDuration != null) {
            activity.setActivityDuration(activityDuration.intValue());
        }

        // Set the search filter if available
        if (searchFilter != null) {
            activity.setSearchFilter(searchFilter);
        }

        System.out.println("Min Price Filter: " + minPriceFilter);
        System.out.println("Max Price Filter: " + maxPriceFilter);


        // Retrieve the user's top category IDs from the UserModel and set in UserActivityModel
        activity.setTopCategory1(user.getTopCategory1() != null ? Long.valueOf(user.getTopCategory1()) : null);
        activity.setTopCategory2(user.getTopCategory2() != null ? Long.valueOf(user.getTopCategory2()) : null);
        activity.setTopCategory3(user.getTopCategory3() != null ? Long.valueOf(user.getTopCategory3()) : null);

        // Save the activity to the database
        userActivityRepository.save(activity);
    }
}
