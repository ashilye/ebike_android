package net.hyntech.common.model.entity;


public class AppUpdateEntity {

    private String type;
    private String version;
    private int versionCode;
    private String url;
    private String content;
    private String time;

    public AppUpdateEntity(String type, String version, int versionCode, String url, String content, String time) {
        this.type = type;
        this.version = version;
        this.versionCode = versionCode;
        this.url = url;
        this.content = content;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
