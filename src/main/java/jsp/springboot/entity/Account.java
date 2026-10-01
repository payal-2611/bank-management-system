package jsp.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jsp.springboot.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer accId;
	
	@Column(unique=true,nullable=false)
	private long accNumber;
	
	@Column(nullable=false)
	private String accHolder;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private AccountType accType;
	
	@Column(nullable=false)
	private double balance;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="bank_id")
	private Bank bank;
	
}
