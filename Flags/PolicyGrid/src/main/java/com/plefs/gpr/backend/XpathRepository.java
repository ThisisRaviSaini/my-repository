package com.plefs.gpr.backend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class XpathRepository {
	public WebElement element = null;
	// @ Xpath from TC 21 to 45
	public static By by_Url = By.linkText("https://devsso.secure.fedex.com/FlagsL2Safe/reports/#/");
	public static By by_User_name = By.xpath("//input[@class='login-email']");
	public static By by_Password = By.xpath("//input[@class='login-password']");
	public static By by_Submit_button = By.xpath("//input[@class='login submit-button']");
	public static By by_homePage = By.xpath("//*[text()='Ravi Saini']");
	public static By by_URSAUVDSKfedExOnsiteLink = By.xpath("//*[@class='active']");
	public static By by_GprExtractReportLink = By.xpath("//*[@href='#/policy-create']");
	public static By by_PolicyGridMonthlyProcessHeader = By.xpath("//*[@class='header']");
	public static By by_Screenname=By.xpath("//h1[text()='Policy Grid Monthly Processes']");
	public static By by_PolicyGridDropDown = By.xpath("//*[@id='extract-constraints']");
	public static By by_CreateExtractButton = By.xpath("//*[@id='create-extract-button']");
	public static By by_WarningMessage = By.xpath("//*[@id='warning-message']");
	public static By by_ExtractTypeTableHeader = By.xpath("//*[@class='column-header column-fileTypeCd sortable']");
	public static By by_DateCreatedTableHeader = By.xpath("//*[@class='column-header column-lastUpdateTmstpDate sortable resizable']");
	public static By by_TimeCreatedTableHeader = By.xpath("//*[@class='column-header column-lastUpdateTmstpTime sortable resizable']");
	public static By by_ExtractIDTableHeader = By.xpath("//*[@class='column-header column-fileStorageSeqNbr sortable resizable']");
	public static By by_FileNameTableHeader = By.xpath("//*[@class='column-header column-displayFileNm sortable']");

	public static By by_ApprovalStatusExtractTable = By.xpath("//td[@class='data-column column-statusCd']");
	public static By by_CompareSelectedButton = By.xpath("//*[@id='compare-selected-button']");
	public static By by_SuccessMessage = By.xpath("//div[@id='success-message']");
	public static By by_NewExtractTypeCompareTable = By.xpath("//td[@class='data-column column-lastUpdateTmstpDate']");
	public static By by_LimitTxtBoxCompareTab = By.xpath("(//span[contains(text(),'Limit:')])[2]//following::input[1]");
	public static By by_RefreshbttnCompareTab = By.xpath("(//*[@class='btn btn-default btn-sm refresh-button'])[2]");
	public static By by_RefreshbttnExtractTab = By.xpath("(//*[@class='btn btn-default btn-sm refresh-button'])[1]");
	public static By by_ComparisionTab = By.xpath("(//table)[2]//tbody//tr");
	public static By by_SortBttnExtractId = By.xpath("//span[text()='Extract ID']//..//span[@class='column-sort-icon']");
	public static By by_SortBttnApprovalStatus = By.xpath("//span[text()='Approval Status']//..//span[@class='column-sort-icon']");
	public static By by_SortBttnExtractType = By.xpath("//span[text()='Extract Type']//..//span[@class='column-sort-icon']");
	public static By by_SortBttnDatecreated = By.xpath("(//span[text()='Date Created'])[1]//..//span[@class='column-sort-icon']");
	public static By by_SortBttnFileName = By.xpath("(//span[text()='File Name'])[1]//..//span[@class='column-sort-icon']");

	public static By by_SortBttnFedExId = By.xpath("(//span[text()='FedEx ID'])[1]//..//span[@class='column-sort-icon']");
	public static By by_PageNoTxtBoxExtractTab = By.xpath("(//h4[text()='Extracts']/../../..//input[@type='number'])[2]");
	public static By by_ResultsExtractTab = By.xpath("//h4[text()='Extracts']/../../..//div[@class='pagination-range'][1]//span[2]");
	public static By by_LastPageBttnExtractTab = By.xpath("//h4[contains(.,'Extracts')]/../../..//button[@class='btn btn-default pagination-lastpage'][1]");
	public static By by_TotalPageLableExtractTab = By.xpath("//h4[contains(.,'Extracts')]/../../..//div[@class='input-group-addon']//span[2]");
	public static By by_NextPageBttnExtractTab = By.xpath("//h4[contains(.,'Extracts')]/../../..//button[@class='btn btn-default pagination-nextpage']");
	public static By by_PrevPageBttnExtractTab = By.xpath("//h4[contains(.,'Extracts')]/../../..//button[@class='btn btn-default pagination-prevpage']");
	public static By by_FirstPageBttnExtractTab = By.xpath("//h4[contains(.,'Extracts')]/../../..//button[@class='btn btn-default pagination-firstpage']");
	public static By by_WaitTimeCreatedDataExtractTab = By.xpath("(//*[@class='data-column column-lastUpdateTmstpTime'])[1]");
	public static By by_WaitFileNameDataExtractTab = By.xpath("(//*[@class='data-column column-displayFileNm'])[1]");
	public static By by_ExtractIDDataExtractTab = By.xpath("(//*[@class='data-column column-fileStorageSeqNbr'])[1]");
	public static By by_TimeCreatedDataCompTab = By.xpath("(//*[@class='data-column column-createTmstpTime'])[1]//div[1]");
	public static By by_ReturnBttn = By.xpath("//input[@value='Return']");
	public static By by_ExtractTablastPageLable = By.xpath("(//div[@class='input-group-addon']/span[2])[1]");
	

	// @ Xpath from TC 46 to 45

	public static By by_Refresh_button = By.xpath("(//span[@class='glyphicon glyphicon-refresh'])[2]");
	public static By by_ExtractRefreshbutton = By.xpath("(//span[@class='glyphicon glyphicon-refresh'])[1]");
	public static By by_Filter_panel = By.xpath("//div[@class='column-selector-box panel panel-default']");
	public static By by_Filter_button_Comparision_tbl = By.xpath("(//span[@class='glyphicon glyphicon-list'])[2]");
	public static By by_ComparisionsTitle = By.xpath("//h4[contains(text(),'Comparison Reports')]");
	public static By by_rowcount = By.xpath("//*[@id='reports-table']//div//div//table//tbody//tr");
	public static By by_Extractrowcount = By.xpath("//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[4]");
	public static By by_Limittextbox = By.xpath("(//span[contains(text(),'Limit:')])[2]//following::input[1]");
	public static By by_ExtractLimittextbox = By.xpath("(//span[contains(text(),'Limit:')])[1]//following::input[1]");
	public static By by_Extractlimitcount = By.xpath("(//div[@class='pagination-range']//span[contains(.,'')][2])[1]");
	public static By by_ExtractTotallimitcount = By.xpath("(//div[@class='pagination-range']//span[contains(.,'')][3])[1]");
	public static By by_Comaprebutton = By.xpath("(//div)[6]/..//input[@value='Compare Selected']");
	public static By by_FirstNewExtractID = By.xpath("(//td[@class='data-column column-newFileExtractSeqNbr']//following::div)[1]");
	public static By by_FirstOldExtractID = By.xpath("(//td[@class='data-column column-oldFileExtractSeqNbr']//following::div)[1]");
	public static By by_ExtractIDCount = By.xpath("//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[7]");
	public static By by_ExtractAlreadyComparedMsg = By.xpath("//div[@class='alert alert-warning']");
	public static By by_ComparisionPageNumber = By.xpath("(//div[@class='input-group-addon'])[2]//preceding::input[1]");
	public static By by_Comparisionrecordcount = By.xpath("(//div[@class='pagination-range']//span[contains(.,'')][2])[2]");
	public static By by_Comparisionfirstpgbutton = By.xpath("(//button[@class='btn btn-default pagination-firstpage'])[2]");
	public static By by_Comparisionprevpgbutton = By.xpath("(//button[@class='btn btn-default pagination-prevpage'])[2]");
	public static By by_Comparisionnextpgbutton = By.xpath("(//button[@class='btn btn-default pagination-nextpage'])[2]");
	public static By by_Comparisionlastpgbutton = By.xpath("(//button[@class='btn btn-default pagination-lastpage'])[2]");
	public static By by_timeCreated = By.xpath("(//*[@class='data-column column-lastUpdateTmstpTime'])[1]");
	public static By by_ComparisionLimitCount = By.xpath("(//div[@class='pagination-range']//span[contains(.,'')][3])[2]");
	public static By by_selectReport = By.xpath("(//data-table[@id='reports-table']//div//div/table//tbody//tr//td//input)[1]");
	public static By by_viewSelected = By.xpath("//*[@value='View Selected Report']");
	public static By by_return = By.xpath("//*[@value='Return']");
	public static By by_resultsReports = By.xpath("(//*[@class='pagination-range'])[2]");
	public static By by_listButtonReports = By.xpath("(//*[@class='btn btn-default btn-sm column-selector-button'])[2]");
	public static By by_listButtonClicked = By.xpath("//*[@class='column-selector-box panel panel-default']");
	public static By by_unselectnewExtractURSAMonth = By.xpath("//*[@class='ng-untouched ng-pristine ng-valid']");
	public static By by_newExtractURSAMonth = By.xpath("//*[contains(text(),'New Extract URSA Month')]");
	public static By by_unselectNewExtracttype = By.xpath("(//span[contains(text(),'New Extract Type')])[1]");
	public static By by_NewExtracttypeheader = By.xpath("(//span[contains(text(),'New Extract Type')])[2]");
	public static By by_Clearselectionbutton = By.xpath("//input[@value='Clear Selections']");
	public static By by_Selectfirstcheckbox = By.xpath("(//input[@type='checkbox'])[2]");
	public static By by_resultsComparision = By.xpath("(//*[@class='pagination-range'])[2]");
	public static By by_firstdateComparision = By.xpath("(//td[@class='data-column column-createTmstpDate'])[1]");
	public static By by_firsttimeComparision = By.xpath("(//td[@class='data-column column-createTmstpTime'])[1]");

	// -------------------------------------------------------------------------------------------------

	public static By by_resultsExtracts = By.xpath("(//*[@class='pagination-range'])[1]");
	public static By by_listButtonExtracts = By.xpath("(//*[@class='btn btn-default btn-sm column-selector-button'])[1]");
	public static By by_listButtonClickedExtracts = By.xpath("//*[@class='column-selector-box panel panel-default']");
	public static By by_unselectExtractType = By.xpath("(//label//input[@type='checkbox'])[2]");
	public static By by_extractTypeHeader = By.xpath("(//*[contains(text(),'Extract Type')])[2]");
	public static By by_dateCreated = By.xpath("(//*[@class='data-column column-lastUpdateTmstpDate'])[1]");
	public static By by_sortExtractType = By.xpath("(//*[contains(text(),'New Extract Type')]/following-sibling::span)[1]");
	public static By by_sortOldExtractType = By.xpath("(//*[contains(text(),'Old Extract Type')]/following-sibling::span)[1]");
	public static By by_NewExtractTypeList = By.xpath("//*[@id='reports-table']//div//div//table//tbody//tr/td[4]//div");
	public static By by_OldExtractTypeList = By.xpath("//*[@id='reports-table']//div//div//table//tbody//tr/td[4]//div");
	public static By by_NewExtractIDList = By.xpath("//*[@class='data-column column-newFileExtractSeqNbr']");
	public static By by_OldExtractIDList = By.xpath("//*[@class='data-column column-oldFileExtractSeqNbr']");
	public static By by_ReportIDList = By.xpath("//*[@class='data-column column-fileCompareSeqNbr']");
	public static By by_StatusList = By.xpath("//span[text()='Status']/../../../..//div[text()='C' or text()='A'  or text()='F' or text()='IP']");
	public static By by_FedExIDList = By.xpath("//*[@class='data-column column-createUserNm']");
	public static By by_sortNewExtractID = By.xpath("(//*[contains(text(),'New Extract ID')]/following-sibling::span)[1]");
	public static By by_sortOldExtractID = By.xpath("(//*[contains(text(),'Old Extract ID')]/following-sibling::span)[1]");
	public static By by_sortFedExID = By.xpath("(//*[contains(text(),'FedEx ID')]/following-sibling::span)[3]");
	public static By by_sortReportID = By.xpath("(//*[contains(text(),'Report ID')]/following-sibling::span)[1]");
	public static By by_sortStatus = By.xpath("(//*[contains(text(),'Status')]/following-sibling::span)[3]");

	public static By by_loggedinuser = By.xpath("//a[@class='userInfo dropdown-toggle']");
	public static By by_GPRExtendreports = By.xpath("//a[@href='#/policy-create']");
	public static By by_GPRExtendreportstitle = By.xpath("//h1[contains(.,'Policy Grid Monthly Processes')]");
	public static By by_dropdownPG = By.xpath("//*[@id='extract-constraints']");
	public static By by_OfferingConstraintOption = By.xpath("//option[@value='OFFERING_CONSTRAINTS']");
	public static By by_OfferingDimConstraintOption = By.xpath("//option[@value='OFFERING_DIMENSION_CONSTRAINTS']");
	public static By by_successmsg = By.xpath("//div[@id='success-message']");
	public static By by_extractbtn = By.xpath("//input[@id='create-extract-button']");
	public static By by_firstrow = By.xpath("(//td[@class='data-column column-fileTypeCd'])[1]");
	//public static By by_warningmsg = By.xpath("//div[@id='warning-message']");
	public static By by_warningmsg = By.xpath("//div[@class='alert alert-warning' and @id='warning-message']");
	public static By by_records = By.xpath("(//div[@class='pagination-range']//span[contains(.,'')][3])[1]");
	public static By by_actualET = By.xpath("//*[@class='column-header column-fileTypeCd sortable']/span[1]");
	public static By by_actualDC = By.xpath("//*[@class='column-header column-lastUpdateTmstpDate sortable resizable']/span[1]");
	//public static By by_actualTC = By.xpath("//*[@class='column-header column-lastUpdateTmstpTime sortable resizable']/span[1]");
	public static By by_actualTC = By.xpath("//th[@class='column-header column-lastUpdateTmstpTime resizable']");
	public static By by_actualEI = By.xpath("//*[@class='column-header column-fileStorageSeqNbr sortable resizable']/span[1]");
	public static By by_actualFN = By.xpath("//*[@class='column-header column-displayFileNm sortable']/span[1]");
	public static By by_actualAS = By.xpath("//*[@class='column-header column-statusCd sortable resizable']/span[1]");
	public static By by_actualNI = By.xpath("//*[@class='column-header column-lastUpdateAgentNm sortable resizable']/span[1]");
	public static By by_actualDT = By.xpath("//*[@class='column-header column-approvalTmstpDateTime sortable resizable']/span[1]");
	public static By by_selectAll = By.xpath("//*[@class='select-column']");
	public static By by_clearSelection_button = By.xpath("//*[@id='clear-selections-button']");
	public static By by_selectOne = By.xpath("(//tbody[@class='data-table-row-wrapper']/tr/td/input)[1]");
	public static By by_dropbox_option = By.xpath("//span[contains(text(),'Drop Box')]");
	public static By by_dropbox_title = By.xpath("//div[@title='Dropbox report for the Location ']");
	public static By by_dropbox_Report_Msg = By.xpath("//div[@class='flex-item flex-align-start sf-element sf-element-text-box']");
	public static By by_LocInBldg = By.xpath("//div[contains(text(),'Loc In Bldg.')]");
	public static By by_Sort = By.xpath("//span[@title='Select sorting mode (Shift+click)']");
	public static By by_SortAscending = By.xpath("//div[@title='Sort ascending']");
	public static By by_LocationCd_text = By.xpath("//input[@class='sf-element sf-element-control sfc-property sfc-text-box']");
	public static By by_Search_btn = By.xpath("//input[@value='SEARCH']");
	public static By by_DateCreatedFirstRow = By.xpath("(//td[@class='data-column column-lastUpdateTmstpDate']//following::div)[1]");

	public static By by_CompareSelected_button = By.xpath("//input[@id='compare-selected-button']");
	public static By by_WarningMessage1 = By.xpath("//div[@id='warning-message']");
	public static By by_Success_WarningMessage = By.xpath("//div[@id='success-message']");
	public static By by_CheckBox_FirstRow = By.xpath("(//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[3])[1]");
	public static By by_CheckBox_SecondRow = By.xpath("(//data-table[@id='extracts-table']//div//div//table//tbody//tr//td[3])[2]");
	public static By by_ClearSelectionButton = By.xpath("//input[@id='clear-selections-button']");

	public static By by_records_ComparisonReport = By.xpath("(//div[@class='pagination-range']//span[contains(.,'')][3])[2]");

	public static By by_Time_Created = By.xpath("(//*[@class='data-column column-lastUpdateTmstpTime'])[1]");

	public static By by_Extract_Type_Asce = By.xpath("(//span[@class='glyphicon glyphicon-sort column-sortable-icon'])[1]");
	public static By by_Extract_Type_Desc = By.xpath("//h4[text()='Extracts']/../../..//span[text()='Extract Type']/..//span[@class='glyphicon glyphicon-triangle-bottom']");

	public static By by_Date_Created_Asce = By.xpath("(//span[@class='glyphicon glyphicon-sort column-sortable-icon'])[2]");
	public static By by_Date_Created_Desc = By.xpath("//h4[text()='Extracts']/../../..//span[text()='Date Created']/..//span[@class='glyphicon glyphicon-triangle-bottom']");

	public static By by_Time_Created_Asce = By.xpath("(//span[@class='glyphicon glyphicon-sort column-sortable-icon'])[3]");

	public static By by_Extract_ID_Asce = By.xpath("(//span[@class='glyphicon glyphicon-sort column-sortable-icon'])[4]");

	public static By by_File_Name_Asce = By.xpath("(//span[@class='glyphicon glyphicon-sort column-sortable-icon'])[5]");

	public static By by_Approval_Status_Asce = By.xpath("(//span[@class='glyphicon glyphicon-sort column-sortable-icon'])[6]");

	public static By by_FedEx_ID_Asce = By.xpath("(//span[@class='glyphicon glyphicon-sort column-sortable-icon'])[7]");

	public static By by_ApprovalStatus_TableHeader = By.xpath("(//td[@class='data-column column-statusCd'])[1]");
	public static By by_ApprovalStatus_IP_TableHeader = By.xpath("(//td[@class='data-column column-statusCd'])[1]");
	public static By by_New_Extract_id = By.xpath("(//*[@class='column-header column-newFileExtractSeqNbr sortable resizable'])");
	public static By by_Old_Extract_id = By.xpath("(//*[@class='column-header column-oldFileExtractSeqNbr sortable resizable'])");

	public static By by_listBttnDiffTable = By.xpath("//mat-button-toggle[contains(@id,'mat-button-toggle')]");
	public static By by_CheckboxDiffTable = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'COUNTRY CODE')])[1]/../../..//input[@type='checkbox'])[3]");
	public static By by_ViewSelectedBttn = By.xpath("//input[@value='View Selected Report']");
	public static By by_NewExtractTypeDataCompTab = By.xpath("//h4[text()='Comparison Reports']/../../..//span[text()='New Extract Type']/../../../..//td[4]");
	public static By by_CheckBoxComp = By.xpath("//h4[text()='Comparison Reports']/../../..//span[text()='New Extract Type']/../../../..//td[4]");
	public static By by_HeaderDiffTable = By.xpath("//th[@scope='colgroup']");
	public static By by_Lasttimecomparetblrecord = By.xpath("(//td[@class='data-column column-createTmstpDate'])[10]");
	public static By by_LastCheckbox = By.xpath("(//span[text()='New Extract Type']/../../../..//input[@type='checkbox'])[2]");
	public static By by_ViewSelectedReport_button = By.xpath("//input[@value='View Selected Report']");
	public static By by_ReportTitle = By.xpath("//h1[text()='Policy Grid Extract Monthly Processes']");
	public static By by_Subheader = By.xpath("//html//tr[2]/th");
	public static By by_MIN_LENGTH_PLUS_GIRTH_CM = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH PLUS GIRTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MIN_LENGTH_PLUS_GIRTH_CM_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH PLUS GIRTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MIN_LENGTH_PLUS_GIRTH_CM_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH PLUS GIRTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MIN_LENGTH_PLUS_GIRTH_CM_Arrow = By.xpath("//mat-panel-title[contains(.,'MIN LENGTH PLUS GIRTH CM')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MIN_LENGTH_PLUS_GIRTH_CM_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[23]");

	public static By by_MAX_LENGTH_PLUS_GIRTH_CM = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH PLUS GIRTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MAX_LENGTH_PLUS_GIRTH_CM_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH PLUS GIRTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MAX_LENGTH_PLUS_GIRTH_CM_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH PLUS GIRTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MAX_LENGTH_PLUS_GIRTH_CM_Arrow = By.xpath("//mat-panel-title[contains(.,'MAX LENGTH PLUS GIRTH CM')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MAX_LENGTH_PLUS_GIRTH_CM_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[24]");

	public static By by_MAX_WIDTH_CM = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX WIDTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MAX_WIDTH_CM_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX WIDTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MAX_WIDTH_CM_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX WIDTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MAX_WIDTH_CM_Arrow = By.xpath("//mat-panel-title[contains(.,'MAX WIDTH CM')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MAX_WIDTH_CM_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[22]");

	public static By by_MIN_WIDTH_CM = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN WIDTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MIN_WIDTH_CM_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN WIDTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MIN_WIDTH_CM_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN WIDTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MIN_WIDTH_CM_Arrow = By.xpath("//mat-panel-title[contains(.,'MIN WIDTH CM')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MIN_WIDTH_CM_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[21]");

	public static By by_MAX_HEIGHT_CM = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX HEIGHT CM')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MAX_HEIGHT_CM_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX HEIGHT CM')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MAX_HEIGHT_CM_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX HEIGHT CM')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MAX_HEIGHT_CM_Arrow = By.xpath("//mat-panel-title[contains(.,'MAX HEIGHT CM')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MAX_HEIGHT_CM_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[20]");

	public static By by_MIN_HEIGHT_CM = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN HEIGHT CM')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MIN_HEIGHT_CM_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN HEIGHT CM')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MIN_HEIGHT_CM_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN HEIGHT CM')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MIN_HEIGHT_CM_Arrow = By.xpath("//mat-panel-title[contains(.,'MIN HEIGHT CM')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MIN_HEIGHT_CM_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[19]");

	public static By by_MAX_LENGTH_CM = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MAX_LENGTH_CM_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MAX_LENGTH_CM_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MAX_LENGTH_CM_Arrow = By.xpath("//mat-panel-title[contains(.,'MAX LENGTH CM')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MAX_LENGTH_CM_Arrow_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[18]");

	public static By by_MIN_LENGTH_CM = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MIN_LENGTH_CM_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MIN_LENGTH_CM_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH CM')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MIN_LENGTH_CM_Arrow = By.xpath("//mat-panel-title[contains(.,'MIN LENGTH CM')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MIN_LENGTH_CM_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[17]");

	public static By by_MAX_LENGTH_PLUS_GIRTH_IN = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH PLUS GIRTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MAX_LENGTH_PLUS_GIRTH_IN_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH PLUS GIRTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MAX_LENGTH_PLUS_GIRTH_IN_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH PLUS GIRTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MAX_LENGTH_PLUS_GIRTH_IN_Arrow = By.xpath("//mat-panel-title[contains(.,'MAX LENGTH PLUS GIRTH IN')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MAX_LENGTH_PLUS_GIRTH_IN_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[16]");

	public static By by_MIN_LENGTH_PLUS_GIRTH_IN = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH PLUS GIRTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MIN_LENGTH_PLUS_GIRTH_IN_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH PLUS GIRTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MIN_LENGTH_PLUS_GIRTH_IN_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH PLUS GIRTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MIN_LENGTH_PLUS_GIRTH_IN_Arrow = By.xpath("//mat-panel-title[contains(.,'MIN LENGTH PLUS GIRTH IN')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MIN_LENGTH_PLUS_GIRTH_IN_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[15]");

	public static By by_MAX_WIDTH_IN = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX WIDTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MAX_WIDTH_IN_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX WIDTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MAX_WIDTH_IN_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX WIDTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MAX_WIDTH_IN_Arrow = By.xpath("//mat-panel-title[contains(.,'MAX WIDTH IN')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MAX_WIDTH_IN_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[14]");

	public static By by_MIN_WIDTH_IN = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN WIDTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MIN_WIDTH_IN_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN WIDTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MIN_WIDTH_IN_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN WIDTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MIN_WIDTH_IN_Arrow = By.xpath("//mat-panel-title[contains(.,'MIN WIDTH IN')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MIN_WIDTH_IN_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[13]");

	public static By by_MAX_HEIGHT_IN = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX HEIGHT IN')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MAX_HEIGHT_IN_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX HEIGHT IN')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MAX_HEIGHT_IN_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX HEIGHT IN')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MAX_HEIGHT_IN_Arrow = By.xpath("//mat-panel-title[contains(.,'MAX HEIGHT IN')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MAX_HEIGHT_IN_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[12]");

	public static By by_MIN_HEIGHT_IN = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN HEIGHT IN')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MIN_HEIGHT_IN_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN HEIGHT IN')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MIN_HEIGHT_IN_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN HEIGHT IN')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MIN_HEIGHT_IN_Arrow = By.xpath("//mat-panel-title[contains(.,'MIN HEIGHT IN')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MIN_HEIGHT_IN_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[11]");

	public static By by_MAX_LENGTH_IN = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MAX_LENGTH_IN_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MAX_LENGTH_IN_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MAX LENGTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MAX_LENGTH_IN_Arrow = By.xpath("//mat-panel-title[contains(.,'MAX LENGTH IN')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MAX_LENGTH_IN_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[10]");

	public static By by_MIN_LENGTH_IN = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_MIN_LENGTH_IN_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_MIN_LENGTH_IN_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'MIN LENGTH IN')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_MIN_LENGTH_IN_Arrow = By.xpath("//mat-panel-title[contains(.,'MIN LENGTH IN')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_MIN_LENGTH_IN_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[9]");

	public static By by_Permission = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'Permission')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_Permission_New = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'Permission')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_Permission_Old = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'Permission')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_Permission_Arrow = By.xpath("//mat-panel-title[contains(.,'Permission')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_Permission_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[8]");

	public static By by_SHIP_DATE = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SHIP DATE')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_SHIP_DATE_New = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SHIP DATE')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_SHIP_DATE_Old = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SHIP DATE')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_SHIP_DATE_Arrow = By.xpath("//mat-panel-title[contains(.,'SHIP DATE')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_SHIP_DATE_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[7]");

	public static By by_IS_DOMESTIC = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'IS DOMESTIC')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_IS_DOMESTIC_New = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'IS DOMESTIC')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_IS_DOMESTIC_Old = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'IS DOMESTIC')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_IS_DOMESTIC_Arrow = By.xpath("//mat-panel-title[contains(.,'IS DOMESTIC')]/../..//span[@style='transform: rotate(0deg);']");
	public static By by_IS_DOMESTIC_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[6]");

	public static By by_SERVICE_OPTION_IDS = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE OPTION IDS')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_SERVICE_OPTION_IDS_New_Policy_Grid = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE OPTION IDS')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_SERVICE_OPTION_IDS_New_Epic = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE OPTION IDS')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_SERVICE_OPTION_IDS_Old_Policy_Grid = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE OPTION IDS')])[1]/../../..//input[@type='checkbox']/../../..)[4]");

	public static By by_SERVICE_OPTION_IDS_Old_Epic = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE OPTION IDS')])[1]/../../..//input[@type='checkbox']/../../..)[5]");

	public static By by_SERVICE_OPTION_IDS_Arrow = By.xpath("//mat-panel-title[contains(.,'SERVICE OPTION IDS')]/../..//span[@style='transform: rotate(0deg);']");
	public static By by_SERVICE_OPTION_IDS_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[5]");

	public static By by_SERVICE_ID = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE ID')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_SERVICE_ID_New_Policy_Grid = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE ID')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_SERVICE_ID_New_Epic = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE ID')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_SERVICE_ID_Old_Policy_Grid = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE ID')])[1]/../../..//input[@type='checkbox']/../../..)[4]");

	public static By by_SERVICE_ID_Old_Epic = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'SERVICE ID')])[1]/../../..//input[@type='checkbox']/../../..)[5]");

	public static By by_SERVICE_ID_Arrow = By.xpath("//mat-panel-title[contains(.,'SERVICE ID')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_SERVICE_ID_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[4]");

	public static By by_ID = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'ID')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_ID_New = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'ID')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_ID_New_1 = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'ID')])[1]/../../..//input[@type='checkbox']/../../..//input)[2]");

	public static By by_ID_Old = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'ID')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_ID_Old_1 = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'ID')])[1]/../../..//input[@type='checkbox']/../../..//input)[3]");

	public static By by_ID_Arrow = By.xpath("//mat-panel-title[contains(.,'ID')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_ID_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[1]");

	public static By by_COUNTRY_CODE = By.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'COUNTRY CODE')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_COUNTRY_CODE_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'COUNTRY CODE')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_COUNTRY_CODE_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'COUNTRY CODE')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_COUNTRY_CODE_Arrow = By.xpath("//mat-panel-title[contains(.,'COUNTRY CODE')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_COUNTRY_CODE_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[2]");

	public static By by_STATE_OR_PROVINCE_CODE = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'STATE OR PROVINCE CODE')])[1]/../../..//input[@type='checkbox']/../../..)[1]");

	public static By by_STATE_OR_PROVINCE_CODE_New = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'STATE OR PROVINCE CODE')])[1]/../../..//input[@type='checkbox']/../../..)[2]");

	public static By by_STATE_OR_PROVINCE_CODE_Old = By
			.xpath("((//span[@class='mat-checkbox-label']/../../../..//mat-panel-title[contains(.,'STATE OR PROVINCE CODE')])[1]/../../..//input[@type='checkbox']/../../..)[3]");

	public static By by_STATE_OR_PROVINCE_CODE_Arrow = By.xpath("//mat-panel-title[contains(.,'STATE OR PROVINCE CODE')]/../..//span[@style='transform: rotate(0deg);']");

	public static By by_STATE_OR_PROVINCE_CODE_CheckBox = By.xpath("(//span/mat-panel-title/mat-checkbox)[3]");

	public static By by_ApprovalStatus = By.xpath("//*[@id='extracts-table']//td[@class='data-column column-statusCd']");
	public static By by_LastExtractRecordTime = By.xpath("(//td[@class='data-column column-lastUpdateTmstpTime'])[10]");
	public static By by_DenyButton = By.xpath("//input[@class='denyButton']");
	public static By by_ApproveBtn = By.xpath("//input[@value='Approve']");
	public static By by_ReportTable = By.xpath("//tbody");
	public static By by_ExtractTypeID = By.xpath("(//td[@class='data-column column-newFileExtractSeqNbr'])[1]");
	public static By by_FirstComparisonRecord = By.xpath("(//h4[text()='Comparison Reports']/../../..//input)[2]");
	public static By by_CompareSelectButton = By.xpath("//input[@type='button' and @value='Compare Selected']");
	public static By by_ReturnButton = By.xpath("//input[@value='Return']");

	public static By by_Approve_Button = By.xpath("//input[@class='processButton']");

	public static By by_approve_button_Success_Message = By.xpath("//div[@class='alert alert-success ng-star-inserted']");

	public static By by_Subheader_count = By.xpath("//tr[1]//th");
	
    public static By by_dateCreatedR=By.xpath("(//*[@class='data-column column-createTmstpDate'])[1]");
    public static By by_timeCreatedSort=By.xpath("(//*[contains(text(),'Time Created')]/following-sibling::span//span)[1]");
    public static By by_timeCreatedSortR=By.xpath("(//*[contains(text(),'Time Created')]/following-sibling::span//span)[2]");


	public static By by_select1 = By.xpath("(//*[contains(text(),'OFFERING_DIMENSION_CONSTRAINTS')])[2]");
	public static By by_select2 = By.xpath("(//*[contains(text(),'OFFERING_DIMENSION_CONSTRAINTS')])[4]");
	public static By by_selectReportA = By.xpath("(//*[@class='ng-untouched ng-pristine ng-valid'])[13]");
	public static By by_approveButton = By.xpath("//*[@value='Approve']");
	public static By by_ExtractIDNew = By.xpath("(//*[@class='data-column column-newFileExtractSeqNbr'])[1]");
	public static By by_statusC = By.xpath("(//*[@class='data-column column-statusCd']//following::div[contains(text(),'C')])[1]");
	public static By by_ExtractTable = By.xpath("//data-table[@headertitle='Extracts']//table");
	public static By by_CompTable = By.xpath("//data-table[@headertitle='Comparison Reports']//table");
	public static By by_DiffTable = By.xpath("//table");
	public static By by_LogoutDropDown = By.xpath("//span[@class='caret']");
	public static By by_LogoutButton= By.xpath("//a[@href='/signoff']");
	public static By by_ErrorMessage= By.xpath("//div[@id='error-message']");
	
	public static By by_Extract4128=By.xpath("//span[text()='Extract ID']/../../../..//td//div[text()='4218']/../..//input");
	public static By by_Extract1=By.xpath("(//span[text()='Extract Type']/../../../..//input[1])[2]");
	public static By by_CompareSelected=By.xpath("//input[@value='Compare Selected']");
	public static By by_ExportToExcel=By.xpath("(//input[@value='Export to Excel'])");
	public static By by_Column1=By.xpath("(//*[@scope='colgroup'])[1]");
	public static By by_Column2=By.xpath("(//*[@scope='colgroup'])[2]");
	public static By by_Column3=By.xpath("(//*[@scope='colgroup'])[3]");
	public static By by_Column4=By.xpath("(//*[@scope='colgroup'])[4]");
	public static By by_Column5=By.xpath("(//*[@scope='colgroup'])[5]");
	public static By by_Column6=By.xpath("(//*[@scope='colgroup'])[6]");
	public static By by_Column7=By.xpath("(//*[@scope='colgroup'])[7]");
	public static By by_Column8=By.xpath("(//*[@scope='colgroup'])[8]");
	public static By by_Column9=By.xpath("(//*[@scope='colgroup'])[9]");
	public static By by_Column10=By.xpath("(//*[@scope='colgroup'])[10]");
	public static By by_Column11=By.xpath("(//*[@scope='colgroup'])[11]");
	public static By by_Column12=By.xpath("(//*[@scope='colgroup'])[12]");
	public static By by_Column13=By.xpath("(//*[@scope='colgroup'])[13]");
	public static By by_Column14=By.xpath("(//*[@scope='colgroup'])[14]");
	public static By by_Column15=By.xpath("(//*[@scope='colgroup'])[15]");
	public static By by_Column16=By.xpath("(//*[@scope='colgroup'])[16]");
	public static By by_Column17=By.xpath("(//*[@scope='colgroup'])[17]");
	public static By by_Column18=By.xpath("(//*[@scope='colgroup'])[18]");
	public static By by_ReportsCheckList=By.xpath("(//h4[text()='Comparison Reports']/../../..//input)[2]");
	public static By by_ViewSelectedButton=By.xpath("(//input[@class='actionButton'])[2]");
	public static By by_policyGridName=By.xpath("(//td[@class='data-column column-fileTypeCd'])[11]");
	public static By by_ExtractId=By.xpath("(//td[@class='data-column column-fileCompareSeqNbr'])[1]");
	public static By by_GrayoutScreen = By.xpath("//h1[@class='visible loading-text']");
	public static By by_4218extract = By.xpath("//span[text()='Extract ID']/../../../..//td//div[text()='4218']/../..//input");
	public static By by_newcreatedextract =By.xpath("(//span[text()='Extract Type']/../../../..//input[1])[2]");
	public static By by_statusIPC = By.xpath("(//span[text()='Status']/../../../..//div[text()='C' or text()='A'  or text()='F' or text()='IP'])[1]");
	public static By by_messageClose = By.xpath("//a[@class='close']");
	
}
