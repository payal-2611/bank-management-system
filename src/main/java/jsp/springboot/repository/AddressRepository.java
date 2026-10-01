package jsp.springboot.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import jsp.springboot.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{

	Optional<Address> findByBank_BankId(Integer bankId);

	Optional<Address> findByCity(String city);

	Optional<Address> findByCityAndStreet(String city, String street);



}
