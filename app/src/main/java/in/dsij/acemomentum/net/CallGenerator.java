package in.dsij.acemomentum.net;

import com.google.gson.Gson;

import java.util.List;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.db.DbUser;
import in.dsij.acemomentum.net.res.ResGetCalls;
import in.dsij.acemomentum.net.res.ResGetDownloadables;
import in.dsij.acemomentum.net.res.ResGetLiveCalls;
import in.dsij.acemomentum.net.res.ResGetProductBrief;
import in.dsij.acemomentum.net.res.ResGetProducts;
import in.dsij.acemomentum.net.res.ResGetTrackerCol;
import in.dsij.acemomentum.net.res.ResLogin;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.net.res.ResPdfWithPassword;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static in.dsij.acemomentum.G.device.PRODUCT_ID;

public class CallGenerator {


    public static String currentUserName() {
        Realm realm = Realm.getDefaultInstance();
        DbUser currentUser = realm.where(DbUser.class).findFirst();
        String currentUserName = null;

        try {
            currentUserName = currentUser.getUsername();
        } catch (Exception e) {
            currentUserName = null;
        } finally {
            realm.close();
        }

        return currentUserName;
    }

    public static String currentToken() {
        Realm realm = Realm.getDefaultInstance();
        DbUser currentUser = realm.where(DbUser.class).findFirst();
        String currentToken = null;

        try {
            currentToken = currentUser.getSessionToken();
        } catch (Exception e) {
            currentToken = null;
        } finally {
            realm.close();
        }

        return currentToken;
    }

    public static Call<ResLogin> loginWithPassword(String userName, String password) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.loginWithPassword(userName, password, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);

    }

    public static Call<ResLogin> loginWithToken(String userName, String token) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.loginWithToken(userName, token, G.device.DEVICE_SERIAL,G.device.VERSION_CODE);

    }

    public static Call<ResMessage> logout() {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.logout(currentUserName(), currentToken(), G.device.APP_ID, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);

    }

    public static Call<ResMessage> signUp(String firstName, String lastName, String email, String phone) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.signUp(firstName, lastName, email, phone, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);

    }

    public static Call<ResMessage> resetPassword(String userName) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.resetPassword(userName, G.device.PRODUCT_ID+"");

    }

    public static Call<ResMessage> notification(String gcmId, boolean enable) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.notification(currentUserName(), currentToken(), G.device.APP_ID, gcmId, enable, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);

    }

    public static Call<ResGetProducts> getProducts() {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.getProducts(currentUserName(), currentToken(),G.device.APP_ID, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);

    }

    public static Call<ResGetProductBrief> getProductBrief(long productId) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.getProductBrief(currentUserName(), currentToken(), productId, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);
    }

    public static Call<List<ResGetCalls>> getCalls(long productId) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.getCalls(currentUserName(), currentToken(), PRODUCT_ID, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);

    }

    public static Call<ResGetDownloadables> getDownloadables(long callId) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.getDownloadables(currentUserName(), currentToken(), callId, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);
    }
    public static Call<ResGetDownloadables> GetDownloadablesAcemomentum(long callId) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.GetDownloadablesAcemomentum(currentUserName(), currentToken(), callId, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);
    }
    public static Call<ResPdfWithPassword> DownloadFileWithPassword(long callId) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.DownloadFileWithPassword(new ResPdfWithPassword( "~/productattachment/ValueProductFiles/ReasonsLarsen_&_Toubro_Ltd692016.pdf",
                "1150453609",""));
    }

    public static Call<ResMessage> postLowRating(int rating, String description) {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.postLowRating(currentUserName(), currentToken(), G.device.APP_ID, G.device.VERSION_NAME, G.device.VERSION_CODE, rating, description, G.device.DEVICE_SERIAL);
    }

    public static Call<List<ResGetTrackerCol>> getTrackerData() {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.getTrackerData(currentUserName(), currentToken(),G.device.APP_ID, G.device.DEVICE_SERIAL, G.device.VERSION_CODE);
    }

    public static Call<ResGetLiveCalls> getLiveCalls() {
        DService dService = ServiceGenerator.createService(DService.class);
        return dService.getLiveCalls(currentUserName(), currentToken(), G.device.DEVICE_SERIAL, G.device.VERSION_CODE);

    }

    public static Call<ResMessage> changePassword(String oldPassword,String newPassword) {
        DService dService = ServiceGenerator.createService(DService.class);
        // TODO: 9/12/2017 Change after implementing device serial and version code
        return dService.changePassword(currentUserName(),currentToken(),oldPassword,newPassword);
    }


    public static Call<ResponseBody> postFeedback(String email, String phone, String subject, String description) {
        DService dService = ServiceGenerator.createService(in.dsij.acemomentum.net.DService.class);

        return dService.submitFeedback(currentUserName(),currentToken(),email,phone,subject,description);
    }

    public static Call<ResponseBody> checkVersion(int version) {

        DService dService = ServiceGenerator.createService(DService.class);
        return dService.checkVersion(version+"", G.device.APP_ID+"");
    }

}
