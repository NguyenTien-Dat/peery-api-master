package vn.edu.fpt.peery.banks;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity @Table(name = "banks")
@Getter @Setter @Builder @RequiredArgsConstructor @AllArgsConstructor @NoArgsConstructor
public class Bank {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NonNull
	private String code;

	@NonNull
	private String name;

	@NonNull
	private String fullName;
}