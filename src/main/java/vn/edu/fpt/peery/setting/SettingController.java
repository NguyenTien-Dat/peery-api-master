package vn.edu.fpt.peery.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingController {
	@Autowired
	SettingService srv;

	@GetMapping("/peery/setting/read")
	public ResponseEntity<Setting> read(@RequestParam String key) {
		Setting target = srv.read(key);
		return ResponseEntity.ok(target);
	}

	@GetMapping("/peery/setting/list")
	public ResponseEntity<List<Setting>> readAll() {
		return ResponseEntity.ok(srv.readAll());
	}

	@PutMapping("/peery/setting/set")
	public ResponseEntity<Void> setMultiple(@RequestBody List<Setting> settings) {
		srv.setMultiple(settings);
		return ResponseEntity.ok().build();
	}
}