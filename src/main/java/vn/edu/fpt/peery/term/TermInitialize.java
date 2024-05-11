package vn.edu.fpt.peery.term;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TermInitialize {
	@Autowired
	TermRepository termRepository;

	public void init() {
		Term t1 = new Term(Long.valueOf(1));
		Term t2 = new Term(Long.valueOf(2));
		Term t3 = new Term(Long.valueOf(3));
		Term t4 = new Term(Long.valueOf(6));
		Term t5 = new Term(Long.valueOf(9));
		Term t6 = new Term(Long.valueOf(12));
		termRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5, t6));

	}
}
