package kodlamaio.hrms.business.concretes;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kodlamaio.hrms.business.abstracts.CandidateService;
import kodlamaio.hrms.business.abstracts.EmailVerificationService;
import kodlamaio.hrms.business.abstracts.UserService;
import kodlamaio.hrms.core.utilities.IdentityValidation;
import kodlamaio.hrms.core.utilities.results.DataResult;
import kodlamaio.hrms.core.utilities.results.ErrorDataResult;
import kodlamaio.hrms.core.utilities.results.SuccessDataResult;
import kodlamaio.hrms.dataAccess.abstracts.CandidateDao;
import kodlamaio.hrms.entities.concretes.Candidate;
import kodlamaio.hrms.entities.concretes.EmailVerification;
import kodlamaio.hrms.entities.concretes.User;

@Service
public class CandidateManager implements CandidateService {
	
	/*dependecy injection - bağımlılık ekleme işlemi*/
	
	@Autowired
	private CandidateDao candidateDao;
	private EmailVerificationService emailVerificationService;
	private UserService userService;
	
	public CandidateManager(CandidateDao candidateDao, EmailVerificationService emailVerificationService, UserService userService) {
		super();
		this.candidateDao = candidateDao;
		this.emailVerificationService = emailVerificationService;
		this.userService = userService;
	}

	@Override
	public DataResult<Candidate> add(Candidate candidate) {
		if(!firstNameChecker(candidate)) {
			return new ErrorDataResult<Candidate>(null,"First name is mandatory!");
		}
		else if(!lastNameChecker(candidate)) {
			return new ErrorDataResult<Candidate>(null,"Last name is mandatory!");
		}
		
		else if(!IdentityValidation.isRealPerson(candidate.getIdentificationNumber())) {
			return new ErrorDataResult<Candidate>(null,"Could Not Authenticate!");
		}
		else if(candidate.getIdentificationNumber().isBlank()) {
			return new ErrorDataResult<Candidate>(null,"Identity Information cannot be left blank!");
		}
		
		else if(!birthDateChecker(candidate)) {
			return new ErrorDataResult<Candidate>(null,"Date of birth is mandatory!");
		}
		
		else if(!emailNullChecker(candidate)) {
			return new ErrorDataResult<Candidate>(null,"Email address is mandatory!");
		}
		else if(!isRealEmail(candidate)) {
			return new ErrorDataResult<Candidate>(null,"Email address entered incorrectly!");
		}
		
		else if(!passwordNullChecker(candidate)) {
			return new ErrorDataResult<Candidate>(null,"Password is mandatory!");
		}
		
		else if(candidateDao.findAllByEmail(candidate.getEmail()).stream().count() != 0 ) {
			return new ErrorDataResult<Candidate>(null,"This email is already registered!");
		}
		else if(candidateDao.findAllByIdentificationNumber(candidate.getIdentificationNumber()).stream().count() != 0 ) {
			return new ErrorDataResult<Candidate>(null,"This identification number is already registered!");
		}
		User savedUser = this.userService.add(candidate);
		this.emailVerificationService.generateCode(new EmailVerification(),savedUser.getId());
		return new SuccessDataResult<Candidate>(this.candidateDao.save(candidate),"Job seeker account added, verification code has been sent : " + candidate.getId());
		
		
	}
	
	private boolean firstNameChecker(Candidate candidate) {
		if(candidate.getFirstName().isBlank() || candidate.getFirstName().equals(null)) {
			return false;
		}
		return true;
	}
	
	private boolean lastNameChecker(Candidate candidate) {
		if(candidate.getLastName().isBlank() || candidate.getLastName().equals(null)) {
			return false;
		}
		return true;
	}
	
	private boolean birthDateChecker(Candidate candidate) {
		if(candidate.getBirthDate().equals(null)) {
			return false;
		}
		return true;
	}
	
	private boolean emailNullChecker(Candidate candidate) {
		if(candidate.getEmail().isBlank() || candidate.getEmail().equals(null)) {
			return false;
		}
		return true;
	}
	
	private boolean passwordNullChecker(Candidate candidate) {
		if(candidate.getPassword().isBlank() || candidate.getPassword().equals(null)) {
			return false;
		}
		return true;
	}
	
	private boolean isRealEmail(Candidate candidate) {
		 String regex = "^(.+)@(.+)$";
	     Pattern pattern = Pattern.compile(regex);
	     Matcher matcher = pattern.matcher(candidate.getEmail());
	     if(!matcher.matches()) {
	    	 return false;
	     }
	     return true;
	     
	}
	


	@Override
	public DataResult<List<Candidate>> getAll() {
		return new SuccessDataResult<List<Candidate>>(this.candidateDao.findAll(),"Job seekers successfully added");
	}

} 
