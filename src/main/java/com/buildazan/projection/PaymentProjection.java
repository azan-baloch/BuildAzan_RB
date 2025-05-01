package com.buildazan.projection;

import java.time.LocalDate;

import com.buildazan.enums.MemberShipLevel;
import com.buildazan.enums.SubscriptionStatus;

public interface PaymentProjection {
    SubscriptionStatus getSubscriptionStatus();
    MemberShipLevel getMemberShipLevel();
    LocalDate getSubscriptionStartDate();
    LocalDate getSubscriptionEndDate();
}
