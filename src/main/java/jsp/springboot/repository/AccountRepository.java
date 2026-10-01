package jsp.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jsp.springboot.entity.Account;
import jsp.springboot.enums.AccountType;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByAccNumber(long accNumber);

	List<Account> findByBankBankId(Integer bankId);

	List<Account> findByAccType(AccountType accType);

	List<Account> findByBalanceGreaterThan(double amount);
}