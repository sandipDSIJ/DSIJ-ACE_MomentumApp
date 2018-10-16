package in.dsij.acemomentum.net;

import java.util.List;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.net.res.ResGetCalls;
import in.dsij.acemomentum.net.res.ResGetDownloadables;
import in.dsij.acemomentum.net.res.ResGetLiveCalls;
import in.dsij.acemomentum.net.res.ResGetProductBrief;
import in.dsij.acemomentum.net.res.ResGetProducts;
import in.dsij.acemomentum.net.res.ResGetTrackerCol;
import in.dsij.acemomentum.net.res.ResLogin;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.net.res.ResPdfWithPassword;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Vikas on 9/12/2017.
 */

public interface DService {

    @FormUrlEncoded
    @POST(G.net.loginWithPassword.ENDPOINT)
    Call<ResLogin> loginWithPassword(
            @Field("username") String userName,
            @Field("password") String password,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.loginWithToken.ENDPOINT)
    Call<ResLogin> loginWithToken(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.logout.ENDPOINT)
    Call<ResMessage> logout(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("appid") int appId,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.signUp.ENDPOINT)
    Call<ResMessage> signUp(
            @Field("firstname") String firstName,
            @Field("lastname") String lastName,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.resetPassword.ENDPOINT)
    Call<ResMessage> resetPassword(
            @Field("email") String userName,
            @Field("productid") String productid
    );

    @FormUrlEncoded
    @POST(G.net.notification.ENDPOINT)
    Call<ResMessage> notification(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("appid") int appId,
            @Field("gcmid") String gcmId,
            @Field("enable") boolean enable,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.getProducts.ENDPOINT)
    Call<ResGetProducts> getProducts(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("appid") int appid,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.getProductBrief.ENDPOINT)
    Call<ResGetProductBrief> getProductBrief(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("productid") long productid,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.getCalls.ENDPOINT)
    Call<List<ResGetCalls>> getCalls(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("productid") long productid,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.getDownloadables.ENDPOINT)
    Call<ResGetDownloadables> getDownloadables(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("callid") long callId,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.GetInvestorFiles.ENDPOINT)
    Call<ResGetDownloadables> GetDownloadablesAcemomentum(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("callid") long callId,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.DownloadFileWithPassword.ENDPOINT)
    Call<ResPdfWithPassword> DownloadFileWithPassword(
            @Body ResPdfWithPassword body
    );

    @FormUrlEncoded
    @POST(G.net.postLowRating.ENDPOINT)
    Call<ResMessage> postLowRating(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("appid") int appId,
            @Field("versionname") String versionName,
            @Field("versionno") int version,
            @Field("rating") int rating,
            @Field("description") String description,
            @Field("deviceid") String deviceId
    );

    @FormUrlEncoded
    @POST(G.net.getTrackerData.ENDPOINT)
    Call<List<ResGetTrackerCol>> getTrackerData(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("appid") int appId,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    /*
     *
     * Not Used in this app
     *
     * */
    @FormUrlEncoded
    @POST(G.net.getLiveCalls.ENDPOINT)
    Call<ResGetLiveCalls> getLiveCalls(
            @Field("username") String userName,
            @Field("token") String token,
            @Field("deviceid") String deviceId,
            @Field("versionno") int version
    );

    @FormUrlEncoded
    @POST(G.net.SubmitFeedback.ENDPOINT)
    Call<ResponseBody> submitFeedback(
            @Field("UserName") String userName,
            @Field("Token") String token,
            @Field("Email") String email,
            @Field("Phone") String phone,
            @Field("subject") String subject,
            @Field("Description") String description
    );

    @FormUrlEncoded
    @POST("/desktopmodules/services/api/MobileApp/DsijMobileAppChangePassword")
    Call<ResMessage> changePassword(
            @Field("UserName") String userName,
            @Field("Token") String token,
            @Field("CurrentPassword") String oldPassword,
            @Field("NewPassword") String newPassword
    );

    @FormUrlEncoded
    @POST("/desktopmodules/services/api/MobileApp/CheckAppVersion_DSIJ/")
    Call<ResponseBody> checkVersion(
            @Field("versionno") String version_no, @Field("appid") String appId
    );

}
