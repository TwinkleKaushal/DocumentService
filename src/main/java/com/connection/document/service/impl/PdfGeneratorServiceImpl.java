package com.connection.document.service.impl;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.tomcat.util.codec.binary.Base64;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.connection.document.configuration.EmailConfig;
import com.connection.document.exception.NoSuchElementException;
import com.connection.document.model.TemplateModel;
import com.connection.document.model.common.RequestModel;
import com.connection.document.repository.TemplateRepository;
import com.connection.document.service.IPdfGeneratorService;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PdfGeneratorServiceImpl implements IPdfGeneratorService {

	@Autowired
	TemplateRepository templaterepo;

	@Autowired
	EmailConfig emailConfig;

	@Value("${temp.var1}")
	String braces1;

	@Value("${temp.var2}")
	String braces2;

	public PdfGeneratorServiceImpl(TemplateRepository templaterepo, EmailConfig emailConfig) {
		super();
		this.templaterepo = templaterepo;
		this.emailConfig = emailConfig;
	}

	/**
	 * this method used to set the alignment of pdf and returns the pdf with
	 * replaced variables
	 */
	@Override
	public String exportPdf(HttpServletResponse response, RequestModel requestModel)
			throws DocumentException, Exception {
		log.info("Inside PDF service implementation");

		TemplateModel template = templaterepo.findByTemplateType(requestModel.getTemplateType());

		// Decoding signature
		String borrowerTextSign = null;
		String lenderTextSign = null;

		byte[] decodedeTextSignB = Base64.decodeBase64(requestModel.getDynamicVar().get("{{borrowerSign}}"));
		borrowerTextSign = new String(decodedeTextSignB, StandardCharsets.UTF_8);

		byte[] decodedeTextSignL = Base64.decodeBase64(requestModel.getDynamicVar().get("{{lenderSign}}"));
		lenderTextSign = new String(decodedeTextSignL, StandardCharsets.UTF_8);
		
	

		// Decoding watermark
		byte[] decodedWatermark = Base64.decodeBase64(template.getWatermark().getImageWatermark());
		File path = new File("./src/main/resources/img/watermark.png");
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(decodedWatermark);
		}
		// Decoding template
		byte[] decodedTemp = Base64.decodeBase64(template.getTemplate());
		File path1 = new File("./templateFolder.docx");

		try (FileOutputStream fos1 = new FileOutputStream(path1)) {
			fos1.write(decodedTemp);
		}
		FileInputStream fis = new FileInputStream(path1);
		try (XWPFDocument doc = new XWPFDocument(fis)) {
			
			// Adding dynamic values to tables
			List<List<Map<String, String>>> list1 = requestModel.getList();
			List<XWPFTable> table1 = doc.getTables();

			for (int k = 0; k < table1.size(); k++) {
				List<Map<String, String>> list = list1.get(k);

				for (Map<String, String> map : list) {
					XWPFTable table = table1.get(k);

					XWPFTableRow oldRow = table.getRow(0);
					CTRow ctrow = CTRow.Factory.parse(oldRow.getCtRow().newInputStream());
					XWPFTableRow newRow = new XWPFTableRow(ctrow, table);

					for (int i = 0; i < newRow.getTableCells().size(); i++) {

						if (map.get(table.getRow(0).getCell(i).getText()) == null)
							continue;
						newRow.getCell(i).removeParagraph(0);
						newRow.getCell(i).setText(map.get(table.getRow(0).getCell(i).getText()));
						log.info(map.get(table.getRow(0).getCell(i).getText()));

					}
					table.addRow(newRow);
				}

			}

			List<XWPFParagraph> paragraphs = doc.getParagraphs();
			for (XWPFParagraph xwpfParagraph : paragraphs) {

				for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
					if (xwpfRun != null) {

						String docText = xwpfRun.getText(0);
						if (docText != null) {

							for (Map.Entry<String, String> tVariable : requestModel.getDynamicVar().entrySet()) {

								docText = docText.replace(braces1 + tVariable.getKey() + braces2, tVariable.getValue());

								if (borrowerTextSign != null) {

									docText = docText.replace("{{borrowerSign}}", borrowerTextSign);
									docText = docText.replace("{{lenderSign}}", lenderTextSign);
								}
							}
							xwpfRun.setText(docText, 0);

						}

					}

				}

			}

			try (FileOutputStream fo = new FileOutputStream("./replacedDocs.docx")) {
				doc.write(fo);
			}
		}

		FileInputStream in = new FileInputStream("./replacedDocs.docx");

		XWPFDocument docx = new XWPFDocument(in);
		File outFile = new File("./ReplacedPdf.pdf");
		OutputStream out1 = new FileOutputStream(outFile);
		PdfOptions options = null;
		PdfConverter.getInstance().convert(docx, out1, options);
		docx.close();
		out1.close();

		// read existing pdf
		PdfReader reader = new PdfReader("./ReplacedPdf.pdf");
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("./waterMark.pdf"));

		Image iWaterMark = null;
		String tWaterMark = null;
		if (requestModel.getWatermark().getIsMandatory().equalsIgnoreCase("Y")) {
			String wType = requestModel.getWatermark().getWaterMarkType();
			switch (wType) {
			case "Image":
				iWaterMark = Image.getInstance("./src/main/resources/img/watermark.png");
				float w = iWaterMark.getScaledWidth();
				float h = iWaterMark.getScaledHeight();

				// properties
				PdfContentByte over;
				Rectangle pagesize;
				float x;
				float y;

				// loop over every page
				int n = reader.getNumberOfPages();
				for (int i = 1; i <= n; i++) {

					// get page size and position
					pagesize = reader.getPageSizeWithRotation(i);
					x = (pagesize.getLeft() + pagesize.getRight()) / 2;
					y = (pagesize.getTop() + pagesize.getBottom()) / 2;
					over = stamper.getOverContent(i);
					over.saveState();

					// set transparency
					PdfGState state = new PdfGState();
					state.setFillOpacity(0.2f);
					over.setGState(state);
					over.addImage(iWaterMark, w, 0, 0, h, x - (w / 2), y - (h / 2));
					over.restoreState();
				}
				break;
			case "Text":
				tWaterMark = template.getWatermark().getTextWatermark();
				Font font = new Font(Font.HELVETICA, 34, Font.BOLD);
				font.setColor(Color.gray);
				font.setSize(150);
				font.isBold();
				Phrase p = new Phrase(tWaterMark, font);

				// properties
				PdfContentByte end;
				float a;
				float b;

				// loop over every page
				int j = reader.getNumberOfPages();
				for (int i = 1; i <= j; i++) {

					// get page size and position
					pagesize = reader.getPageSizeWithRotation(i);
					a = (pagesize.getLeft() + pagesize.getRight()) / 2;
					b = (pagesize.getTop() + pagesize.getBottom()) / 2;
					end = stamper.getOverContent(i);
					end.saveState();

					// set transparency
					PdfGState state = new PdfGState();
					state.setFillOpacity(0.2f);
					end.setGState(state);
					ColumnText.showTextAligned(end, Element.ALIGN_CENTER, p, a, b, 50);
					end.restoreState();
				}
				break;
			case "":
				break;
			default:
				throw new NoSuchElementException("Please give the valid Watermark type or leave it empty");
			}
		}

		stamper.close();
		reader.close();

		// Generating metadata for the document
		PDDocument pddDoc = PDDocument.load(new File("./waterMark.pdf"));
		PDDocumentInformation info = pddDoc.getDocumentInformation();
		info.setAuthor(requestModel.getMetadata().getAuthor());
		info.setCreator(requestModel.getMetadata().getCreator());
		info.getCreationDate();
		info.getModificationDate();
		info.setSubject(requestModel.getMetadata().getSubject());
		info.setTitle(requestModel.getMetadata().getTitle());

		// setting password encryption
		String indusnet1 = requestModel.getPassword();
		log.info("Your password is: " + indusnet1);
		AccessPermission ap = new AccessPermission();
		StandardProtectionPolicy spp = new StandardProtectionPolicy(indusnet1, "pdfGenerator", ap);
		spp.setEncryptionKeyLength(128);
		spp.setPermissions(ap);
		pddDoc.protect(spp);
		pddDoc.save(response.getOutputStream());
		pddDoc.save(new FileOutputStream("./Agreement.pdf"));
		pddDoc.close();

		CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(()->{
			try {
				emailConfig
				.sendMail(requestModel.getEmailSender().getToEmail(),
						"pdf_" + requestModel.getMetadata().getCreator() + "_" + requestModel.getMetadata().getSubject()
								+ "_" + requestModel.getMetadata().getCreationdate() + ".pdf",
								"./waterMark.pdf", requestModel);
			} catch (MessagingException | IOException e) {
				e.printStackTrace();
			}

        });
		
		return ("pdf generated successfully");
	}
	

	static void replacePictureData(XWPFPictureData source, byte[] data) {
		try (ByteArrayInputStream in = new ByteArrayInputStream(data);
				OutputStream out = source.getPackagePart().getOutputStream();) {
			byte[] buffer = new byte[2048];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
