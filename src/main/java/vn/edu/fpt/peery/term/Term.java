package vn.edu.fpt.peery.term;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "terms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Term {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	@Column
	private Long NumberOfMonth;
	public Term(Long numberOfMonth) {
		NumberOfMonth = numberOfMonth;
	}
	
}
