package jsp.springboot.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bank {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer bankId;

    @Column(nullable = false)
	private String bankName;
    
	@Column(unique=true, nullable=false)
	private String ifsc;
	
    @Column(nullable = false)
	private String branchName;
    
	@Column(unique=true,nullable=false)
	private long contactNumber;
	
	//establish connection
	
	@JsonIgnore
	@OneToMany(mappedBy="bank",cascade = CascadeType.ALL)
	private List<Account> accounts;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

	
}
