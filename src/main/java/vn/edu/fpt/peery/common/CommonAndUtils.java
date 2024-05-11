package vn.edu.fpt.peery.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.edu.fpt.peery.setting.SettingService;
import vn.edu.fpt.peery.user.User;

@Component
public class CommonAndUtils {
	@Autowired
	SettingService st;

	public static String createHyperLink(String url, Long id) {
		return "";
	}

	public BigDecimal calculateAPR(User user) {
		Double aprMin = Double.valueOf(st.read("APR_MIN").getValue());
		Double aprMax = Double.valueOf(st.read("APR_MAX").getValue());
		Integer creditScoreMin = 0;
		Integer creditScoreMax = 100;

		// Formula generated by ChatGPT
		double rawResult = aprMax - ((double) ( user.getCreditScore() - creditScoreMin) / (creditScoreMax - creditScoreMin)) * (aprMax - aprMin);

		BigDecimal rate = BigDecimal.valueOf(rawResult).setScale(4, RoundingMode.HALF_UP);
		return rate;
	}
}