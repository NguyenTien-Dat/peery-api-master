package vn.edu.fpt.peery.term;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TermRepository extends JpaRepository<Term, Long> {
	Term getTermById(Long Id);
}
