package vn.edu.fpt.peery.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService {
	@Autowired
	SettingRepository repo;

	public void addSetting(Setting setting) {
		repo.save(setting);
	}

	public void addSettings(List<Setting> settings) {
		repo.saveAll(settings);
	}

	public Setting read(String key) {
		Setting target = repo.findById(key).orElseThrow();
		return target;
	}

	public List<Setting> readAll() {
		return repo.findAll();
	}

	public void setMultiple(List<Setting> settings) {
		List<Setting> _new = new ArrayList<>();

		settings.forEach(s -> {
			Setting target = repo.findById(s.getKey()).get();
			target.setValue(s.getValue());
			_new.add(target);
		});

		repo.saveAll(_new);
	}
}