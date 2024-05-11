package vn.edu.fpt.peery.banks;

import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankInitialize {

	@Autowired
	BankRepository bankRepository;

	public void init() {
		ArrayList<Bank> banks = new ArrayList<>();

		banks.add(new Bank("970415", "VietinBank", "Vietnam Bank for Industry and Trade"));
		banks.add(new Bank("970436", "Vietcombank", "Joint Stock Commercial Bank for Foreign Trade of Vietnam"));
		banks.add(new Bank("970418", "BIDV", "Joint Stock Commercial Bank for Investment and Development of Vietnam"));
		banks.add(new Bank("970405", "Agribank", "Vietnam Bank for Agriculture and Rural Development"));
		banks.add(new Bank("970448", "OCB", "Orient Commercial Joint Stock Bank"));
		banks.add(new Bank("970422", "MBBank", "Military Commercial Joint Stock Bank"));
		banks.add(new Bank("970407", "Techcombank", "Vietnam Technological and Commercial Joint Stock Bank"));
		banks.add(new Bank("970416", "ACB", "Asia Commercial Joint Stock Bank"));
		banks.add(new Bank("970432", "VPBank", "Vietnam Prosperity Joint Stock Commercial Bank"));
		banks.add(new Bank("970423", "TPBank", "Tien Phong Bank"));
		banks.add(new Bank("970403", "Sacombank", "Saigon Thuong Tin Commercial Joint Stock Bank"));
		banks.add(new Bank("970437", "HDBank", "Ho Chi Minh City Development Joint Stock Commercial Bank"));
		banks.add(new Bank("970454", "VietCapitalBank", "Viet Capital Bank"));
		banks.add(new Bank("970429", "SCB", "Saigon Commercial Joint Stock Bank"));
		banks.add(new Bank("970441", "VIB", "Vietnam International Commercial Joint Stock Bank"));
		banks.add(new Bank("970443", "SHB", "Saigon - Hanoi Commercial Joint Stock Bank"));
		banks.add(new Bank("970431", "Eximbank", "Vietnam Export Import Commercial Joint Stock Bank"));
		banks.add(new Bank("970426", "MSB", "Military Commercial Joint Stock Bank"));
		banks.add(new Bank("546034", "CAKE", "VPBank - CAKE Digital Bank"));
		banks.add(new Bank("546035", "Ubank", "VPBank - Ubank"));
		banks.add(new Bank("963388", "Timo", "Timo by Ban Viet Bank"));
		banks.add(new Bank("971005", "Viettel Money", "Viettel - Telecommunications Industry Group"));
		banks.add(new Bank("971011", "VNPT Money", "VNPT - Vietnam Posts and Telecommunications Group"));
		banks.add(new Bank("970400", "SaigonBank", "Saigon Cong Thuong Commercial Joint Stock Bank"));
		banks.add(new Bank("970409", "BacABank", "Bac A Commercial Joint Stock Bank"));
		banks.add(new Bank("970412", "PVcomBank", "PetroVietnam Construction Joint Stock Corporation Bank"));
		banks.add(new Bank("970414", "Oceanbank", "Ocean Commercial Joint Stock Bank"));
		banks.add(new Bank("970419", "NCB", "National Citizen Bank"));
		banks.add(new Bank("970424", "ShinhanBank", "Shinhan Bank Vietnam"));
		banks.add(new Bank("970425", "ABBANK", "An Binh Commercial Joint Stock Bank"));
		banks.add(new Bank("970427", "VietABank", "Viet A Commercial Joint Stock Bank"));
		banks.add(new Bank("970428", "NamABank", "Nam A Commercial Joint Stock Bank"));
		banks.add(new Bank("970430", "PGBank", "Petrolimex Group Commercial Joint Stock Bank"));
		banks.add(new Bank("970433", "VietBank", "Vietnam Thuong Tin Commercial Joint Stock Bank"));
		banks.add(new Bank("970438", "BaoVietBank", "Bao Viet Commercial Joint Stock Bank"));
		banks.add(new Bank("970440", "SeABank", "Southeast Asia Commercial Joint Stock Bank"));
		banks.add(new Bank("970446", "COOPBANK", "Cooperative Bank of Vietnam"));
		banks.add(new Bank("970449", "LienVietPostBank", "LienVietPostBank"));
		banks.add(new Bank("970452", "KienLongBank", "Kien Long Commercial Joint Stock Bank"));
		banks.add(new Bank("668888", "KBank", "Kasikornbank Public Company Limited"));
		banks.add(new Bank("970458", "UnitedOverseas", "United Overseas Bank (Vietnam)"));
		banks.add(new Bank("970410", "StandardChartered", "Standard Chartered Bank (Vietnam)"));
		banks.add(new Bank("970439", "PublicBank", "Public Bank Vietnam Limited"));
		banks.add(new Bank("801011", "Nonghyup", "National Agricultural Cooperative Federation - Nonghyup Bank"));
		banks.add(new Bank("970434", "IndovinaBank", "Indovina Bank"));
		banks.add(new Bank("970456", "IBKHCM", "Industrial Bank of Korea - Ho Chi Minh City Branch"));
		banks.add(new Bank("970455", "IBKHN", "Industrial Bank of Korea - Hanoi Branch"));
		banks.add(new Bank("970421", "VRB", "Vietnam - Russia Joint Venture Bank"));
		banks.add(new Bank("970457", "Woori", "Woori Bank"));
		banks.add(new Bank("970462", "KookminHN", "Kookmin Bank - Hanoi Branch"));
		banks.add(new Bank("970463", "KookminHCM", "Kookmin Bank - Ho Chi Minh City Branch"));
		banks.add(new Bank("458761", "HSBC", "Hongkong and Shanghai Banking Corporation"));
		banks.add(new Bank("970442", "HongLeong", "Hong Leong Bank Vietnam"));
		banks.add(new Bank("970408", "GPBank", "Global Petro Bank"));
		banks.add(new Bank("970406", "DongABank", "Dong A Commercial Joint Stock Bank"));
		banks.add(new Bank("796500", "DBSBank", "DBS Bank Ltd - Ho Chi Minh City Branch"));
		banks.add(new Bank("422589", "CIMB", "CIMB Bank Vietnam"));
		banks.add(new Bank("970444", "CBBank", "Construction Joint Stock Commercial Bank"));
		banks.add(new Bank("533948", "Citibank", "Citibank, N.A. - Hanoi Branch"));
		banks.add(new Bank("970466", "KEBHanaHCM", "KEB Hana Bank - Ho Chi Minh City Branch"));
		banks.add(new Bank("970467", "KEBHanaHN", "KEB Hana Bank - Hanoi Branch"));
		banks.add(new Bank("977777", "MAFC", "Mirae Asset Finance Company"));
		banks.add(new Bank("999888", "VBSP", "Vietnam Bank for Social Policies"));

		banks.sort(Comparator.comparing(Bank::getName));

		bankRepository.saveAll(banks);
	}
}