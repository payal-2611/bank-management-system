package jsp.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Address;
import jsp.springboot.exception.NoRecordAvailableException;
import jsp.springboot.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	private AddressRepository addressRepository;

	//================Get All Address=====================================
	
	public ResponseEntity<ResponseStructure<List<Address>>> getAllAddress() {
		List<Address> addresses=addressRepository.findAll();
		
		if(addresses.isEmpty()) {
	        throw new NoRecordAvailableException("No Bank Records Available");
		}
		
		ResponseStructure<List<Address>> response=new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("All Address Records fetched Successfullly");
		response.setData(addresses);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	//=============================Get Address By Id==================================
	public ResponseEntity<ResponseStructure<Address>> getAddressById(int id) {
		Optional<Address> opt=addressRepository.findById(id);
		
		if(opt.isEmpty()) {
	        throw new NoRecordAvailableException("Address "+id+" not found");
		}
		ResponseStructure<Address> response=new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("All Address Record found!");
		response.setData(opt.get());
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	//==================Update Address====================
	
	public ResponseEntity<ResponseStructure<Address>> updateAddress(Address address) {

	    Optional<Address> existingAddress =addressRepository.findById(address.getAddressId());

	    if (existingAddress.isEmpty()) {
	        throw new NoRecordAvailableException("Address Id " + address.getAddressId() + " Not Found");
	    }

	    // Street validation
	    if (address.getStreet() == null || address.getStreet().trim().isEmpty()) {
	        throw new IllegalArgumentException("Street cannot be empty");
	    }

	    // City validation
	    if (address.getCity() == null|| address.getCity().trim().isEmpty()) {
	        throw new IllegalArgumentException("City cannot be empty");
	    }

	    // State validation
	    if (address.getState() == null|| address.getState().trim().isEmpty()) {
	        throw new IllegalArgumentException("State cannot be empty");
	    }

	    // Pincode validation
	    String pincode = String.valueOf(address.getPincode());

	    if (!pincode.matches("^[0-9]{6}$")) {
	        throw new IllegalArgumentException("Pincode should contain exactly 6 digits");
	    }

	    Address updatedAddress = addressRepository.save(address);

	    ResponseStructure<Address> response = new ResponseStructure<>();

	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Address Updated Successfully");
	    response.setData(updatedAddress);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	// ========================Get Address By Bank =========================
	public ResponseEntity<ResponseStructure<Address>> getAddressByBank(Integer bankId) {
		Optional<Address> op=addressRepository.findByBank_BankId(bankId);
		
		if(op.isEmpty()) {
			throw new NoRecordAvailableException("Address not found for Bank Id " + bankId);
		}
		
		  ResponseStructure<Address> response = new ResponseStructure<>();

		    response.setStatusCode(HttpStatus.OK.value());
		    response.setMessage("Address Found Successfully");
		    response.setData(op.get());

		    return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//=====================Get Address By city========================================
	public ResponseEntity<ResponseStructure<Address>> getAddressByCity(String city) {
		Optional<Address> op=addressRepository.findByCity(city);
		
		if(op.isEmpty()) {
			throw new NoRecordAvailableException("Address not found in city " + city);
		}
		
		  ResponseStructure<Address> response = new ResponseStructure<>();

		    response.setStatusCode(HttpStatus.OK.value());
		    response.setMessage("Address Found Successfully");
		    response.setData(op.get());

		    return new ResponseEntity<>(response, HttpStatus.OK);
	
	}
	//=====================Get Address By city and Street========================================
	public ResponseEntity<ResponseStructure<Address>> getAddressByCityAndStreet(String city, String street) {
		Optional<Address> op=addressRepository.findByCityAndStreet(city,street);
		
		if(op.isEmpty()) {
			throw new NoRecordAvailableException("Address not found in city  "+ city + "  and  "+street);
		}
		
		  ResponseStructure<Address> response = new ResponseStructure<>();

		    response.setStatusCode(HttpStatus.OK.value());
		    response.setMessage("Address Found Successfully");
		    response.setData(op.get());

		    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	//http://localhost:8080/address/city-street?city=Lucknow&street=Mall%20Road
	//%20 for space
	
	
}
