package com.connection.document;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.connection.document.configuration.EmailConfig;
import com.connection.document.model.common.EmailSenderModel;
import com.connection.document.model.common.MetaData;
import com.connection.document.model.common.RequestModel;
import com.connection.document.model.common.Signature;
import com.connection.document.model.common.WaterMark;
import com.connection.document.repository.TemplateRepository;
import com.connection.document.service.IPdfGeneratorService;
import com.connection.document.service.impl.PdfGeneratorServiceImpl;
import com.lowagie.text.DocumentException;

@SpringBootTest
class PdfGeneratorServiceTest {

	@Autowired
	TemplateRepository templaterepo;

	@Autowired
	IPdfGeneratorService iPdfGeneratorService;
	
	@Autowired
	EmailConfig emailConfig;

	@BeforeEach
	public void set() {
		this.iPdfGeneratorService = new PdfGeneratorServiceImpl(templaterepo,emailConfig);
	}

	@Test
	void contextLoads() throws DocumentException, Exception {
		HttpServletResponse request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getResponse();
		
		 Map<String, String> myMap1 = new HashMap<>();
		 myMap1.put("Month","Feb");
		 myMap1.put("Month#","01");
		 myMap1.put("EMI","250000");
		 myMap1.put("Principle","5000000");
		 myMap1.put("Interest rate","3");
		 myMap1.put("Outstanding Balance","4897653");
		 
		 Map<String, String> myMap2 = new HashMap<>();
		 myMap2.put("Month","Feb");
		 myMap2.put("Month#","01");
		 myMap2.put("EMI","250000");
		 myMap2.put("Principle","5000000");
		 myMap2.put("Interest rate","3");
		 myMap2.put("Outstanding Balance","4897653");
		 
		List<Map<String, String>> demolist=new ArrayList<>();
		demolist.add(myMap2);
		demolist.add(myMap2);
		
		List<List<Map<String, String>>> demolist2=new ArrayList<>();
		demolist2.add(demolist);
		
		
		Map<String, String> map = new HashMap<>();
		map.put("{{currentDate}}", "06-02-2023");
		map.put("{{nameL}}", "Raja Yadav");
		map.put("{{addressL}}", "salt lake kolkata");
		map.put("{{panCardL}}", "QWER9878L");
		map.put("{{fatherNameL}}", "Sursh Yadav");
		map.put("{{nameB}}", "Twinkle Kaushal");
		map.put("{{panCardB}}", "ASDF3245N");
		map.put("{{addressB}}", "Newtown");
		map.put("{{fatherNameB}}", "Ashok kaushal");
		map.put("{{roi}}", "4");
		map.put("{{month}}", "2");
		map.put("{{amount}}", "500000");
		map.put("{{borrowerSign}}","S3R3aW5rbGU");
		map.put("{{lenderSign}}","WWFkYXZS");
		RequestModel templateRequest = RequestModel.builder().templateType("finalloanAgreement").dynamicVar(map)
				.password("Twinkle@2001")
				.metadata(MetaData.builder().author("Twinkle").creator("Raja").subject("LOS").title("Agreement")
						.creationdate(LocalDateTime.now()).modeificationdate(LocalDateTime.now()).build())
				.watermark(WaterMark.builder().isMandatory("Y").waterMarkType("Image").build())
				.list(demolist2)
				.emailSender(EmailSenderModel.builder().body("Hello , here's your PDF!").toEmail("twinkle.kaushal@indusnet.co.in").subject("Agreement").build())
				.signature(Signature.builder().isMandatory("Y").signatureType("Text").build()).build();

		String str = iPdfGeneratorService.exportPdf(request, templateRequest);
		assertEquals("pdf generated successfully", str);
	}
	
	//Testing the Base64 Encoder-decoder of Signature's
	@Test
   void EncodingDecodingCheck() {
		
		String borrowerTextSignActual = null;
		String lenderTextSignActual = null;

		byte[] decodedeTextSignB = Base64.decodeBase64("S3R3aW5rbGU");
		borrowerTextSignActual = new String(decodedeTextSignB, StandardCharsets.UTF_8);

		byte[] decodedeTextSignL = Base64.decodeBase64("WWFkYXZS");
		lenderTextSignActual = new String(decodedeTextSignL, StandardCharsets.UTF_8);
		
		String borrowerTextSignExpected = "Ktwinkle";
		String lenderTextSignExpected = "YadavR";
		
		assertEquals(borrowerTextSignActual, borrowerTextSignExpected);
		assertEquals(lenderTextSignActual, lenderTextSignExpected);
		
	}

}
