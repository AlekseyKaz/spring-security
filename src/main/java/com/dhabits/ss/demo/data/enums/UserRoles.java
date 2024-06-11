package com.dhabits.ss.demo.data.enums;

import com.dhabits.ss.demo.data.entity.UserActionsEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.function.Function;

@Getter
public enum UserRoles implements GrantedAuthority {
    REGULAR(UserActionsEntity::isRegular),
    ADMIN(UserActionsEntity::isAdmin);
    private final Function<UserActionsEntity, Boolean> hasAuthority;

    UserRoles(Function<UserActionsEntity, Boolean> hasAuthority) {
        this.hasAuthority = hasAuthority;
    }

    public boolean hasAuthority(UserActionsEntity entity) {
        return hasAuthority.apply(entity);
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
