package project.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN", "어드민");
    
    private final String key;
    private final String title;
}