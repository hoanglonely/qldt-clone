package com.mb.lab.banks.user.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.POIXMLException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.mb.lab.banks.user.business.dto.base.CategoryDto;
import com.mb.lab.banks.user.business.dto.base.FileDownloadDto;
import com.mb.lab.banks.user.business.dto.base.ImportVerifyDto;
import com.mb.lab.banks.user.business.service.sub.ImportFileSubService;
import com.mb.lab.banks.user.util.persistence.PageNumberRequest;
import com.mb.lab.banks.utils.common.StringUtils;
import com.mb.lab.banks.utils.exception.BusinessException;
import com.mb.lab.banks.utils.exception.BusinessExceptionCode;

public class ExcelUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

	public static final int MAXIMUM_NUMBER_OF_RECORD_PER_IMPORT = 2000;

	protected static final Pattern VALID_CODE_REGEX = Pattern.compile("^[a-zA-Z0-9_]+$", Pattern.CASE_INSENSITIVE);
	protected static final Pattern VALID_PHONE_REGEX = Pattern.compile("^[0-9]{8,12}$", Pattern.CASE_INSENSITIVE);
	protected static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	private static final long NUMBER_RECORD_QUERY_WHEN_EXPORT = 1000;

	public static final int EXCEL_COLUMN_WIDTH_SMALL = 3000;
	public static final int EXCEL_COLUMN_WIDTH_MEDIUM = EXCEL_COLUMN_WIDTH_SMALL * 2;
	public static final int EXCEL_COLUMN_WIDTH_LARGE = EXCEL_COLUMN_WIDTH_SMALL * 3;
	public static final int EXCEL_COLUMN_WIDTH_EXTRA_LARGE = EXCEL_COLUMN_WIDTH_LARGE * 2;

	// @formatter:off
    private static final List<String> CONTENT_TYPES = Arrays.asList(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.template",
            "application/vnd.ms-excel.sheet.macroEnabled.12",
            "application/vnd.ms-excel.template.macroEnabled.12",
            "application/vnd.ms-excel.addin.macroEnabled.12",
            "application/vnd.ms-excel.sheet.binary.macroEnabled.12"
            );
    // @formatter:on

	private static final List<String> EXTENSIONS = Arrays.asList("xls", "xlt", "xla", "xlsx", "xltx", "xlsm", "xltm",
			"xlam", "xlsb");

	// EXPORT
	public static final CellStyle copyStyle(Workbook workbook, CellStyle cellStyle) {
		CellStyle cs = workbook.createCellStyle();

		cs.setFont(workbook.getFontAt(cellStyle.getFontIndex()));

		cs.setAlignment(cellStyle.getAlignment());
		cs.setVerticalAlignment(cellStyle.getVerticalAlignment());

		cs.setBorderBottom(cellStyle.getBorderBottom());
		cs.setBorderTop(cellStyle.getBorderTop());
		cs.setBorderLeft(cellStyle.getBorderLeft());
		cs.setBorderRight(cellStyle.getBorderRight());

		return cs;
	}

	public static boolean isExcelFile(String fileContentType, String fileName) {
		if (fileContentType == null) {
			return false;
		}

		if (!CONTENT_TYPES.contains(fileContentType)) {
			return false;
		}

		String ext = FilenameUtils.getExtension(fileName);
		if (ext != null && !EXTENSIONS.contains(ext.toLowerCase())) {
			return false;
		}

		return true;
	}

	public static final CellStyle getCellStyle(Workbook workbook, boolean bold, boolean border, short align) {
		return getCellStyle(workbook, bold, border, align, (short) 12, IndexedColors.BLACK);
	}

	public static final CellStyle getCellStyle(Workbook workbook, boolean bold, boolean border, short align,
			short fontSize, IndexedColors fontColor) {
		Font font = workbook.createFont();
		if (bold) {
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		font.setFontName("Arial");
		font.setFontHeightInPoints(fontSize);
		font.setColor(fontColor.getIndex());

		CellStyle cs = workbook.createCellStyle();
		cs.setFont(font);
		cs.setAlignment(align);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		if (border) {
			cs.setBorderBottom(CellStyle.BORDER_THIN);
			cs.setBorderTop(CellStyle.BORDER_THIN);
			cs.setBorderLeft(CellStyle.BORDER_THIN);
			cs.setBorderRight(CellStyle.BORDER_THIN);
		}

		return cs;
	}

	public static final CellStyle getCellStyle(Workbook workbook, boolean bold, boolean border) {
		return getCellStyle(workbook, bold, border, CellStyle.ALIGN_LEFT);
	}

	public static final XSSFCellStyle getCellStyle(XSSFWorkbook workbook, boolean bold, boolean border) {
		return (XSSFCellStyle) getCellStyle((Workbook) workbook, bold, border);
	}

	public static final XSSFCellStyle getNumberStyle(XSSFWorkbook workbook, boolean bold, boolean border) {
		XSSFCellStyle cellStyle = (XSSFCellStyle) getCellStyle((Workbook) workbook, bold, border);
		XSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("#,##0.00"));
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		return cellStyle;
	}

	public static final XSSFCellStyle getVNDCurrencyStyle(XSSFWorkbook workbook, boolean bold, boolean border) {
		XSSFCellStyle cellStyle = (XSSFCellStyle) getCellStyle((Workbook) workbook, bold, border);
		XSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("#,##0"));
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		return cellStyle;
	}

	public static final XSSFCellStyle getIntegerStyle(XSSFWorkbook workbook, boolean bold, boolean border) {
		XSSFCellStyle cellStyle = (XSSFCellStyle) getCellStyle((Workbook) workbook, bold, border);
		XSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("#,##0"));
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		return cellStyle;
	}

	public static final Cell createCell(Row row, int column, CellStyle cs, String value) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(cs);
		cell.setCellValue(value);
		return cell;
	}

	public static final Cell createCell(Row row, int column, CellStyle cs, Long value) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(cs);
		if (value != null) {
			cell.setCellValue(value);
		} else {
			cell.setCellValue((String) null);
		}

		return cell;
	}

	public static final Cell createCell(Row row, int column, CellStyle cs, Integer value) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(cs);
		if (value != null) {
			cell.setCellValue(value);
		} else {
			cell.setCellValue((String) null);
		}

		return cell;
	}

	public static final Cell createCell(Row row, int column, CellStyle cs, Double value) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(cs);
		if (value != null) {
			cell.setCellValue(value);
		} else {
			cell.setCellValue((String) null);
		}

		return cell;
	}

	public static final Cell createCell(Row row, int column, CellStyle cs, BigDecimal value) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(cs);
		if (value != null) {
			cell.setCellValue(value.doubleValue());
		} else {
			cell.setCellValue("");
		}

		return cell;
	}

	public static final Cell createCell(Row row, int column, CellStyle cs, Boolean value) {
		Cell cell = row.createCell(column);
		cell.setCellStyle(cs);
		cell.setCellValue(value != null && value ? "x" : "");
		return cell;
	}

	public static final Cell createHeaderCell(Sheet sheet, Row row, int column, CellStyle cs, String value,
			Integer width) {
		Cell cell = createCell(row, column, cs, value);
		if (width != null) {
			sheet.setColumnWidth(column, width);
		}
		return cell;
	}

	public static final String getCellValue(XSSFCell cell) {
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return null;
		}

		try {
			String value = cell.getStringCellValue();
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			return value.trim();
		} catch (IllegalStateException e1) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Fail to get cell value", e1);
			}
		}

		try {
			double valueDouble = cell.getNumericCellValue();
			String pattern = "###.############";
			DecimalFormat decimalFormat = new DecimalFormat(pattern);

			String value = decimalFormat.format(valueDouble);
			if (value.endsWith(".0")) {
				value = value.substring(0, value.length() - 2);
			}
			return value;
		} catch (IllegalStateException | NumberFormatException e2) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Fail to convert cell value", e2);
			}
			return null;
		}
	}

	public static <DOMAIN extends Serializable> long exportData(ExportAction<DOMAIN> exportQuery, Workbook workbook) {
		long count = exportQuery.count();
		long nbQuery = (count / NUMBER_RECORD_QUERY_WHEN_EXPORT)
				+ ((count % NUMBER_RECORD_QUERY_WHEN_EXPORT) == 0 ? 0 : 1);

		for (int page = 1; page <= nbQuery; page++) {
			PageNumberRequest pageRequest = new PageNumberRequest((int) NUMBER_RECORD_QUERY_WHEN_EXPORT, page - 1);
			List<? extends DOMAIN> domains = exportQuery.getList(pageRequest);

			Function<List<? extends DOMAIN>, List<? extends DOMAIN>> beforeConvertFunction = exportQuery
					.getBeforeConvertFunction();
			if (beforeConvertFunction != null) {
				domains = beforeConvertFunction.apply(domains);
			}

			int preIndex = (int) ((page - 1) * NUMBER_RECORD_QUERY_WHEN_EXPORT);

			for (int i = 0, length = domains.size(); i < length; i++) {
				DOMAIN domain = domains.get(i);
				exportQuery.createRow(domain, preIndex + i);
			}
		}

		return count;
	}

	public static interface ExportAction<DOMAIN extends Serializable> {

		public default Function<List<? extends DOMAIN>, List<? extends DOMAIN>> getBeforeConvertFunction() {
			return null;
		}

		public long count();

		public List<? extends DOMAIN> getList(PageNumberRequest pageRequest);

		public void createRow(DOMAIN domain, int index);

	}

	public static FileDownloadDto getErrorMessageFile(String errorMessage) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Error");

		CellStyle headerCS = ExcelUtils.getCellStyle(workbook, true, false);
		headerCS.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerCS.setFillForegroundColor(IndexedColors.RED.getIndex());

		XSSFRow row = sheet.createRow(0);

		ExcelUtils.createHeaderCell(sheet, row, 0, headerCS, errorMessage, ExcelUtils.EXCEL_COLUMN_WIDTH_EXTRA_LARGE);

		return ExcelUtils.getFileDownloadDto(workbook, "Error.xlsx");
	}

	// IMPORT
	public static final String getValidExcelSourceName(String sourceName) {
		Assert.isTrue(!StringUtils.isEmpty(sourceName), "sourceName not empty");

		sourceName = sourceName.replace(" ", "_");
		sourceName = sourceName.replace("-", "_");
		sourceName = sourceName.replace("+", "_");
		sourceName = sourceName.replace("'", "_");

		return sourceName;
	}

	public static final String getValidSourceNameFormula(String cellName) {
		String formula = getExcelSubtituteFormula(cellName, " ", "_");

		formula = getExcelSubtituteFormula(formula, "-", "_");
		formula = getExcelSubtituteFormula(formula, "+", "_");
		formula = getExcelSubtituteFormula(formula, "'", "_");

		return formula;
	}

	public static final String getExcelSubtituteFormula(String value, String oldText, String newText) {
		return String.format("SUBSTITUTE(%s, \"%s\", \"%s\")", value, oldText, newText);
	}

	public static final Name createDataValidationSource(XSSFWorkbook workbook, String sourceName,
			String sourceSheetName, int firstRow, int lastRow, int firstCol, int lastCol) {
		Name excelName = workbook.createName();
		excelName.setNameName(sourceName);

		String firstColName = CellReference.convertNumToColString(firstCol);
		String lastColName = CellReference.convertNumToColString(lastCol);

		String reference = String.format("%s!$%s$%s:$%s$%s", sourceSheetName, firstColName, firstRow + 1, lastColName,
				lastRow + 1);

		LOGGER.debug(String.format("Add Name: [%s] [%s]", sourceName, reference));

		excelName.setRefersToFormula(reference);

		return excelName;
	}

	public static final void addDataValidationToSheet(XSSFSheet dataSheet, String sourceName, int firstRow,
			Integer lastRow, int firstCol, int lastCol) {
		lastRow = lastRow == null ? firstRow + MAXIMUM_NUMBER_OF_RECORD_PER_IMPORT : lastRow;

		CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(dataSheet);
		XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
				.createFormulaListConstraint(sourceName);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
		validation.setShowErrorBox(true);
		dataSheet.addValidationData(validation);
	}

	public static final <T> void createHorizontalValidationSource(List<T> domains, XSSFWorkbook workbook,
			XSSFSheet sourceSheet, int sourceRowIndex, Function<Integer, String> getSourceNameFn,
			Function<T, String> getDisplayNameFn) {
		XSSFRow row = sourceSheet.createRow(sourceRowIndex);

		int colIndex = 0;
		for (T domain : domains) {
			row.createCell(colIndex).setCellValue(getDisplayNameFn.apply(domain));
			colIndex++;
		}

		String sourceName = getSourceNameFn.apply(sourceRowIndex);

		ExcelUtils.createDataValidationSource(workbook, sourceName, sourceSheet.getSheetName(), sourceRowIndex,
				sourceRowIndex, 0, colIndex - 1);
	}

	public static final <T extends CategoryDto> Map<String, T> getCategoryByCodeMap(List<T> categories) {
		HashMap<String, T> categoryByName = new HashMap<>();
		for (T category : categories) {
			categoryByName.put(category.getCode().toUpperCase(), category);
		}

		return categoryByName;
	}

	public static final SheetData readExcel(File file, int sheetIndex, int startRow, int nbColumn) {
		XSSFWorkbook wb = null;
		try {
			InputStream inputStream = new FileInputStream(file);

			wb = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = wb.getSheetAt(sheetIndex);
			int lastRowNum = sheet.getLastRowNum();
			int nbRows = lastRowNum + 1 - startRow;

			List<List<String>> rows = new ArrayList<>(nbRows);

			for (int i = startRow; i <= lastRowNum; i++) {
				XSSFRow _row = sheet.getRow(i);
				ArrayList<String> row = new ArrayList<>();
				rows.add(row);
				for (int j = 0; j < nbColumn; j++) {
					XSSFCell cell = _row != null ? _row.getCell(j) : null;
					row.add(getCellValue(cell));
				}
			}

			return new SheetData(sheet.getSheetName(), rows);
		} catch (IOException e) {
			LOGGER.error("error when read excel for import", e);
			throw new UnsupportedOperationException(e);
		} catch (POIXMLException e) {
			throw new BusinessException(BusinessExceptionCode.INVALID_EXCEL, "it's not excel file", e);
		} finally {
			IOUtils.closeQuietly(wb);
		}
	}

	public static final File createExcelWithError(File importFile, int sheetIndex, SheetVerifyData sheetVerifyData) {
		XSSFWorkbook workbook = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(importFile);

			workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

			CreationHelper factory = workbook.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();

			for (int i = 0; i < sheetVerifyData.getNbRows(); i++) {
				int rowIndex = sheetVerifyData.getStartRowIndex() + i;
				XSSFRow _row = sheet.getRow(rowIndex);
				if (_row != null) {
					for (int j = 0; j < sheetVerifyData.getNbColumns(); j++) {
						int columnIndex = sheetVerifyData.getStartColumnIndex() + j;

						XSSFCell cell = _row.getCell(columnIndex);
						if (cell == null) {
							cell = _row.createCell(columnIndex);
						}

						if (sheetVerifyData.getErrors()[i][j]) {
							XSSFCellStyle oldCS = cell.getCellStyle();
							CellStyle newCS = ExcelUtils.copyStyle(workbook, oldCS);
							newCS.setFillPattern(CellStyle.SOLID_FOREGROUND);
							newCS.setFillForegroundColor(IndexedColors.RED.getIndex());
							cell.setCellStyle(newCS);

							String errorMessage = sheetVerifyData.getErrorMessages()[i][j];
							if (!StringUtils.isEmpty(errorMessage)) {
								Comment comment = cell.getCellComment();
								if (comment == null) {
									ClientAnchor anchor = factory.createClientAnchor();
									anchor.setCol1(cell.getColumnIndex() + 1);
									anchor.setCol2(cell.getColumnIndex() + 3);
									anchor.setRow1(_row.getRowNum());
									anchor.setRow2(_row.getRowNum() + 5);

									// Create the comment and set the text+author
									comment = drawing.createCellComment(anchor);
								}

								RichTextString richTextString = factory.createRichTextString(errorMessage);
								comment.setString(richTextString);

								// Assign the comment to the cell
								cell.setCellComment(comment);
							}
						} else {
							XSSFCellStyle oldCS = cell.getCellStyle();
							if (oldCS.getFillForegroundColor() == IndexedColors.RED.getIndex()) {
								CellStyle newCS = ExcelUtils.copyStyle(workbook, oldCS);

								newCS.setFillPattern(CellStyle.SOLID_FOREGROUND);
								newCS.setFillForegroundColor(IndexedColors.WHITE.getIndex());

								cell.setCellStyle(newCS);
							}

							cell.removeCellComment();
						}
					}
				}

			}

			return getFile(workbook);
		} catch (IOException e) {
			LOGGER.error("error when read excel for import", e);
			throw new UnsupportedOperationException(e);
		} catch (POIXMLException e) {
			LOGGER.error("it's not excel file", e);
			throw new BusinessException(BusinessExceptionCode.INVALID_EXCEL, "it's not excel file", e);
		} finally {
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(inputStream);
		}
	}

	public static final ImportVerifyDto verify(File importFile, int sheetIndex, int startRowIndex, int nbColumn,
			RowVerifier rowVerifier, ExcelErrorMessageFactory errorMessageFactory,
			ImportFileSubService importFileSubService) {
		try {
			String importFilePath = importFileSubService.storeFile(importFile, "IMPORT_", null);

			SheetData sheetData = ExcelUtils.readExcel(importFile, sheetIndex, startRowIndex, nbColumn);

			SheetVerifyData sheetVerifyData = new SheetVerifyData(startRowIndex, 0, nbColumn,
					sheetData.getRows().size(), errorMessageFactory);
			int rowIndex = startRowIndex - 1;
			for (List<String> row : sheetData.getRows()) {
				rowIndex++;
				rowVerifier.verifyRow(sheetVerifyData, row, rowIndex);
			}

			File errorFile = ExcelUtils.createExcelWithError(importFile, sheetIndex, sheetVerifyData);
			String errorFilePath = importFileSubService.storeFile(errorFile, "IMPORT_ERROR_", null);
			String errorFileUrl = importFileSubService.getUrl(errorFilePath);

			ImportVerifyDto dto = new ImportVerifyDto();
			dto.setNbRows(sheetVerifyData.getNbRows());
			dto.setNbErrorRows(sheetVerifyData.getErrorRowIndexes().size());
			dto.setImportFilePath(importFilePath);
			dto.setErrorFileUrl(errorFileUrl);
			return dto;
		} catch (IOException e) {
			LOGGER.error("cannot store file", e);
			throw new UnsupportedOperationException(e);
		}
	}

	public static class SheetData {
		private String sheetName;
		private List<List<String>> rows;

		public SheetData(String sheetName, List<List<String>> rows) {
			super();

			this.sheetName = sheetName;
			this.rows = rows;
		}

		public String getSheetName() {
			return sheetName;
		}

		public List<List<String>> getRows() {
			return rows;
		}

	}

	public static class SheetVerifyData {
		private int startRowIndex;
		private int startColumnIndex;
		private int nbRows;
		private int nbColumns;
		private boolean[][] errors;
		private String[][] errorMessages;
		private Set<Integer> errorRowIndexes;
		private ExcelErrorMessageFactory errorMessageFactory;

		public SheetVerifyData(int startRowIndex, int startColumnIndex, int nbColumns, int nbRows,
				ExcelErrorMessageFactory errorMessageFactory) {
			super();
			this.startRowIndex = startRowIndex;
			this.startColumnIndex = startColumnIndex;
			this.nbRows = nbRows;
			this.nbColumns = nbColumns;

			this.errors = new boolean[nbRows][nbColumns];
			this.errorMessages = new String[nbRows][nbColumns];
			this.errorRowIndexes = new HashSet<>();
			this.errorMessageFactory = errorMessageFactory;
		}

		public int getStartRowIndex() {
			return startRowIndex;
		}

		public int getStartColumnIndex() {
			return startColumnIndex;
		}

		public int getNbRows() {
			return nbRows;
		}

		public int getNbColumns() {
			return nbColumns;
		}

		public boolean[][] getErrors() {
			return errors;
		}

		public String[][] getErrorMessages() {
			return errorMessages;
		}

		public boolean isError(int rowIndex, int columnIndex) {
			return this.errors[rowIndex - startRowIndex][columnIndex - startColumnIndex];
		}

		public boolean isError(int rowIndex) {
			for (boolean error : this.errors[rowIndex - startRowIndex]) {
				if (error) {
					return true;
				}
			}
			return false;
		}

		public boolean isError() {
			return getErrorRowIndexes().size() > 0;
		}

		public Set<Integer> getErrorRowIndexes() {
			return errorRowIndexes;
		}

		public void addError(int rowIndex, int columnIndex, String errorMessage) {
			this.errorRowIndexes.add(rowIndex);
			this.errors[rowIndex - startRowIndex][columnIndex - startColumnIndex] = true;
			if (!StringUtils.isEmpty(errorMessage)) {
				String _errorMessage = this.errorMessages[rowIndex - startRowIndex][columnIndex - startColumnIndex];
				if (_errorMessage != null) {
					_errorMessage = _errorMessage + " - " + errorMessage;
				} else {
					_errorMessage = errorMessage;
				}
				this.errorMessages[rowIndex - startRowIndex][columnIndex - startColumnIndex] = _errorMessage;
			}
		}

		public String checkText(int rowIndex, int columnIndex, List<String> row, boolean mandatory, Integer minLength,
				Integer maxLength) {
			Assert.notNull(row, "row cannot null");
			if (minLength != null && maxLength != null) {
				Assert.isTrue(minLength <= maxLength, "minLength > maxLength");
			}

			String value = row.get(columnIndex);

			if (mandatory) {
				if (StringUtils.isEmpty(value)) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMandatoryErrorMessage(columnIndex));
					return null;
				}
			}

			if (!StringUtils.isEmpty(value)) {
				value = value.trim();

				if (minLength != null && value.length() < minLength) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getMinLengthErrorMessage(columnIndex, minLength));
				}

				if (maxLength != null && value.length() > maxLength) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getMaxLengthErrorMessage(columnIndex, maxLength));
				}

				return value;
			}

			return null;
		}

		public String checkUsername(int rowIndex, int columnIndex, List<String> row, boolean mandatory,
				Integer minLength, Integer maxLength) {
			Assert.notNull(row, "row cannot null");
			if (minLength != null && maxLength != null) {
				Assert.isTrue(minLength <= maxLength, "minLength > maxLength");
			}

			String value = row.get(columnIndex);

			if (mandatory) {
				if (StringUtils.isEmpty(value)) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMandatoryErrorMessage(columnIndex));
					return null;
				}
			}

			if (!StringUtils.isEmpty(value)) {
				value = value.trim();

				if (minLength != null && maxLength != null
						&& (value.length() < minLength || value.length() > maxLength)) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getLengthErrorMessage(columnIndex, minLength, maxLength));
					return null;
				}

				Matcher matcher = VALID_CODE_REGEX.matcher(value);

				if (!matcher.find()) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getUsernameErrorMessage(columnIndex, minLength, maxLength));
					return null;
				}

				return value;
			}

			return null;
		}

		public String checkPassword(int rowIndex, int columnIndex, List<String> row, boolean mandatory,
				Integer minLength, Integer maxLength) {
			Assert.notNull(row, "row cannot null");
			if (minLength != null && maxLength != null) {
				Assert.isTrue(minLength <= maxLength, "minLength > maxLength");
			}

			String value = row.get(columnIndex);

			if (mandatory) {
				if (StringUtils.isEmpty(value)) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMandatoryErrorMessage(columnIndex));
					return null;
				}
			}

			if (!StringUtils.isEmpty(value)) {
				value = value.trim();

				if (minLength != null && maxLength != null
						&& (value.length() < minLength || value.length() > maxLength)) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getLengthErrorMessage(columnIndex, minLength, maxLength));
					return null;
				}

				if (!PasswordUtils.isValidPassword(value)) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getPasswordErrorMessage(columnIndex, minLength, maxLength));
					return null;
				}

				return value;
			}

			return null;
		}

		public BigDecimal checkNumber(int rowIndex, int columnIndex, List<String> row, boolean mandatory, int scale,
				BigDecimal min, boolean includeMin, BigDecimal max, boolean includeMax) {
			Assert.notNull(row, "row cannot null");

			BigDecimal value = ExcelUtils.convertToBigDecimal(row.get(columnIndex), scale);

			if (mandatory) {
				if (value == null) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMandatoryErrorMessage(columnIndex));
				}
			}

			if (value != null) {
				if (min != null && (value.compareTo(min) < 0 || (!includeMin && value.compareTo(min) == 0))) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMinErrorMessage(columnIndex, min));
				}

				if (max != null && (value.compareTo(max) > 0 || (!includeMax && value.compareTo(max) == 0))) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMaxErrorMessage(columnIndex, max));
				}
			}

			return value;
		}

		public BigDecimal checkNumber(int rowIndex, int columnIndex, List<String> row, boolean mandatory, int scale,
				BigDecimal min, BigDecimal max) {
			return checkNumber(rowIndex, columnIndex, row, mandatory, scale, min, true, max, true);
		}

		public BigDecimal checkNumber(int rowIndex, int columnIndex, List<String> row, boolean mandatory, Integer min,
				Integer max) {
			return checkNumber(rowIndex, columnIndex, row, mandatory, 0, min == null ? null : new BigDecimal(min),
					max == null ? null : new BigDecimal(max));
		}

		public String checkPhone(int rowIndex, int columnIndex, List<String> row, boolean mandatory, Integer minLength,
				Integer maxLength) {
			Assert.notNull(row, "row cannot null");
			if (minLength != null && maxLength != null) {
				Assert.isTrue(minLength <= maxLength, "minLength > maxLength");
			}

			String value = row.get(columnIndex);

			if (mandatory) {
				if (StringUtils.isEmpty(value)) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMandatoryErrorMessage(columnIndex));
					return null;
				}
			}

			if (!StringUtils.isEmpty(value)) {
				value = value.trim();

				Matcher matcher = VALID_PHONE_REGEX.matcher(value);

				if (!matcher.find()) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getPhoneErrorMessage(columnIndex, minLength, maxLength));
					return null;
				}

				return value;
			}

			return null;
		}

		public String checkEmail(int rowIndex, int columnIndex, List<String> row, boolean mandatory, Integer minLength,
				Integer maxLength) {
			Assert.notNull(row, "row cannot null");
			if (minLength != null && maxLength != null) {
				Assert.isTrue(minLength <= maxLength, "minLength > maxLength");
			}

			String value = row.get(columnIndex);

			if (mandatory) {
				if (StringUtils.isEmpty(value)) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMandatoryErrorMessage(columnIndex));
					return null;
				}
			}

			if (!StringUtils.isEmpty(value)) {
				value = value.trim();

				if (minLength != null && maxLength != null
						&& (value.length() < minLength || value.length() > maxLength)) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getLengthErrorMessage(columnIndex, minLength, maxLength));
					return null;
				}

				Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(value);

				if (!matcher.find()) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getEmailErrorMessage(columnIndex, minLength, maxLength));
					return null;
				}

				return value;
			}

			return null;
		}

		public <T> T checkCategory(int rowIndex, int columnIndex, List<String> row, boolean mandatory,
				Map<String, T> categoryMap) {
			return checkCategory(rowIndex, columnIndex, row, mandatory, categoryMap, new Function<String, String>() {

				@Override
				public String apply(String value) {
					return value.trim().toUpperCase();
				}

			});
		}

		public <T> T checkCategory(int rowIndex, int columnIndex, List<String> row, boolean mandatory,
				Map<String, T> categoryMap, Function<String, String> transformValueFunction) {
			Assert.notNull(row, "row cannot null");

			String value = row.get(columnIndex);

			if (mandatory) {
				if (StringUtils.isEmpty(value)) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMandatoryErrorMessage(columnIndex));
					return null;
				}
			}

			if (!StringUtils.isEmpty(value)) {
				value = transformValueFunction.apply(value);
				T category = CollectionUtils.isEmpty(categoryMap) ? null : categoryMap.get(value);

				if (category == null) {
					addError(rowIndex, columnIndex,
							this.errorMessageFactory.getCategoryNotFoundErrorMessage(columnIndex, value));
				}

				return category;
			}

			return null;
		}

		public <T> Set<T> checkCategoryList(int rowIndex, int columnIndex, List<String> row, boolean mandatory,
				Map<String, T> categoryMap) {
			return checkCategoryList(rowIndex, columnIndex, row, mandatory, categoryMap,
					new Function<String, String>() {

						@Override
						public String apply(String value) {
							return value.trim().toUpperCase();
						}

					});
		}

		public <T> Set<T> checkCategoryList(int rowIndex, int columnIndex, List<String> row, boolean mandatory,
				Map<String, T> categoryMap, Function<String, String> transformValueFunction) {
			Assert.notNull(row, "row cannot null");

			String value = row.get(columnIndex);
			String[] values = StringUtils.commaDelimitedListToStringArray(value);

			if (mandatory) {
				if (values.length == 0) {
					addError(rowIndex, columnIndex, this.errorMessageFactory.getMandatoryErrorMessage(columnIndex));
					return null;
				}
			}

			if (values.length > 0) {
				Set<T> results = new HashSet<>(values.length);
				for (int i = 0; i < values.length; i++) {
					String singleValue = transformValueFunction.apply(values[i]);
					T category = CollectionUtils.isEmpty(categoryMap) ? null : categoryMap.get(singleValue);

					if (category == null) {
						addError(rowIndex, columnIndex,
								this.errorMessageFactory.getCategoryNotFoundErrorMessage(columnIndex, singleValue));
						return null;
					}
					results.add(category);
				}

				return results;
			}

			return null;
		}

	}

	public static interface RowVerifier {

		public void verifyRow(SheetVerifyData sheetVerifyData, List<String> row, int rowIndex);

	}

	public static interface ExcelErrorMessageFactory {

		public String getMandatoryErrorMessage(int columnIndex);

		public String getPasswordErrorMessage(int columnIndex, Integer minLength, Integer maxLength);

		public String getEmailErrorMessage(int columnIndex, Integer minLength, Integer maxLength);

		public String getPhoneErrorMessage(int columnIndex, Integer minLength, Integer maxLength);

		public String getMinLengthErrorMessage(int columnIndex, int minLength);

		public String getMaxLengthErrorMessage(int columnIndex, int maxLength);

		public String getMinErrorMessage(int columnIndex, BigDecimal min);

		public String getMaxErrorMessage(int columnIndex, BigDecimal max);

		public String getCategoryNotFoundErrorMessage(int columnIndex, String value);

		public String getUsernameErrorMessage(int columnIndex, int minLength, int maxLength);

		public String getLengthErrorMessage(int columnIndex, int minLength, int maxLength);

	}

	public static final BigDecimal convertToBigDecimal(String number, int scale) {
		if (number != null) {
			try {
				BigDecimal _number = new BigDecimal(number);
				_number = _number.setScale(scale, RoundingMode.HALF_UP);
				return _number;
			} catch (NumberFormatException ex) {
				return null;
			}
		}

		return null;
	}

	// **********************************
	// **********************************
	// **********************************

	// BASE
	public static final FileDownloadDto getFileDownloadDto(Workbook workbook, String fileName) {
		Assert.notNull(fileName, "fileName cannot be null");

		fileName = StringUtils.getFileName(fileName).replace(" ", "_");

		File outTempFile = getFile(workbook);
		return new FileDownloadDto(fileName, outTempFile);
	}

	public static final File getFile(Workbook workbook) {
		FileOutputStream outputStream = null;
		try {
			File outTempFile = File.createTempFile(UUID.randomUUID().toString(), "tmp");
			outputStream = new FileOutputStream(outTempFile);
			workbook.write(outputStream);

			return outTempFile;
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		} finally {
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(outputStream);
		}
	}

}
