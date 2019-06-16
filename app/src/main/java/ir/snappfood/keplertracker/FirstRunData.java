package ir.snappfood.keplertracker;

public class FirstRunData {

    private String ip;
    private String userAgent;
    private long timeStamp;
    private String width;
    private String height;
    private String gpu;
    private String referrer;
    private String client;
    private String storeName;
    private String appVersion;
    private String deviceType;
    private String brand;
    private String UDID;
    private String OSVersion;

    public FirstRunData(String ip, String userAgent, String width, String height, String gpu, String referrer, String client, String storeName, String appVersion, String deviceType, String brand, String UDID, String OSVersion) {
        this.timeStamp = System.currentTimeMillis();
        this.ip = ip;
        this.userAgent = userAgent;
        this.width = width;
        this.height = height;
        this.gpu = gpu;
        this.referrer = referrer;
        this.client = client;
        this.storeName = storeName;
        this.appVersion = appVersion;
        this.deviceType = deviceType;
        this.brand = brand;
        this.UDID = UDID;
        this.OSVersion = OSVersion;
    }

    @Override
    public String toString() {
        return "galois," +
                "\"userAgent\"=\"" + Utils.replaceSpaces(userAgent) + "\"," +
                "\"timeStamp\"=\"" + timeStamp + "\"," +
                "\"width\"=\"" + Utils.replaceSpaces(width) + "\"," +
                "\"height\"=\"" + Utils.replaceSpaces(height) + "\"," +
                "\"gpu\"=\"" + Utils.replaceSpaces(gpu) + "\"," +
                "\"ip\"=\"" + Utils.replaceSpaces(ip) + "\"," +
                "\"referrer\"=\"" + Utils.replaceSpaces(referrer) + "\"," +
                "\"client\"=\"" + Utils.replaceSpaces(client) + "\"," +
                "\"storeName\"=\"" + Utils.replaceSpaces(storeName) + "\"," +
                "\"appVersion\"=\"" + Utils.replaceSpaces(appVersion) + "\"," +
                "\"deviceType\"=\"" + Utils.replaceSpaces(deviceType) + "\"," +
                "\"brand\"=\"" + Utils.replaceSpaces(brand) + "\"," +
                "\"UDID\"=\"" + Utils.replaceSpaces(UDID) + "\" " +
                "\"OSVersion\"=\"" + Utils.replaceSpaces(OSVersion) + "\"";
    }
}
