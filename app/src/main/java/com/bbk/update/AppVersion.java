package com.bbk.update;

public class AppVersion {
    private String updateMessage;
    private String apkUrl;
    private int apkCode;

    private String forceupdate;
    public static final String APK_DOWNLOAD_URL = "url";
    public static final String APK_UPDATE_CONTENT = "updateMessage";
    public static final String APK_VERSION_CODE = "versionCode";
    public static final String APK_FORCEUPDATE = "forceupdate";


    public String getForceupdate() {
        return forceupdate;
    }

    public void setForceupdate(String forceupdate) {
        this.forceupdate = forceupdate;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }
                                                                                                                                           
    public String getUpdateMessage() {
        return updateMessage;
    }
                                                                                                                                           
    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }
                                                                                                                                           
    public String getApkUrl() {
        return apkUrl;
    }
                                                                                                                                           
    public void setApkCode(int apkCode) {
        this.apkCode = apkCode;
    }
                                                                                                                                           
    public int getApkCode() {
        return apkCode;
    }

	@Override
	public String toString() {
		return "{\"updateMessage\"=\"" + updateMessage + "\", " +
				"\"apkUrl\"=\""+ apkUrl + 
				"\", \"apkCode\"=" + apkCode + "\",\"forceupdate\"=" + forceupdate + "\"}";
	}
    
    
}
