package vn.edu.fpt.peery.attachmenttype;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentTypeInitialize {
	@Autowired
	AttachmentTypeRepository repo;

	public void init() {
		AttachmentType type1 = new AttachmentType();
		type1.setType("Personal_Image");
		AttachmentType type2 = new AttachmentType();
		type2.setType("Cic_Front_Image");
		AttachmentType type3 = new AttachmentType();
		type3.setType("Cic_Back_Image");
		AttachmentType type4 = new AttachmentType();
		type4.setType("Bill_Image");
		AttachmentType type5 = new AttachmentType();
		type5.setType("Income_Proof");
		repo.saveAll(Arrays.asList(type1, type2, type3, type4, type5));
	}
}
