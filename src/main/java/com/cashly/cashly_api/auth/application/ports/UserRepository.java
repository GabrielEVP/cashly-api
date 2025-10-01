package com.cashly.cashly_api.auth.application.ports;

import com.cashly.cashly_api.auth.domain.entities.User;
import com.cashly.cashly_api.auth.domain.valueobjects.Email;
import com.cashly.cashly_api.auth.domain.valueobjects.UserId;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);

    void delete(User user);
}
