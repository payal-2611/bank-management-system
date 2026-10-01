package jsp.springboot.container;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Address;
import jsp.springboot.entity.Bank;
import jsp.springboot.service.BankService;

@RestController
@RequestMapping("/bank")
public class BankController {
	
	@Autowired
    private BankService bankService;
	
	//Insert a record
	//T save(T ref)
	@PostMapping
    public ResponseEntity<ResponseStructure<Bank>> createBank(@RequestBody Bank bank) {
        return bankService.createBank(bank);
    }

	@GetMapping
	public ResponseEntity<ResponseStructure<List<Bank>>> getAllBank() {
	    return bankService.getAllBank();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Bank>> getBankById(@PathVariable int id) {
	    return bankService.getBankById(id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteBank(@PathVariable int id) {
	    return bankService.deleteBank(id);
	}
	
	@PutMapping
	public ResponseEntity<ResponseStructure<Bank>> updateBank(@RequestBody Bank bank) {
	    return bankService.updateBank(bank);
	}  
	
	@GetMapping("/pagination")
	public ResponseEntity<ResponseStructure<Page<Bank>>> getBankByPaginationAndSorting(
	        @RequestParam int pageNumber,
	        @RequestParam int pageSize,
	        @RequestParam String field) {

	    return bankService.getBankByPaginationAndSorting(
	            pageNumber, pageSize, field);
	}
	
	@GetMapping("/ifsc/{ifsc}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByIfscCode(
	        @PathVariable String ifsc) {

	    return bankService.getBankByIfscCode(ifsc);
	}
	
	@GetMapping("/address")
	public ResponseEntity<ResponseStructure<Bank>> getBankByAddress(@RequestBody Address address) {
	    return bankService.getBankByAddress(address);
	}
	
	@GetMapping("/city")
	public ResponseEntity<ResponseStructure<Bank>> getBankByCity(@RequestParam String city) {
	    return bankService.getBankByCity(city);
	}
	
	@GetMapping("/contactNumber")
	public ResponseEntity<ResponseStructure<Bank>> getBankByContactNumber(@RequestParam long contactNumber){
		return bankService.getBankByContactNumber(contactNumber);
	}
	
	@GetMapping("/branch")
	public ResponseEntity<ResponseStructure<Bank>> getBankByBranchName(@RequestParam String branchName) {
	    return bankService.getBankByBranchName(branchName);
	}
}
