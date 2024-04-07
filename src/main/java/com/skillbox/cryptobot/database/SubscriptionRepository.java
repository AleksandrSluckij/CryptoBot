package com.skillbox.cryptobot.database;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    @Query(value = "SELECT s.* FROM subscriptions s WHERE s.telegram_user_id = ?1", nativeQuery = true)
    SubscriptionEntity findByUserIdEquals(long telegramUserId);

    @Query(value = "SELECT s.subscription_value FROM subscriptions s WHERE s.telegram_user_id = ?1", nativeQuery = true)
    Double findSubscriptionValue(long telegramUserId);

    @Query(value = "SELECT s.* FROM subscriptions s WHERE (s.subscription_value - ?1) > 0.0001 "
        + "AND s.last_notified_time < ?2", nativeQuery = true)
    List<SubscriptionEntity> findUsersToNotify(double price, long timeToNotify);

    @Modifying
    @Transactional
    @Query(value = "UPDATE subscriptions SET subscription_value = ?2 WHERE telegram_user_id = ?1", nativeQuery = true)
    void updateSubscriptionValue(Long userId, Double subscriptionValue);
}
