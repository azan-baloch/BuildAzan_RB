package com.buildazan.projection;

import java.time.LocalDateTime;

public interface UserExpirationTimeProjection {
    LocalDateTime getExpirationTime();
}
