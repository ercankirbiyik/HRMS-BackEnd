package kodlamaio.hrms.business.concretes;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import kodlamaio.hrms.business.abstracts.EmailVerificationService;
import kodlamaio.hrms.business.abstracts.EmployerService;
import kodlamaio.hrms.business.abstracts.UserService;
import kodlamaio.hrms.core.utilities.results.DataResult;
import kodlamaio.hrms.core.utilities.results.ErrorDataResult;
import kodlamaio.hrms.core.utilities.results.SuccessDataResult;
import kodlamaio.hrms.dataAccess.abstracts.EmployerDao;
import kodlamaio.hrms.entities.concretes.EmailVerification;
import kodlamaio.hrms.entities.concretes.Employer;
import kodlamaio.hrms.entities.concretes.User;

@Service
public class EmployerManager implements EmployerService {
	
	private EmployerDao employerDao;
	private EmailVerificationService emailVerificationService;
	private UserService userService;
	
	public EmployerManager(EmployerDao employerDao, EmailVerificationService emailVerificationService,
			UserService userService) {
		super();
		this.employerDao = employerDao;
		this.emailVerificationService = emailVerificationService;
		this.userService = userService;
	}

	@Override
	public DataResult<Employer> add(Employer employer) {
		
		if(!companyNameChecker(employer)) {
			return new ErrorDataResult<Employer>(null,"Company name is mandatory!");
		}
		else if(!webSiteChecker(employer)) {
			return new ErrorDataResult<Employer>(null,"Website address is mandatory!");
		}
		else if(!isRealEmployer(employer)) {
			return new ErrorDataResult<Employer>(null,"Invalid email address!");
		}
		else if(!passwordNullChecker(employer)) {
			return new ErrorDataResult<Employer>(null,"Password is mandatory!");
		}
		else if(!isRealPhoneNumber(employer)) {
			return new ErrorDataResult<Employer>(null,"Invalid phone number!");
	    }
		else if(!isEmailAlreadyRegistered(employer)) {
			return new ErrorDataResult<Employer>(null,"This email address is already registered!");
		}
		User savedUser = this.userService.add(employer);
		this.emailVerificationService.generateCode(new EmailVerification(),savedUser.getId());
		return new SuccessDataResult<Employer>(this.employerDao.save(employer),"Employer account added, verification code has been sent : " + employer.getId());
	}
	
	private boolean companyNameChecker(Employer employer) {
		if(employer.getCompanyName().isBlank() || employer.getCompanyName() == null) {
			return false;
		}
		return true;
	}
	
	private boolean webSiteChecker(Employer employer) {
		if(employer.getWebAddress().isBlank() || employer.getWebAddress() == null) {
			return false;
		}
		return true;
	} 
	
	private boolean isRealEmployer(Employer employer) {
		 String regex = "^(.+)@(.+)$";
	     Pattern pattern = Pattern.compile(regex);
	     Matcher matcher = pattern.matcher(employer.getEmail());
	     if(!matcher.matches()) {
	    	 return false; 
	     }
	     else if(!employer.getEmail().contains(employer.getWebAddress())) {
	    	return false; 
	     }
	     return true;
	     
	}
	
	private boolean isEmailAlreadyRegistered(Employer employer) {
		if(employerDao.findAllByEmail(employer.getEmail()).stream().count() != 0) {
			return false;
		}
		return true;
	}
	
	private boolean passwordNullChecker(Employer employer) {
		if(employer.getPassword().isBlank() || employer.getPassword() == null) {
			return false;
		}
		return true;
	}
	
	private boolean isRealPhoneNumber(Employer employer) {
		String patterns 
	      = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" 
	      + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$" 
	      + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
		
		Pattern pattern = Pattern.compile(patterns);
		Matcher matcher = pattern.matcher(employer.getPhoneNumber());
		
		if(!matcher.matches()) {
			return false;
		}
		return true;
		
	}

	@Override
	public DataResult<List<Employer>> getAll() {
		return new SuccessDataResult<List<Employer>>(this.employerDao.findAll(),"Employers have been successfully listed!");
	}


}
