package com.luminous.pdi.client_apis;

import com.google.gson.JsonObject;
import com.luminous.pdi.RemarkFragment.dto.DataMethod;
import com.luminous.pdi.RemarkFragment.dto.DefectPhoto;
import com.luminous.pdi.RemarkFragment.dto.ImageUpload;
import com.luminous.pdi.activities.LoginActivity.CreateOTP.ReassureCreateOTP;
import com.luminous.pdi.activities.LoginActivity.VerifyOTP.ReassureVerifyOTP;
import com.luminous.pdi.activities.LoginActivity.dto.CreateOtpEntity;
import com.luminous.pdi.activities.LoginActivity.dto.VerifyOtpEntity;
import com.luminous.pdi.addproductLight.LightRes.InvoiceQntyBaseResponse;
import com.luminous.pdi.addproductLight.LightRes.MaterialLIghtBaseRes;
import com.luminous.pdi.addproductLight.LightRes.MaterialLightRess;
import com.luminous.pdi.addproductLight.dto.RequestView;
import com.luminous.pdi.addproduct_fan.dto.ApiBaseResponse;
import com.luminous.pdi.addproduct_fan.dto.FaultBaseResponse;
import com.luminous.pdi.addproduct_fan.dto.MaterialBaseResponse;
import com.luminous.pdi.addproduct_fan.dto.SaveStatusBaseResponse;
import com.luminous.pdi.calendar.responce.CalenderData.GetCalenderData;
import com.luminous.pdi.calendar.responce.PenidngResponse;
import com.luminous.pdi.calendar.responce.newDto.PendingCalnderResponse;
import com.luminous.pdi.completed.dto.CompleteBaseResponse;
import com.luminous.pdi.draft.dto.DraftBaseResponse;
import com.luminous.pdi.draft.dto.VisitBaseResponse;
import com.luminous.pdi.draft.dto.WarrantyBaseResponse;
import com.luminous.pdi.home.dto.HomePageBaseResponse;
import com.luminous.pdi.home.dto.NavigationBaseResponse;
import com.luminous.pdi.home.fragments.CreateListDTO.CreateListDashBoard;
import com.luminous.pdi.search.dto.ProductSearchBaseResponse;

import org.json.JSONObject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RetrofitInterface {

    @GET
    Observable<CreateOtpEntity> createOtp(@Url String url);
    @Headers({
            "Content-Type:application/json",
            "Authorization:Basic YXBpYWRtaW46YXBpYWRtaW5AMTIz"
    })
    @POST
    Observable<ReassureCreateOTP> createReassureOtp(@Url String url, @Body ReassureCreateOTP sendUserMobileNo);
    @Headers({
            "Content-Type:application/json",
            "Authorization:Basic YXBpYWRtaW46YXBpYWRtaW5AMTIz"
    })
    @POST
    Observable<ReassureVerifyOTP> reassureVerifyOtp(@Url String url,@Body ReassureVerifyOTP verifyOtp);

    @POST
    Observable<ImageUpload>getreasureUploadImage(@Header("X-access-token")String authorization,@Url String url, @Body ImageUpload imageUpload);

    @GET
    Observable<NavigationBaseResponse> navigationMenuData(@Url String url);


    @GET
    Observable<CreateListDashBoard> getHomePageData(@Header("X-access-token") String authorization, @Url String url);
    @GET
    Observable<GetCalenderData> getCalendereData(@Url String url);

    @GET
    Observable<FaultBaseResponse> getFaultData(@Url String url);

    @GET
    Observable<MaterialBaseResponse> getMaterialData(@Url String url);

    @GET
    Observable<CompleteBaseResponse> getVisitDetailPageData(@Url String url);

    @GET
    Observable<DraftBaseResponse> getDraftVisitDetailPageData(@Url String url);

    @GET
    Observable<PendingCalnderResponse> getPendingResponceDataa(@Url String url);

    @GET
    Observable<ProductSearchBaseResponse> getSearchPageData(@Url String url);

    @DELETE
    Observable<VisitBaseResponse> getDrafDeleteData(@Url String url);

    @POST
    Observable<ApiBaseResponse> saveFanDetailData(@Url String url, @Body JsonObject job);

    @GET
    Observable<MaterialLightRess> getMaterialLightData(@Url String url);

    @GET
    Observable<InvoiceQntyBaseResponse> getInvoiceQntyListData(@Url String url);

    @GET
    Observable<SaveStatusBaseResponse> getSaveStatusData(@Url String url);

    @POST
    Observable<MaterialLIghtBaseRes>saveLightDetailData(@Url String url, @Body JsonObject job);

    @GET
    Observable<ResponseBody> getDraftFanDetailData(@Url String url);

    @GET
    Observable<WarrantyBaseResponse> getProductWarrantyStatusData(@Url String url);

    @GET
    Observable<RequestView> getProductLightWarrantyStatusData(@Url String url);

    @GET
    Observable<ResponseBody> getDraftLightDetailData(@Url String url);

}
