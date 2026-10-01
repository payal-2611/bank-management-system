package jsp.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Address;
import jsp.springboot.entity.Bank;
import jsp.springboot.exception.NoRecordAvailableException;
import jsp.springboot.repository.BankRepository;

@Service
public class BankService {
	
	@Autowired
	private BankRepository bankRepository;
	
    // ================= CREATE BANK =================

	 public ResponseEntity<ResponseStructure<Bank>> createBank(Bank bank) {

		// 1.Contact Number Validation
		 String contact = String.valueOf(bank.getContactNumber());

		 if(!contact.matches("^[0-9]{10}$")){
		     throw new IllegalArgumentException("Contact number should contain exactly 10 digits");
		 }
	        	
	     // Contact Number must be unique

		 if(bankRepository.existsByContactNumber(bank.getContactNumber())){
			    throw new IllegalArgumentException("Contact number already exists");
		 }
	      
		 //2.IFSC code validation
		 if(bank.getIfsc()==null || !bank.getIfsc().matches("^[A-Z]{4}0[A-Z0-9]{6}$")){
			    throw new IllegalArgumentException("Invalid IFSC Code");
		 }
		 
		 // IFSC must be unique
		 if(bankRepository.existsByIfsc(bank.getIfsc())){
			    throw new IllegalArgumentException("IFSC already exists");
		 }
		 
		 //3.Bank name validation
		 if(bank.getBankName()==null || bank.getBankName().trim().isEmpty()){
			    throw new IllegalArgumentException("Bank Name cannot be empty");
		 }
		 if(!bank.getBankName().matches("[A-Za-z ]+")){
			    throw new IllegalArgumentException("Bank name should contain only alphabets");
		 }
		 
		 // 4. Address Validation
		 if(bank.getAddress()==null) {
			 throw new IllegalArgumentException("Bank must have an address");
		 }
		 
		 //Save bank
	     Bank savedBank = bankRepository.save(bank);

	     //Response
	        ResponseStructure<Bank> response = new ResponseStructure<>();
	        response.setStatusCode(HttpStatus.CREATED.value());
	        response.setMessage("Bank Created Successfully");
	        response.setData(savedBank);

	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    }
	 
	  // ================= GET ALL BANK =================

	 public ResponseEntity<ResponseStructure<List<Bank>>> getAllBank() {

		    List<Bank> banks = bankRepository.findAll();

		    if (banks.isEmpty()) {
		        throw new NoRecordAvailableException("No Bank Records Available");
		    }

		    ResponseStructure<List<Bank>> response = new ResponseStructure<>();
		    response.setStatusCode(HttpStatus.OK.value());
		    response.setMessage("All Bank Records Fetched Successfully");
		    response.setData(banks);

		    return new ResponseEntity<>(response, HttpStatus.OK);
		}
	 

	 // ================= GET BANK BY ID =================

	 public ResponseEntity<ResponseStructure<Bank>> getBankById(int id) {

	     Optional<Bank> bank = bankRepository.findById(id);
	     
	     if(bank.isEmpty()) {
	    	 throw new NoRecordAvailableException("No Bank Records Available");
	     }

	     ResponseStructure<Bank> response = new ResponseStructure<>();
	     response.setStatusCode(HttpStatus.OK.value());
	     response.setMessage("Bank Record Found Successfully");
	     response.setData(bank.get());

	     return new ResponseEntity<>(response, HttpStatus.OK);
	 }
	 
	// ========================Delete Bank===========================
	 
	 public ResponseEntity<ResponseStructure<String>> deleteBank(int id) {

	     Optional<Bank> bank = bankRepository.findById(id);

	     if (bank.isEmpty()) {
	         throw new NoRecordAvailableException("Bank Id " + id + " Not Found");
	     }

	     bankRepository.deleteById(id);

	     ResponseStructure<String> response = new ResponseStructure<>();
	     response.setStatusCode(HttpStatus.OK.value());
	     response.setMessage("Bank Deleted Successfully");
	     response.setData("Bank with id " + id + " deleted");

	     return new ResponseEntity<>(response, HttpStatus.OK);
	 }
	 
	 // ================= UPDATE BANK =================

	 public ResponseEntity<ResponseStructure<Bank>> updateBank(Bank bank) {

	     // Check whether bank exists
	     Optional<Bank> existingBank = bankRepository.findById(bank.getBankId());

	     if (existingBank.isEmpty()) {
	         throw new NoRecordAvailableException("Bank Id " + bank.getBankId() + " Not Found");
	     }
	    
	     // 1. Contact Number Validation
	     String contact=String.valueOf(bank.getContactNumber());
	     
	     if(!contact.matches("[0-9]{10}$")) {
	    	 	throw new IllegalArgumentException("Contact no. should contain exactly 10 digits");
	     }
	     
	     // Check duplicate contact number
	     Optional<Bank> bankWithContact=bankRepository.findByContactNumber(bank.getContactNumber());
	     
	     if(bankWithContact.isPresent() && bankWithContact.get().getBankId()!=bank.getBankId()) {
	    	 	throw new IllegalArgumentException("Contact number already exists");
	     }
	     
	     //2. IFSC validation
	     if(bank.getIfsc()==null || !bank.getIfsc().matches("[A-Z]{4}0[A-Z0-9]{6}$")) {
	    	 	throw new IllegalArgumentException("Invalid IFSC Code");
	     }
	     
	     //Check duplicate IFSC 
	     Optional<Bank> bankWithIfsc=bankRepository.findByIfsc(bank.getIfsc());
	     
	     if(bankWithIfsc.isPresent() && bankWithIfsc.get().getBankId()!=bank.getBankId()){
			    throw new IllegalArgumentException("IFSC already exists");
		 }
	     
	     //3. Bank name Validation
	     if(bank.getBankName()==null||bank.getBankName().trim().isEmpty()) {
	    	 	throw new IllegalArgumentException("Bank Name cannot be empty");
	     }
	     
	     if (!bank.getBankName().matches("[A-Za-z ]+")) {
	            throw new IllegalArgumentException("Bank name should contain only alphabets");
	     }
	     
	     //4.Address Validation
	     if(bank.getAddress()==null) {
	    	 	throw new IllegalArgumentException("Bank must have an address");
	     }
	     
	     //Update
	     Bank updatedBank = bankRepository.save(bank);

	     //Response
	     ResponseStructure<Bank> response = new ResponseStructure<>();
	     response.setStatusCode(HttpStatus.OK.value());
	     response.setMessage("Bank Updated Successfully");
	     response.setData(updatedBank);

	     return new ResponseEntity<>(response, HttpStatus.OK);
	 }
	 
	 
	 // ================= PAGINATION + SORTING =================	 
	 public ResponseEntity<ResponseStructure<Page<Bank>>> getBankByPaginationAndSorting(
	         int pageNumber, int pageSize, String field) {

	     PageRequest pageRequest = PageRequest.of(pageNumber,pageSize,Sort.by(field).ascending());

	     Page<Bank> banks = bankRepository.findAll(pageRequest);

	     if (banks.isEmpty()) {
	         throw new NoRecordAvailableException("No Bank Records Available");
	     }

	     ResponseStructure<Page<Bank>> response = new ResponseStructure<>();
	     response.setStatusCode(HttpStatus.OK.value());
	     response.setMessage("Bank Records Fetched Successfully With Pagination And Sorting");
	     response.setData(banks);

	     return new ResponseEntity<>(response, HttpStatus.OK);
	 }
	 
	// ==================getBankByIfscCode=====================================
	 public ResponseEntity<ResponseStructure<Bank>> getBankByIfscCode(String ifsc) {

		    Optional<Bank> optional = bankRepository.findByIfsc(ifsc);

		    if(optional.isEmpty()) {
		        throw new NoRecordAvailableException("Bank not found");
		    }

		        ResponseStructure<Bank> response = new ResponseStructure<>();

		        response.setStatusCode(HttpStatus.OK.value());
		        response.setMessage("Bank found");
		        response.setData(optional.get());

		        return new ResponseEntity<>(response, HttpStatus.OK);

		}
	
	 //=================getBankByAddress===================================
	 public ResponseEntity<ResponseStructure<Bank>> getBankByAddress(Address address) {

		    Optional<Bank> optional = bankRepository.findByAddress(address);

		    if(optional.isEmpty()) {
		        throw new NoRecordAvailableException("Bank not found");
		    }

		        ResponseStructure<Bank> response = new ResponseStructure<>();

		        response.setStatusCode(HttpStatus.OK.value());
		        response.setMessage("Bank found for given address successfully");
		        response.setData(optional.get());

		        return new ResponseEntity<>(response, HttpStatus.OK);

		}

	 //=======================getBank by city=========================
	 public ResponseEntity<ResponseStructure<Bank>> getBankByCity(String city) {
		 Optional<Bank> optional = bankRepository.findByAddress_City(city);

		    if(optional.isEmpty()) {
		        throw new NoRecordAvailableException("Bank not found in city"+city);
		    }

		        ResponseStructure<Bank> response = new ResponseStructure<>();

		        response.setStatusCode(HttpStatus.OK.value());
		        response.setMessage("Bank found for given "+ city +" successfully");
		        response.setData(optional.get());

		        return new ResponseEntity<>(response, HttpStatus.OK);
	 }

	 //==================================get bank by contactNumber================================
	 public ResponseEntity<ResponseStructure<Bank>> getBankByContactNumber(long contactNumber) {
		Optional<Bank> optional = bankRepository.findBankByContactNumber(contactNumber);
		
		if(optional.isEmpty()) {
			throw new NoRecordAvailableException("Bank not found!");
		}
		
		ResponseStructure<Bank> response = new ResponseStructure<>();

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Bank found successfully");
        response.setData(optional.get());

        return new ResponseEntity<>(response, HttpStatus.OK);
	 }
	 
	 //==============================get bank by Branch Name==================================
	 public ResponseEntity<ResponseStructure<Bank>> getBankByBranchName(String branchName) {

		    Optional<Bank> optional = bankRepository.findByBranchName(branchName);

		    if (optional.isEmpty()) {
		        throw new NoRecordAvailableException(
		                "Bank not found for branch: " + branchName);
		    }

		    ResponseStructure<Bank> response = new ResponseStructure<>();

		    response.setStatusCode(HttpStatus.OK.value());
		    response.setMessage("Bank found successfully");
		    response.setData(optional.get());

		    return new ResponseEntity<>(response, HttpStatus.OK);
		}
	
}
