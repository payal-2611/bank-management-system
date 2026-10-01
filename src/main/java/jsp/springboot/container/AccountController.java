package jsp.springboot.container;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.dto.TransferResponse;
import jsp.springboot.entity.Account;
import jsp.springboot.enums.AccountType;
import jsp.springboot.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	//Insert a record
	//t save(T ref)
	@PostMapping
	public ResponseEntity<ResponseStructure<Account>> createAccount(@RequestBody Account account,@RequestParam Integer bankId){
		return accountService.createAccount(account,bankId);
	}
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccount(){
		return accountService.getAllAccount();
	}
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Account>> getById(@PathVariable int id){
		return accountService.getById(id);
	}
	
	@PatchMapping("/{accId}")
	public ResponseEntity<ResponseStructure<Account>> updateAccountTypeAndHolderName(@PathVariable Integer accId,
																					@RequestParam String accType,
																					@RequestParam String accHolder){
		return accountService.updateAccountTypeAndHolderName(accId,accType,accHolder);
	}
	
	@PatchMapping("/{accId}/deposit")
	public ResponseEntity<ResponseStructure<Account>> depositAmount(@PathVariable Integer accId,
																					@RequestParam double amount){
		return accountService.depositAmount(accId,amount);
	}
	
	@PatchMapping("/{accId}/withdraw")
	public ResponseEntity<ResponseStructure<Account>> withdrawAmount(@PathVariable Integer accId,
																					@RequestParam double amount){
		return accountService.withdrawAmount(accId,amount);
	}
	
	@PatchMapping("/transfer")
	public ResponseEntity<ResponseStructure<TransferResponse>> transferAmount(@RequestParam Integer fromAccId,@RequestParam Integer toAccId,
																					@RequestParam double amount){
		return accountService.transferAmount(fromAccId,toAccId,amount);
	}
	
	@GetMapping("/bank/{bankId}")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByBankId(@PathVariable Integer bankId){
		return accountService.getAccountsByBankId(bankId);
	}
	@GetMapping("/type/{accType}")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByType(@PathVariable AccountType accType){
		return accountService.getAccountsByType(accType);
	}
	@GetMapping("/balance-greater")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByBalanceGreaterThan(@RequestParam double amount){
		return accountService.getAccountsByBalanceGreaterThan(amount);
	}
	@GetMapping("/pagination")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByPaginationAndSorting(@RequestParam int page,
																							@RequestParam int size,
																							@RequestParam String field){
		return accountService.getAccountsByPaginationAndSorting(page,size,field);
	}

}
