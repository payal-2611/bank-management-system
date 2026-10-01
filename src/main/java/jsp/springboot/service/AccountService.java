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
import jsp.springboot.dto.TransferResponse;
import jsp.springboot.entity.Account;
import jsp.springboot.entity.Bank;
import jsp.springboot.enums.AccountType;
import jsp.springboot.exception.NoRecordAvailableException;
import jsp.springboot.repository.AccountRepository;
import jsp.springboot.repository.BankRepository;


@Service
public class AccountService {

//	@Autowired
//	private Account account;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BankRepository bankRepository;

	//================create Account===========================================================
	public ResponseEntity<ResponseStructure<Account>> createAccount(Account account, Integer bankId) {
		
		// Account holder name validation
	    if (account.getAccHolder() == null|| account.getAccHolder().trim().isEmpty()) {
			throw new IllegalArgumentException("Account holder name is required");
	    }
	    
	    if (!account.getAccHolder().matches("[A-Za-z ]+")) {
	        throw new IllegalArgumentException("Account holder name should contain only alphabets");
	    }

		//Account number validation
		String accountNumber=String.valueOf(account.getAccNumber());
		
		if (!accountNumber.matches("^[0-9]{10}$")) {
			throw new IllegalArgumentException("Account number should contain exactly 10 digits");
		}
		
		// Duplicate account number
	    if (accountRepository.findByAccNumber(account.getAccNumber()).isPresent()) {
	        throw new IllegalArgumentException("Account number already exists");
	    }
	    
	    // Account type validation
	    if (account.getAccType() == null) {
	        throw new IllegalArgumentException("Account type is required");
	    }
	    if (account.getAccType() == null) {
	        throw new IllegalArgumentException("Account type is required");
	    }
	    
	    // Balance validation
	    if (account.getBalance() < 0) {
	        throw new IllegalArgumentException("Balance cannot be negative");
	    }

	    // Bank validation
	    Bank bank = bankRepository.findById(bankId)
	            .orElseThrow(() -> new IllegalArgumentException("Bank not found"));

	    account.setBank(bank);
	    Account savedAccount = accountRepository.save(account);

	    ResponseStructure<Account> response =new ResponseStructure<>();

	    response.setStatusCode(HttpStatus.CREATED.value());
	    response.setMessage("Account Created Successfully");
	    response.setData(savedAccount);

	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	//============get all Account======================================================
	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccount() {
		List<Account> acc = accountRepository.findAll();

	    if (acc.isEmpty()) {
	        throw new NoRecordAvailableException("No Account Records Available");
	    }

	    ResponseStructure<List<Account>> response = new ResponseStructure<>();
	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("All Account Records Fetched Successfully");
	    response.setData(acc);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	//=======================get by id===============================================

	public ResponseEntity<ResponseStructure<Account>> getById(int id) {
		Optional<Account> op=accountRepository.findById(id);
		
		if(op.isEmpty()) {
			throw new NoRecordAvailableException("no Account Record is Available for "+ id);
		}
		
		ResponseStructure<Account> response=new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Account Record fetched Successfully");
		response.setData(op.get());
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	//======================update AccountType And HolderName================================================
	public ResponseEntity<ResponseStructure<Account>> updateAccountTypeAndHolderName(Integer accId,
																					String accType,
																					String accHolder) {
		Account account=accountRepository.findById(accId).orElseThrow(()->new NoRecordAvailableException("Account not found"));
		
		//Account Holder Validation
		if(accHolder==null || accHolder.trim().isEmpty()) {
			throw new IllegalArgumentException("Account holder name is required");
		}
		if(!accHolder.matches("[A-Za-z ]+")) {
			 throw new IllegalArgumentException("Account holder name should contain only alphabets");
		}
		//Account Type Validation
		AccountType type;
		
		try {
			type=AccountType.valueOf(accType.toUpperCase());
		}
		catch(Exception e){
			throw new IllegalArgumentException("Invalid account type");
		}
		account.setAccHolder(accHolder);
		account.setAccType(type);
		
		Account updateAccount=accountRepository.save(account);
		
		ResponseStructure<Account> response =new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Account Type and Holder Name Updated Successfully");
		response.setData(updateAccount);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	//==================Deposit Amount =============================
	public ResponseEntity<ResponseStructure<Account>> depositAmount(Integer accId, double amount) {
		
		Account account=accountRepository.findById(accId).orElseThrow(()->new NoRecordAvailableException("Account not found"));

		if(amount<=0) {
	        throw new IllegalArgumentException("Deposit amount must be greater than zero");
		}
		double updatedBalance=account.getBalance()+amount;
		account.setBalance(updatedBalance);
		
		Account saveAmount=accountRepository.save(account);
		
		ResponseStructure<Account> response =new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Amount Deposited Successfully");
		response.setData(saveAmount);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	//======================Withdraw amount==============================
	public ResponseEntity<ResponseStructure<Account>> withdrawAmount(Integer accId, double amount) {
		Account account=accountRepository.findById(accId).orElseThrow(()->new NoRecordAvailableException("Account not found"));

		if(amount<=0) {
	        throw new IllegalArgumentException("Withdrawn amount must be greater than zero");
		}
		if(amount>account.getBalance()) {
	        throw new IllegalArgumentException("Insufficient balance");
		}
		double updatedBalance=account.getBalance()-amount;
		account.setBalance(updatedBalance);
		
		Account saveAmount=accountRepository.save(account);
		
		ResponseStructure<Account> response =new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Amount Withdrawn Successfully");
		response.setData(saveAmount);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	//=========================transfer Amount============================================
	public ResponseEntity<ResponseStructure<TransferResponse>> transferAmount(Integer fromAccId, Integer toAccId,
			double amount) {
		Account sender=accountRepository.findById(fromAccId).orElseThrow(()->new NoRecordAvailableException("Sender Account not found"));

		Account receiver=accountRepository.findById(toAccId).orElseThrow(()->new NoRecordAvailableException("Receiver Account not found"));

		if(fromAccId.equals(toAccId)) {
			throw new IllegalArgumentException("Sender and receiver accounts cannot be the same");
		}
		if(amount<=0) {
	        throw new IllegalArgumentException("Transfer amount must be greater than zero");
		}
		if(amount>sender.getBalance()) {
	        throw new IllegalArgumentException("Insufficient balance");
		}
		sender.setBalance(sender.getBalance()-amount);
		receiver.setBalance(receiver.getBalance()+amount);
		
		accountRepository.save(sender);
		accountRepository.save(receiver);
		
		TransferResponse transferResponse = new TransferResponse(sender, receiver);
		ResponseStructure<TransferResponse> response =new ResponseStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("Amount Transferred Successfully");
		response.setData(transferResponse);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	//================get Account by bankId=================================

	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByBankId(Integer bankId) {
		List<Account> accounts=accountRepository.findByBankBankId(bankId);
		
		if(accounts.isEmpty()) {
			 throw new NoRecordAvailableException("No accounts found for this bank");
		}
		ResponseStructure<List<Account>> response = new ResponseStructure<>();

	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Accounts Fetched Successfully");
	    response.setData(accounts);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//==============get Account by Type=========================================
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByType(AccountType accType) {
		List<Account> accounts=accountRepository.findByAccType(accType);
		
		if(accounts.isEmpty()) {
			 throw new NoRecordAvailableException("No accounts found for this Account Type");
		}
		ResponseStructure<List<Account>> response = new ResponseStructure<>();

	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Accounts Fetched Successfully");
	    response.setData(accounts);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//============get Accounts By Balance Greater Than value========================================
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByBalanceGreaterThan(double amount) {
		List<Account> accounts = accountRepository.findByBalanceGreaterThan(amount);
		  
		if(accounts.isEmpty()) {
		    throw new NoRecordAvailableException("No accounts found with balance greater than " + amount);
		}
		  	
		ResponseStructure<List<Account>> response = new ResponseStructure<>();

	    response.setStatusCode(HttpStatus.OK.value());
	    response.setMessage("Accounts Fetched Successfully");
	    response.setData(accounts);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//========================get AccountsBy Pagination And Sorting============================
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByPaginationAndSorting(int page, int size,
																							String field) {
		Page<Account> accountPage=accountRepository.findAll(PageRequest.of(page, size,Sort.by(field).ascending()));
		
		List<Account> accounts=accountPage.getContent();
		if(accounts.isEmpty()) {
	        throw new NoRecordAvailableException("No accounts available");
		}
		 ResponseStructure<List<Account>> response = new ResponseStructure<>();

		    response.setStatusCode(HttpStatus.OK.value());
		    response.setMessage("Accounts Fetched Successfully");
		    response.setData(accounts);

		    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
