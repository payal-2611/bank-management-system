package jsp.springboot.dto;

import jsp.springboot.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TransferResponse {

    private Account sender;
    private Account receiver;
	
}
