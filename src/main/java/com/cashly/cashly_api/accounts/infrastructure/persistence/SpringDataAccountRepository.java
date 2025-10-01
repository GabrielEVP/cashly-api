package com.cashly.cashly_api.accounts.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataAccountRepository extends JpaRepository<AccountEntity, String> {
    List<AccountEntity> findByUserId(String userId);
}
