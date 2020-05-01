
package com.luminous.pdi.home.fragments.CreateListDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseDatum implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("program_id")
    @Expose
    private Integer programId;
    @SerializedName("repairer_id")
    @Expose
    private Integer repairerId;
    @SerializedName("cl_aiz_claim_number")
    @Expose
    private String clAizClaimNumber;
    @SerializedName("assigned_to")
    @Expose
    private Integer assignedTo;
    @SerializedName("case_status")
    @Expose
    private Integer caseStatus;
    @SerializedName("case_substatus")
    @Expose
    private Integer caseSubstatus;
    @SerializedName("client_code")
    @Expose
    private String clientCode;
    @SerializedName("unique_cl_aiz_claim_number")
    @Expose
    private String uniqueClAizClaimNumber;
    @SerializedName("cl_client_claim_number")
    @Expose
    private String clClientClaimNumber;
    @SerializedName("po_status")
    @Expose
    private String poStatus;
    @SerializedName("po_end_date")
    @Expose
    private String poEndDate;
    @SerializedName("po_policy_number")
    @Expose
    private String poPolicyNumber;
    @SerializedName("po_start_date")
    @Expose
    private String poStartDate;
    @SerializedName("po_credit_available")
    @Expose
    private Double poCreditAvailable;
    @SerializedName("cu_first_name")
    @Expose
    private String cuFirstName;
    @SerializedName("cu_last_name")
    @Expose
    private String cuLastName;
    @SerializedName("cu_email")
    @Expose
    private String cuEmail;
    @SerializedName("cu_phone")
    @Expose
    private String cuPhone;
    @SerializedName("cu_pincode")
    @Expose
    private String cuPincode;
    @SerializedName("cu_address")
    @Expose
    private String cuAddress;
    @SerializedName("cu_city")
    @Expose
    private String cuCity;
    @SerializedName("cu_state")
    @Expose
    private String cuState;
    @SerializedName("cu_country")
    @Expose
    private Object cuCountry;
    @SerializedName("pr_product_type")
    @Expose
    private String prProductType;
    @SerializedName("pr_brand")
    @Expose
    private String prBrand;
    @SerializedName("pr_model")
    @Expose
    private String prModel;
    @SerializedName("pr_serial_number")
    @Expose
    private String prSerialNumber;
    @SerializedName("pr_name")
    @Expose
    private String prName;
    @SerializedName("pr_price")
    @Expose
    private Double prPrice;
    @SerializedName("pr_properties")
    @Expose
    private String prProperties;
    @SerializedName("pr_purchase_date")
    @Expose
    private String prPurchaseDate;
    @SerializedName("cl_service_account_number")
    @Expose
    private String clServiceAccountNumber;
    @SerializedName("cl_case_type")
    @Expose
    private String clCaseType;
    @SerializedName("cl_email")
    @Expose
    private Object clEmail;
    @SerializedName("cl_loss_date")
    @Expose
    private String clLossDate;
    @SerializedName("cl_reserve_amount")
    @Expose
    private Integer clReserveAmount;
    @SerializedName("cl_caller_type")
    @Expose
    private String clCallerType;
    @SerializedName("cl_description")
    @Expose
    private String clDescription;
    @SerializedName("cl_fault_category")
    @Expose
    private String clFaultCategory;
    @SerializedName("cl_fault_sub_category")
    @Expose
    private String clFaultSubCategory;
    @SerializedName("cl_problem_category")
    @Expose
    private Object clProblemCategory;
    @SerializedName("cl_problem_sub_category")
    @Expose
    private Object clProblemSubCategory;
    @SerializedName("cl_priority")
    @Expose
    private String clPriority;
    @SerializedName("cl_claim_status")
    @Expose
    private String clClaimStatus;
    @SerializedName("cl_claim_sub_status")
    @Expose
    private String clClaimSubStatus;
    @SerializedName("cl_repair_type")
    @Expose
    private Object clRepairType;
    @SerializedName("cl_claim_creation_date")
    @Expose
    private String clClaimCreationDate;
    @SerializedName("cl_notes")
    @Expose
    private String clNotes;
    @SerializedName("vs_earliest_start_permitted")
    @Expose
    private String vsEarliestStartPermitted;
    @SerializedName("vs_due_date")
    @Expose
    private String vsDueDate;
    @SerializedName("vs_expected_datetime_closure")
    @Expose
    private Object vsExpectedDatetimeClosure;
    @SerializedName("repairer_schedule_time_slot")
    @Expose
    private Object repairerScheduleTimeSlot;
    @SerializedName("repairer_schedule_time")
    @Expose
    private Object repairerScheduleTime;
    @SerializedName("cl_nature_of_loss")
    @Expose
    private String clNatureOfLoss;
    @SerializedName("asset_care_number")
    @Expose
    private String assetCareNumber;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("repeat_repair")
    @Expose
    private String repeatRepair;
    @SerializedName("modified_by")
    @Expose
    private Integer modifiedBy;
    @SerializedName("deleted")
    @Expose
    private Integer deleted;
    @SerializedName("final_status")
    @Expose
    private Object finalStatus;
    @SerializedName("cancellation_reason")
    @Expose
    private Object cancellationReason;
    @SerializedName("sorturl")
    @Expose
    private Object sorturl;
    @SerializedName("action")
    @Expose
    private Action action;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Integer getRepairerId() {
        return repairerId;
    }

    public void setRepairerId(Integer repairerId) {
        this.repairerId = repairerId;
    }

    public String getClAizClaimNumber() {
        return clAizClaimNumber;
    }

    public void setClAizClaimNumber(String clAizClaimNumber) {
        this.clAizClaimNumber = clAizClaimNumber;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Integer caseStatus) {
        this.caseStatus = caseStatus;
    }

    public Integer getCaseSubstatus() {
        return caseSubstatus;
    }

    public void setCaseSubstatus(Integer caseSubstatus) {
        this.caseSubstatus = caseSubstatus;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getUniqueClAizClaimNumber() {
        return uniqueClAizClaimNumber;
    }

    public void setUniqueClAizClaimNumber(String uniqueClAizClaimNumber) {
        this.uniqueClAizClaimNumber = uniqueClAizClaimNumber;
    }

    public String getClClientClaimNumber() {
        return clClientClaimNumber;
    }

    public void setClClientClaimNumber(String clClientClaimNumber) {
        this.clClientClaimNumber = clClientClaimNumber;
    }

    public String getPoStatus() {
        return poStatus;
    }

    public void setPoStatus(String poStatus) {
        this.poStatus = poStatus;
    }

    public String getPoEndDate() {
        return poEndDate;
    }

    public void setPoEndDate(String poEndDate) {
        this.poEndDate = poEndDate;
    }

    public String getPoPolicyNumber() {
        return poPolicyNumber;
    }

    public void setPoPolicyNumber(String poPolicyNumber) {
        this.poPolicyNumber = poPolicyNumber;
    }

    public String getPoStartDate() {
        return poStartDate;
    }

    public void setPoStartDate(String poStartDate) {
        this.poStartDate = poStartDate;
    }

    public Double getPoCreditAvailable() {
        return poCreditAvailable;
    }

    public void setPoCreditAvailable(Double poCreditAvailable) {
        this.poCreditAvailable = poCreditAvailable;
    }

    public String getCuFirstName() {
        return cuFirstName;
    }

    public void setCuFirstName(String cuFirstName) {
        this.cuFirstName = cuFirstName;
    }

    public String getCuLastName() {
        return cuLastName;
    }

    public void setCuLastName(String cuLastName) {
        this.cuLastName = cuLastName;
    }

    public String getCuEmail() {
        return cuEmail;
    }

    public void setCuEmail(String cuEmail) {
        this.cuEmail = cuEmail;
    }

    public String getCuPhone() {
        return cuPhone;
    }

    public void setCuPhone(String cuPhone) {
        this.cuPhone = cuPhone;
    }

    public String getCuPincode() {
        return cuPincode;
    }

    public void setCuPincode(String cuPincode) {
        this.cuPincode = cuPincode;
    }

    public String getCuAddress() {
        return cuAddress;
    }

    public void setCuAddress(String cuAddress) {
        this.cuAddress = cuAddress;
    }

    public String getCuCity() {
        return cuCity;
    }

    public void setCuCity(String cuCity) {
        this.cuCity = cuCity;
    }

    public String getCuState() {
        return cuState;
    }

    public void setCuState(String cuState) {
        this.cuState = cuState;
    }

    public Object getCuCountry() {
        return cuCountry;
    }

    public void setCuCountry(Object cuCountry) {
        this.cuCountry = cuCountry;
    }

    public String getPrProductType() {
        return prProductType;
    }

    public void setPrProductType(String prProductType) {
        this.prProductType = prProductType;
    }

    public String getPrBrand() {
        return prBrand;
    }

    public void setPrBrand(String prBrand) {
        this.prBrand = prBrand;
    }

    public String getPrModel() {
        return prModel;
    }

    public void setPrModel(String prModel) {
        this.prModel = prModel;
    }

    public String getPrSerialNumber() {
        return prSerialNumber;
    }

    public void setPrSerialNumber(String prSerialNumber) {
        this.prSerialNumber = prSerialNumber;
    }

    public String getPrName() {
        return prName;
    }

    public void setPrName(String prName) {
        this.prName = prName;
    }

    public Double getPrPrice() {
        return prPrice;
    }

    public void setPrPrice(Double prPrice) {
        this.prPrice = prPrice;
    }

    public String getPrProperties() {
        return prProperties;
    }

    public void setPrProperties(String prProperties) {
        this.prProperties = prProperties;
    }

    public String getPrPurchaseDate() {
        return prPurchaseDate;
    }

    public void setPrPurchaseDate(String prPurchaseDate) {
        this.prPurchaseDate = prPurchaseDate;
    }

    public String getClServiceAccountNumber() {
        return clServiceAccountNumber;
    }

    public void setClServiceAccountNumber(String clServiceAccountNumber) {
        this.clServiceAccountNumber = clServiceAccountNumber;
    }

    public String getClCaseType() {
        return clCaseType;
    }

    public void setClCaseType(String clCaseType) {
        this.clCaseType = clCaseType;
    }

    public Object getClEmail() {
        return clEmail;
    }

    public void setClEmail(Object clEmail) {
        this.clEmail = clEmail;
    }

    public String getClLossDate() {
        return clLossDate;
    }

    public void setClLossDate(String clLossDate) {
        this.clLossDate = clLossDate;
    }

    public Integer getClReserveAmount() {
        return clReserveAmount;
    }

    public void setClReserveAmount(Integer clReserveAmount) {
        this.clReserveAmount = clReserveAmount;
    }

    public String getClCallerType() {
        return clCallerType;
    }

    public void setClCallerType(String clCallerType) {
        this.clCallerType = clCallerType;
    }

    public String getClDescription() {
        return clDescription;
    }

    public void setClDescription(String clDescription) {
        this.clDescription = clDescription;
    }

    public String getClFaultCategory() {
        return clFaultCategory;
    }

    public void setClFaultCategory(String clFaultCategory) {
        this.clFaultCategory = clFaultCategory;
    }

    public String getClFaultSubCategory() {
        return clFaultSubCategory;
    }

    public void setClFaultSubCategory(String clFaultSubCategory) {
        this.clFaultSubCategory = clFaultSubCategory;
    }

    public Object getClProblemCategory() {
        return clProblemCategory;
    }

    public void setClProblemCategory(Object clProblemCategory) {
        this.clProblemCategory = clProblemCategory;
    }

    public Object getClProblemSubCategory() {
        return clProblemSubCategory;
    }

    public void setClProblemSubCategory(Object clProblemSubCategory) {
        this.clProblemSubCategory = clProblemSubCategory;
    }

    public String getClPriority() {
        return clPriority;
    }

    public void setClPriority(String clPriority) {
        this.clPriority = clPriority;
    }

    public String getClClaimStatus() {
        return clClaimStatus;
    }

    public void setClClaimStatus(String clClaimStatus) {
        this.clClaimStatus = clClaimStatus;
    }

    public String getClClaimSubStatus() {
        return clClaimSubStatus;
    }

    public void setClClaimSubStatus(String clClaimSubStatus) {
        this.clClaimSubStatus = clClaimSubStatus;
    }

    public Object getClRepairType() {
        return clRepairType;
    }

    public void setClRepairType(Object clRepairType) {
        this.clRepairType = clRepairType;
    }

    public String getClClaimCreationDate() {
        return clClaimCreationDate;
    }

    public void setClClaimCreationDate(String clClaimCreationDate) {
        this.clClaimCreationDate = clClaimCreationDate;
    }

    public String getClNotes() {
        return clNotes;
    }

    public void setClNotes(String clNotes) {
        this.clNotes = clNotes;
    }

    public String getVsEarliestStartPermitted() {
        return vsEarliestStartPermitted;
    }

    public void setVsEarliestStartPermitted(String vsEarliestStartPermitted) {
        this.vsEarliestStartPermitted = vsEarliestStartPermitted;
    }

    public String getVsDueDate() {
        return vsDueDate;
    }

    public void setVsDueDate(String vsDueDate) {
        this.vsDueDate = vsDueDate;
    }

    public Object getVsExpectedDatetimeClosure() {
        return vsExpectedDatetimeClosure;
    }

    public void setVsExpectedDatetimeClosure(Object vsExpectedDatetimeClosure) {
        this.vsExpectedDatetimeClosure = vsExpectedDatetimeClosure;
    }

    public Object getRepairerScheduleTimeSlot() {
        return repairerScheduleTimeSlot;
    }

    public void setRepairerScheduleTimeSlot(Object repairerScheduleTimeSlot) {
        this.repairerScheduleTimeSlot = repairerScheduleTimeSlot;
    }

    public Object getRepairerScheduleTime() {
        return repairerScheduleTime;
    }

    public void setRepairerScheduleTime(Object repairerScheduleTime) {
        this.repairerScheduleTime = repairerScheduleTime;
    }

    public String getClNatureOfLoss() {
        return clNatureOfLoss;
    }

    public void setClNatureOfLoss(String clNatureOfLoss) {
        this.clNatureOfLoss = clNatureOfLoss;
    }

    public String getAssetCareNumber() {
        return assetCareNumber;
    }

    public void setAssetCareNumber(String assetCareNumber) {
        this.assetCareNumber = assetCareNumber;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getRepeatRepair() {
        return repeatRepair;
    }

    public void setRepeatRepair(String repeatRepair) {
        this.repeatRepair = repeatRepair;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Object getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(Object finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Object getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(Object cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Object getSorturl() {
        return sorturl;
    }

    public void setSorturl(Object sorturl) {
        this.sorturl = sorturl;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}
