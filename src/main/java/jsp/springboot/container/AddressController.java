package jsp.springboot.container;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Address;
import jsp.springboot.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	//Insert a record
	//t save(T ref)
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Address>>> getAllAddress(){
		return addressService.getAllAddress();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Address>> getAddressById(@PathVariable int id){
		return addressService.getAddressById(id);
	}
	
	@PutMapping
	public ResponseEntity<ResponseStructure<Address>> updateAddress(@RequestBody Address address){
		return addressService.updateAddress(address);
	}
	
	@GetMapping("/bank/{bankId}")
	public ResponseEntity<ResponseStructure<Address>> getAddressByBank(@PathVariable Integer bankId){
		return addressService.getAddressByBank(bankId);
	}
	
	@GetMapping("/city")	
	public ResponseEntity<ResponseStructure<Address>> getAddressByCity(@RequestParam String city){
		return addressService.getAddressByCity(city);
	}

	@GetMapping("/city-street")	
	public ResponseEntity<ResponseStructure<Address>> getAddressByCityAndStreet(@RequestParam String city,@RequestParam String street){
		return addressService.getAddressByCityAndStreet(city,street);
	}	
	
}
