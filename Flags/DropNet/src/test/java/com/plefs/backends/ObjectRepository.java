package com.plefs.backends;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ObjectRepository {
	public WebElement element = null;

	public static By by_username=By.xpath("//input[@name='username']");
	public static By by_password=By.xpath("//input[@name='password']");
	public static By by_submit=By.xpath("//input[@name='submit']");
	
	public static By by_Dropnetpage=By.xpath("//div[@align='center']/img[@src='images/RMLogo.gif']");
	public static By by_dropnetlink=By.xpath("//div[@align ='center']/a/img[@name='DropNet']");
	public static By by_landlordLease=By.xpath("//img[@name='LandlordLease']");
	public static By by_searchButton=By.xpath("//input[@name='Search_button']");
	
	public static By by_firstRow=By.xpath("/html/body/table/tbody/tr[2]/td[1]/a");
	public static By by_list=By.xpath("//input[@name='ShowSearch_button']");
	
	public static By by_UpdateBtn=By.xpath("//input[@name='ShowUpdate_button']");
	public static By by_ContactNm=By.xpath("//input[@name='contactNm']");
	public static By by_ContactEmailId=By.xpath("//input[@name='emailCd']");
	public static By by_company_name=By.xpath("//input[@name='companyNm']");
	public static By by_Store_number = By.xpath("//input[@name='storeNbr']");
	public static By by_loc_name = By.xpath("//input[@name='locationNm']");
	public static By by_bldg_nm = By.xpath("//input[@name='buildingNm']");
	public static By by_loc_city = By.xpath("//input[@name='locInCityDesc']");
	public static By by_bar_cd = By.xpath("//input[@name='barCd']");
	public static By by_mon_open = By.xpath("//input[@name='monOpenTm']");
	public static By by_tue_open = By.xpath("//input[@name='tueOpenTm']");
	public static By by_wed_open = By.xpath("//input[@name='wedOpenTm']");
	public static By by_thu_open = By.xpath("//input[@name='thuOpenTm']");
	public static By by_fri_open = By.xpath("//input[@name='friOpenTm']");
	public static By by_sat_open = By.xpath("//input[@name='satOpenTm']");
	public static By by_sun_open = By.xpath("//input[@name='sunOpenTm']");
	
	public static By by_mon_clos = By.xpath("//input[@name='monCloseTm']");
	public static By by_tue_clos = By.xpath("//input[@name='tueCloseTm']");
	public static By by_wed_clos = By.xpath("//input[@name='wedCloseTm']");
	public static By by_thu_clos = By.xpath("//input[@name='thuCloseTm']");
	public static By by_fri_clos = By.xpath("//input[@name='friCloseTm']");
	public static By by_sat_clos = By.xpath("//input[@name='satCloseTm']");
	public static By by_sun_clos = By.xpath("//input[@name='sunCloseTm']");
	public static By by_direction = By.xpath("//textarea[@name='directionDesc']");
	public static By by_comments = By.xpath("//textarea[@name='commentDesc']");
	public static By by_addrecords = By.xpath("//input[@name='Add_button']");
	
    public static By by_LocationCd=By.xpath("//input[@name='locationCd']");
    public static By by_ChannelCd=By.xpath("//select[@name='channelCd']");
    public static By by_EffectiveDt=By.xpath("//input[@name='effDt']");
    public static By by_comMent=By.xpath("//textarea[@name='commentDesc']");
    public static By by_ShowBtn=By.xpath("//input[@name='ShowDelete_button']");
    public static By by_serial_number = By.xpath("//input[@name='serialNbr']");
   
    public static By by_UpdateRecordBtn=By.xpath("//input[@value=' Update Record ']");
   
    public static By by_Payee_FeeTypeCode=By.xpath("//select[@name='feeCd']");
    public static By by_Payee_FeeAmount=By.xpath("//input[@name='feeAmt']"); 
    
    public static By by__Payee_Company_nm=By.xpath("//input[@name='payeeNm']");
    public static By by__Payee_Street=By.xpath("//input[@name='payeeAddressDesc']");
    public static By by__Payee_citynm=By.xpath("//input[@name='payeeCityNm']");
    public static By by__Payee_countrycd=By.xpath("//input[@name='payeeCountryCd']");
    public static By by__Payee_taxIdCd=By.xpath("//input[@name='taxIdCd']");
    public static By by__payee_PhoneNbr=By.xpath("//input[@name='payeePhoneNbr']");
    public static By by__payee_SuiteDesc=By.xpath("//input[@name='payeeSuiteDesc']");
    public static By by__Payee_minorityCdPayee=By.xpath("//select[@name='minorityCdPayee']");
    public static By by__payee_vendorSetup=By.xpath("//select[@name='vendorSetup']");
    public static By by__payee_payeeStateProvCd=By.xpath("//input[@name='payeeStateProvCd']");
    public static By by__payee_paymentDueDt=By.xpath("//input[@name='paymentDueDt']");
    public static By by__payee_vendorNbr=By.xpath("//input[@name='vendorNbr']");
    public static By by__payee_taxPct=By.xpath("//input[@name='taxPct']");
    public static By by__payee_payeeFaxNbr=By.xpath("//input[@name='payeeFaxNbr']");
    public static By by__payee_payeePostalCd=By.xpath("//input[@name='payeePostalCd']");
   // public static By by__payee_feeAmt=By.xpath("//input[@name='feeAmt']");
    public static By by__payee_escalationPct=By.xpath("//input[@name='escalationPct']");
    public static By by__payee_contractExpirationDt=By.xpath("//input[@name='contractExpirationDt']");

    
    public static By by_ReactivateBtn=By.xpath("//input[@name='Reactivate_button']");
   // public static By by_StatusMsg=By.xpath("//*[@name='Status']//table/tbody/tr/td/input[@class='InputText']");
    
    public static By by_Msg=By.xpath("/html/body/form/table/tbody/tr/td[2]/input");
 
    
	public static By by_AddBttn=By.xpath("//input[@id='Add_button']");
	public static By by_VerifyAddress=By.xpath("//input[@name='VerifyAddress_button']");
	
	public static By by_ValidateAddress=By.xpath("//*[@name='ValidateAddress_button']");
	public static By by_Accept=By.xpath("//*[@name='Accept_button']");
	public static By by_Decline=By.xpath("//*[@name='Decline_button']");
	public static By by_Other=By.xpath("//*[@name='Other_button']");
	public static By by_AddRecord=By.xpath("//*[@name='AddDropBox_button']");
	public static By by_PostalCodeError=By.xpath("//h4");
	public static By by_Expiration_date=By.xpath("/html/body/form[2]/table[2]/tbody/tr[3]/td[6]");
	public static By by_show_textbox= By.xpath("/html/body/form/table[1]/tbody/tr/td[2]/input");
	public static By by_after_update_contact= By.xpath("/html/body/table[1]/tbody/tr[3]/td/table/tbody/tr[1]/td[2]");

	public static By by_DropBox_AccessCdLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[1]/td[1]");
	public static By by_DropBox_AccessCdValue=By.xpath("//select[@name='locAccessTypeCd']");
	public static By by_DropBox_BuildingNmLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[1]/td[3]");
	public static By by_DropBox_BuildingNmValue=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[1]/td[4]/input");
	public static By by_DropBox_StreetLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[1]");
	public static By by_DropBox_StreetValue=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[2]/input");
	public static By by_DropBox_SuiteLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[3]");
	public static By by_DropBox_SuiteValue=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[4]/input");
	public static By by_DropBox_RoomLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[5]");
	public static By by_DropBox_RoomValue=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[6]/input");
	public static By by_DropBox_CityLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[1]");
	public static By by_DropBox_CityValue=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[2]/input");
	public static By by_DropBox_StateLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[3]");
	public static By by_DropBox_StateValue=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[4]/input");
	public static By by_DropBox_PostalLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[5]");
	public static By by_DropBox_PostalValue=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[6]/input");
	public static By by_DropBox_CountryCdLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[4]/td[1]");
	public static By by_DropBox_CountryCdValue=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[4]/td[2]/input");
	public static By by_DropBox_LocOnPropertyLabel=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[4]/td[3]");
	public static By by_DropBox_LocOnPropertyValue=By.xpath("//table[1]/tbody/tr[1]/td[1]/table/tbody/tr[4]/td[4]/input");

	//Bussiness OR

	public static By by_BuildingInfo_CompnyNmLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[1]/td[1]");
	public static By by_BuildingInfo_CompnyNmValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[1]/td[2]/input");
	public static By by_BuildingInfo_AggrDateLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[1]/td[3]");
	public static By by_BuildingInfo_AggrDateValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[1]/td[4]/input");

	public static By by_BuildingInfo_StreetLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[1]");
	public static By by_BuildingInfo_StreetValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[2]/input");
	public static By by_BuildingInfo_SuiteLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[3]");
	public static By by_BuildingInfo_SuiteValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[4]/input");

	public static By by_BuildingInfo_CityLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[3]/td[1]");
	public static By by_BuildingInfo_CityValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[3]/td[2]/input");
	public static By by_BuildingInfo_StateLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[3]/td[3]");
	public static By by_BuildingInfo_StateValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[3]/td[4]/input");
	public static By by_BuildingInfo_PostalLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[3]/td[5]");
	public static By by_BuildingInfo_PostalValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[3]/td[6]/input");

	public static By by_BuildingInfo_CmpnyEmailLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[4]/td[1]");
	public static By by_BuildingInfo_CmpnyEmailValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[4]/td[2]/input");
	public static By by_BuildingInfo_CountryCdLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[4]/td[3]");
	public static By by_BuildingInfo_CountryCdValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[4]/td[4]/input");

	public static By by_BuildingInfo_CmpnyCntctNmLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[1]");
	public static By by_BuildingInfo_CmpnyCntctNmValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[2]/input");
	public static By by_BuildingInfo_CmpnyCntctPhoneLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[3]");
	public static By by_BuildingInfo_CmpnyCntctPhoneValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[4]/input");
	public static By by_BuildingInfo_FaxNoLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[5]");
	public static By by_BuildingInfo_FaxNoValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[6]/input");

	public static By by_BuildingInfo_BuildngCntctNmLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[6]/td[1]");
	public static By by_BuildingInfo_BuildngCntctNmValue=By.xpath("//input[@name='managerNm']");
	public static By by_BuildingInfo_BuildngCntctPhoneLabel=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[6]/td[3]");
	public static By by_BuildingInfo_BuildngCntctPhoneValue=By.xpath("//table[1]/tbody/tr[2]/td[1]/table/tbody/tr[6]/td[4]/input");
	public static By by_BuildingInfo_UseDropBox=By.xpath("//iput[@name='useDBInfo']");


	//Payee OR
	public static By by_Payee_FeeTypeCd=By.xpath("//select[@name='feeCd']");
	public static By by_Payee_UseOwnerInfo=By.xpath("//input[@name='useManagementChecked']");
	public static By by_Payee_ComPanyNameLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[1]/td[1]");
	public static By by_Payee_MnCdAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[1]/td[3]");
	public static By by_Payee_VendorFlagLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[1]/td[5]");
	public static By by_Payee_StreetLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[2]/td[1]");
	public static By by_Payee_SuiteLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[2]/td[3]");
	public static By by_Payee_CityAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[1]");
	public static By by_Payee_StateLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[3]");
	public static By by_Payee_PostalCdAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[5]");
	public static By by_Payee_CntryCdAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[1]");
	public static By by_Payee_VendorNmAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[3]");
	public static By by_Payee_FeeAmtAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[5]");
	public static By by_Payee_TaxIDAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[1]");
	public static By by_Payee_TaxPerAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[3]");
	public static By by_Payee_EscltnPCtAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[5]");
	public static By by_Payee_PhoneAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[1]");
	public static By by_Payee_FaxNmAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[3]");
	public static By by_Payee_CntrctExpDateAppLabel=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[5]");
	public static By by_Payee_CommentsAppLabel=By.xpath("//form[@name='addForm']/table[2]/tbody/tr[1]/td[1]");

	public static By by_Payee_ComPanyNameValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[1]/td[2]/input");
	public static By by_Payee_MnCdAppValue=By.xpath("//select[@name='minorityCdPayee']");
	public static By by_Payee_VendorFlagValue=By.xpath("//select[@name='vendorSetup']");
	public static By by_Payee_StreetValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[2]/td[2]/input");
	public static By by_Payee_SuiteValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[2]/td[4]/input");
	public static By by_Payee_CityAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[2]/input");
	public static By by_Payee_StateValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[4]/input");
	public static By by_Payee_PostalCdAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[6]/input");
	public static By by_Payee_CntryCdAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[2]/input");
	public static By by_Payee_VendorNmAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[4]/input");
	//public static By by_Payee_FeeAmtAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[6]/input");
	public static By by_Payee_TaxIDAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[2]/input");
	public static By by_Payee_TaxPerAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[4]/input");
	public static By by_Payee_EscltnPCtAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[6]/input");
	public static By by_Payee_PhoneAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[2]/input");
	public static By by_Payee_FaxNmAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[4]/input");
	public static By by_Payee_CntrctExpDateAppValue=By.xpath("//table[1]/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[6]/input");
	public static By by_Payee_CommentsAppValue=By.xpath("//*[@id='commentsID1']");

	//-----------------------Objects after adding data of dropbox----------------------------------------------------
	
	//DropBox OR
	public static By by_Add_DropBox_LocCodeLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[1]/td[1]");
	public static By by_Add_DropBox_LocCodeValue=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[1]/td[2]");
	public static By by_Add_DropBox_EffecDateLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[1]/td[3]");
	public static By by_Add_DropBox_EffecDateValue=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[1]/td[4]");
	public static By by_Add_DropBox_ExpDateLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[1]/td[5]");
	public static By by_Add_DropBox_ExpDateValue=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[1]/td[6]");
	public static By by_Add_DropBox_AccessCdLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[1]");
	public static By by_Add_DropBox_AccessCdValue=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[2]");
	public static By by_Add_DropBox_BuildingNmLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[3]");
	public static By by_Add_DropBox_BuildingNmValue=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[2]/td[4]");
	public static By by_Add_DropBox_StreetLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[1]");
	public static By by_Add_DropBox_StreetValue=By.xpath("//input[@name='locAddressDesc']");
	public static By by_Add_DropBox_SuiteLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[3]");
	public static By by_Add_DropBox_SuiteValue=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[4]");
	public static By by_Add_DropBox_RoomLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[5]");
	public static By by_Add_DropBox_RoomValue=By.xpath("//input[@name='locRoomFloorDesc']");
	public static By by_Add_DropBox_CityLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[4]/td[1]");
	public static By by_Add_DropBox_CityValue=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[4]/td[2]");
	public static By by_Add_DropBox_StateLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[4]/td[3]");
	public static By by_Add_DropBox_StateValue=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[4]/td[4]");
	public static By by_Add_DropBox_PostalLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[4]/td[5]");
	public static By by_Add_DropBox_PostalValue=By.xpath("//input[@name='locPostalCd']");
	public static By by_Add_DropBox_CountryCdLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[5]/td[1]");
	public static By by_Add_DropBox_CountryCdValue=By.xpath("//input[@name='locCountryCd']");
	public static By by_Add_DropBox_ResLocLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[5]/td[3]");
	public static By by_Add_DropBox_ResLocValue=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[5]/td[4]");
	public static By by_Add_DropBox_LocOnPropertyLabel=By.xpath("//table[@class='BodyText']/tbody/tr[1]/td[1]/table/tbody/tr[5]/td[5]");
	public static By by_Add_DropBox_LocOnPropertyValue=By.xpath("//input[@name='locInBuildingDesc']");
	public static By by_Add_Dropbox = By.xpath("//input[@name='AddDropBox_button']");
	public static By by_cross_street = By.xpath("//input[@name='crossStreetDesc']");
	//Bussiness OR

	public By by_Add_BuildingInfo_CompnyNmLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[1]/td[1]");
	public static By by_Add_BuildingInfo_CompnyNmValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[1]/td[2]");
	public static By by_Add_BuildingInfo_AggrDateLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[1]/td[3]");
	public static By by_Add_BuildingInfo_AggrDateValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[1]/td[4]");

	public static By by_Add_BuildingInfo_CommenceDtLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[1]");
	public static By by_Add_BuildingInfo_CommenceDtValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[2]");
	public static By by_Add_BuildingInfo_EffecDtLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[3]");
	public static By by_Add_BuildingInfo_EffecDtValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[4]");
	public static By by_Add_BuildingInfo_ExpDtLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[5]");
	public static By by_Add_BuildingInfo_ExpDtValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[6]");

	public static By by_Add_BuildingInfo_StreetLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[3]/td[1]");
	public static By by_Add_BuildingInfo_StreetValue=By.xpath("//input[@name='addressDesc']");
	public static By by_Add_BuildingInfo_SuiteLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[3]/td[3]");
	public static By by_Add_BuildingInfo_SuiteValue=By.xpath("//input[@name='suiteDesc']");

	public static By by_Add_BuildingInfo_CityLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[4]/td[1]");
	public static By by_Add_BuildingInfo_CityValue=By.xpath("//input[@name='cityNm']");
	public static By by_Add_BuildingInfo_StateLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[4]/td[3]");
	public static By by_Add_BuildingInfo_StateValue=By.xpath("//input[@name='stateProvinceCd']");
	public static By by_Add_BuildingInfo_PostalLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[4]/td[5]");
	public static By by_Add_BuildingInfo_PostalValue=By.xpath("//input[@name='postalCd']");

	public static By by_Add_BuildingInfo_CmpnyEmailLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[1]");
	public static By by_Add_BuildingInfo_CmpnyEmailValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[2]");
	public static By by_Add_BuildingInfo_CountryCdLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[3]");
	public static By by_Add_BuildingInfo_CountryCdValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[5]/td[4]");

	public static By by_Add_BuildingInfo_CmpnyCntctNmLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[6]/td[1]");
	public static By by_Add_BuildingInfo_CmpnyCntctNmValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[6]/td[2]");
	public static By by_Add_BuildingInfo_CmpnyCntctPhoneLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[6]/td[3]");
	public static By by_Add_BuildingInfo_CmpnyCntctPhoneValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[6]/td[4]");
	public static By by_Add_BuildingInfo_CmpnyCntctPhoneValue1=By.xpath("//input[@name='bldCntPhoneNbr']");
	public static By by_Add_BuildingInfo_FaxNoLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[6]/td[5]");
	public static By by_Add_BuildingInfo_FaxNoValue=By.xpath("//input[@name='faxNbr']");

	public static By by_Add_BuildingInfo_BuildngCntctNmLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[7]/td[1]");
	public static By by_Add_BuildingInfo_BuildngCntctNmValue=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[7]/td[2]");
	public static By by_Add_BuildingInfo_BuildngCntctPhoneLabel=By.xpath("//table[@class='BodyText']/tbody/tr[2]/td[1]/table/tbody/tr[7]/td[3]");
	public static By by_Add_BuildingInfo_BuildngCntctPhoneValue=By.xpath("//input[@name='phoneNbr']");


	//Payee OR

	public static By by_Add_PayeeFeeCdAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[1]/td[1]");
	public static By by_Add_PayeeMnCdAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[1]/td[3]");
	public static By by_Add_PayeeVendorFlagAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[1]/td[5]");
	public static By by_Add_PayeeNmAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[2]/td[1]");
	public static By by_Add_PayeePymntDueDtAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[2]/td[3]");
	public static By by_Add_PayeeAddrsAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[1]");
	public static By by_Add_PayeeSuiteAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[3]");
	public static By by_Add_PayeeCityAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[1]");
	public static By by_Add_PayeeStateProvincCdAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[3]");
	public static By by_Add_PayeePostalCdAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[5]");
	public static By by_Add_PayeeCntryCdAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[1]");
	public static By by_Add_PayeeVendorNmAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[3]");
	public static By by_Add_PayeeFeeAmtAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[5]");
	public static By by_Add_PayeeTaxIDAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[1]");
	public static By by_Add_PayeeTaxPerAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[3]");
	public static By by_Add_PayeeEscltnPCtAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[5]");
	public static By by_Add_PayeePhoneAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[7]/td[1]");
	public static By by_Add_PayeeFaxNmAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[7]/td[3]");
	public static By by_Add_PayeeCntrctExpDateAppLabel=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[7]/td[5]");
	public static By by_Add_PayeeCommentsAppLabel=By.xpath("//form[@name='leaseShowForm']/table/tbody/tr[1]/td[1]");


	//-------------------------------------------LOC LEASE---------------------------------------------------------------------
	 public static By by_LLUpdateRecordBtn=By.xpath("//input[@value='Update Record']");
	 public static By by_LLShowBtn=By.xpath("//input[@name='Show_button']");
	 public static By by_LLUpdateBtn=By.xpath("//input[@name='ShowUpdate_button']");
	 public static By by_LLListButton=By.xpath("//input[@name='ShowSearch_button']");
	 public static By by_LLExpiration_Value=By.xpath("//input[@name='LocLease_expirationDt1']");
	 public static By by_LLExpDate=By.xpath("//select[@name='LocLease_expirationDtOp']");
     public static By by_LLSearch_Button=By.xpath("//input[@name='List_button']");
 	 public static By by_LLNext_Button=By.xpath("//input[@name='Next_button']");    
	
	
 	 public static By by_LLLast_Button=By.xpath("//input[@name='Last_button']");

	public static By by_Add_PayeePymntDueDtAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[2]/td[4]");
	public static By by_Add_PayeeAddrsAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[2]");
	public static By by_Add_PayeeSuiteAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[3]/td[4]");
	public static By by_Add_PayeeCityAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[2]");
	public static By by_Add_PayeeStateProvincCdAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[4]");
	public static By by_Add_PayeePostalCdAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[4]/td[6]");
	public static By by_Add_PayeeCntryCdAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[2]");
	public static By by_Add_PayeeVendorNmAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[4]");
	public static By by_Add_PayeeFeeAmtAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[5]/td[6]");
	public static By by_Add_PayeeTaxIDAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[2]");
	public static By by_Add_PayeeTaxPerAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[4]");
	public static By by_Add_PayeeEscltnPCtAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[6]/td[6]");
	public static By by_Add_PayeePhoneAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[7]/td[2]");
	public static By by_Add_PayeeFaxNmAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[7]/td[4]");
	public static By by_Add_PayeeCntrctExpDateAppValue=By.xpath("//table[@class='BodyText']/tbody/tr[3]/td[1]/table/tbody/tr[7]/td[6]");
	public static By by_Add_PayeeCommentsAppValue=By.xpath("//form[@name='leaseShowForm']/table/tbody/tr[1]/td[2]/table/tbody/tr[2]/td[1]");



	
}
