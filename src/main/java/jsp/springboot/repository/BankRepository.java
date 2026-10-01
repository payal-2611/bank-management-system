package jsp.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jsp.springboot.entity.Address;
import jsp.springboot.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Integer> {

	Optional<Bank> findByIfsc(String ifsc);

	Optional<Bank> findByAddress(Address address);

	Optional<Bank> findByAddress_City(String city);

	boolean existsByContactNumber(long contactNumber);

	boolean existsByIfsc(String ifsc);

	Optional<Bank> findByContactNumber(long contactNumber);

	Optional<Bank> findBankByContactNumber(long contactNumber);

	Optional<Bank> findByBranchName(String branchName);
}
