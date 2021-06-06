package kodlamaio.hrms.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kodlamaio.hrms.business.abstracts.CandidateJobExperienceService;
import kodlamaio.hrms.core.utilities.results.DataResult;
import kodlamaio.hrms.core.utilities.results.ErrorDataResult;
import kodlamaio.hrms.core.utilities.results.SuccessDataResult;
import kodlamaio.hrms.dataAccess.abstracts.CandidateCvDao;
import kodlamaio.hrms.dataAccess.abstracts.CandidateJobExperienceDao;
import kodlamaio.hrms.entities.concretes.CandidateJobExperience;


@Service
public class CandidateJobExperienceManager implements CandidateJobExperienceService {
	
	private CandidateJobExperienceDao candidateJobExperienceDao;
	
	private CandidateCvDao candidateCvDao;

	
	@Autowired
	public CandidateJobExperienceManager(CandidateJobExperienceDao candidateJobExperienceDao, CandidateCvDao candidateCvDao) {
		super();
		this.candidateJobExperienceDao = candidateJobExperienceDao;
		this.candidateCvDao = candidateCvDao;
	}
	
	@Override
	public DataResult<List<CandidateJobExperience>> orderedCandidateCvJobExperience(int id) {
		if(!this.candidateCvDao.existsById(id)) {
			return new ErrorDataResult<>("CV is not found!");
		}
		return new SuccessDataResult<List<CandidateJobExperience>>(this.candidateJobExperienceDao.getJobExperienceOrderByExistDateDesc(id),"The job seeker's experience information has been successfully listed!");
	}

	@Override
	public DataResult<List<CandidateJobExperience>> getAll() {
		return new SuccessDataResult<List<CandidateJobExperience>>(this.candidateJobExperienceDao.findAll(),"The job seeker's experience successfully listed!");
	}

}
