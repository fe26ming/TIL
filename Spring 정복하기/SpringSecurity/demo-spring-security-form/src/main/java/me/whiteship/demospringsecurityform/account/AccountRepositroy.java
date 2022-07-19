package me.whiteship.demospringsecurityform.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepositroy extends JpaRepository<Account, Integer> {

    Account findByUsername(String username);
}
