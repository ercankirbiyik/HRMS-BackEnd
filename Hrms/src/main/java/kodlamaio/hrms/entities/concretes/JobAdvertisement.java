package kodlamaio.hrms.entities.concretes;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
  
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_advertisements")
@Entity
public class JobAdvertisement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "quota")
	private int quota;
	
	@Column(name = "appeal_expiration_date")
private LocalDateTime appealExpirationDate;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="min_salary")
	private Double minSalary;
	
	@Column(name="max_salary")
	private Double maxSalary;
	
	@Column(name="is_active")
	private boolean isActive;
	
	 @ManyToOne
	  @JoinColumn(name = "city_id")
	   private City city;
	 
	 @ManyToOne
	  @JoinColumn(name = "job_title_id")
	   private JobTitle jobtitle;
	 
	 @ManyToOne
	  @JoinColumn(name = "employer_id")
	   private Employer employer;

}
