package kodlamaio.hrms.core.utilities;

import kodlamaio.hrms.core.utilities.results.ErrorResult;
import kodlamaio.hrms.core.utilities.results.Result;
import kodlamaio.hrms.core.utilities.results.SuccessResult;
import kodlamaio.hrms.services.FakeMernis;

public class IdentityValidation {
	 
	public static Result isRealPerson(String tcNo) {
		FakeMernis mernis = new FakeMernis();
		if(FakeMernis.validate(tcNo) == true) {
			return new SuccessResult();
		}
		return new ErrorResult("It has to be the identity number of the Republic of Turkey!");
	}

} 
