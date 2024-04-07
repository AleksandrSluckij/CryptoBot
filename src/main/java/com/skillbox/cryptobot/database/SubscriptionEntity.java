package com.skillbox.cryptobot.database;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
@NoArgsConstructor
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;
    @Column(name = "telegram_user_id", nullable = false)
    private Long userId;
    @Column(name = "subscription_value", nullable = true)
    private Double subscriptionValue;
    @Column(name = "last_notified_time")
    private Long lastNotified;
    public SubscriptionEntity(long userId) {
        this.userId = userId;
        this.lastNotified = System.currentTimeMillis() - 3_600_000;
    }

}
