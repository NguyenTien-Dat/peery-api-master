package vn.edu.fpt.peery.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingInit {
	@Autowired
	private SettingService srv;

	public void init() {
		List<Setting> settings = new ArrayList<>();

		settings.add(new Setting("APR_MIN", "Minimum interest rate per year", "0.05"));
		settings.add(new Setting("APR_MAX", "Maximum interest rate per year", "0.2"));

		srv.addSettings(settings);
	}
}