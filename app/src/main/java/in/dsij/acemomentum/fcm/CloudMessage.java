package in.dsij.acemomentum.fcm;

/**
 * Created by Vikas on 10/27/2017.
 */

public class CloudMessage {

    /**
     * SendTime : 123456
     * Sender : DSIJ Trader Firebase
     * ChannelId : trader_default_channel
     * Type : 1
     * New : false
     * ProductId : 148
     * ProductName : Pop BTST
     * Subscribed : false
     * Trial : false
     * Id : 123
     * Headline : Test Headline
     * Description : Test Description
     * Time : 123568
     * TemplateId : 1
     * PriorityId : 1
     * NotificationTitle : Test Title
     * NotificationMessage : Test Message
     * Link : http://www.dsij.in
     */

    private long SendTime;
    private String Sender;
    private String ChannelId;
    private int Type;
    private boolean New;
    private long ProductId;
    private String ProductName;
    private boolean Subscribed;
    private boolean Trial;
    private long Id;
    private String Company;
    private long ReccoDate;
    private double ReccoPrice;
    private double TargetPrice;
    private double CurrentMarketPrice;
    private int TemplateId;
    private int PriorityId;
    private String NotificationTitle;
    private String NotificationMessage;
    private String Link;

    public long getSendTime() {
        return SendTime;
    }

    public CloudMessage setSendTime(long sendTime) {
        SendTime = sendTime;
        return this;
    }

    public String getSender() {
        return Sender;
    }

    public CloudMessage setSender(String sender) {
        Sender = sender;
        return this;
    }

    public String getChannelId() {
        return ChannelId;
    }

    public CloudMessage setChannelId(String channelId) {
        ChannelId = channelId;
        return this;
    }

    public int getType() {
        return Type;
    }

    public CloudMessage setType(int type) {
        Type = type;
        return this;
    }

    public boolean isNew() {
        return New;
    }

    public CloudMessage setNew(boolean aNew) {
        New = aNew;
        return this;
    }

    public long getProductId() {
        return ProductId;
    }

    public CloudMessage setProductId(long productId) {
        ProductId = productId;
        return this;
    }

    public String getProductName() {
        return ProductName;
    }

    public CloudMessage setProductName(String productName) {
        ProductName = productName;
        return this;
    }

    public boolean isSubscribed() {
        return Subscribed;
    }

    public CloudMessage setSubscribed(boolean subscribed) {
        Subscribed = subscribed;
        return this;
    }

    public boolean isTrial() {
        return Trial;
    }

    public CloudMessage setTrial(boolean trial) {
        Trial = trial;
        return this;
    }

    public long getId() {
        return Id;
    }

    public CloudMessage setId(long id) {
        Id = id;
        return this;
    }

    public String getCompany() {
        return Company;
    }

    public CloudMessage setCompany(String company) {
        Company = company;
        return this;
    }

    public long getReccoDate() {
        return ReccoDate;
    }

    public CloudMessage setReccoDate(long reccoDate) {
        ReccoDate = reccoDate;
        return this;
    }

    public double getReccoPrice() {
        return ReccoPrice;
    }

    public CloudMessage setReccoPrice(double reccoPrice) {
        ReccoPrice = reccoPrice;
        return this;
    }

    public double getTargetPrice() {
        return TargetPrice;
    }

    public CloudMessage setTargetPrice(double targetPrice) {
        TargetPrice = targetPrice;
        return this;
    }

    public double getCurrentMarketPrice() {
        return CurrentMarketPrice;
    }

    public CloudMessage setCurrentMarketPrice(double currentMarketPrice) {
        CurrentMarketPrice = currentMarketPrice;
        return this;
    }

    public int getTemplateId() {
        return TemplateId;
    }

    public CloudMessage setTemplateId(int templateId) {
        TemplateId = templateId;
        return this;
    }

    public int getPriorityId() {
        return PriorityId;
    }

    public CloudMessage setPriorityId(int priorityId) {
        PriorityId = priorityId;
        return this;
    }

    public String getNotificationTitle() {
        return NotificationTitle;
    }

    public CloudMessage setNotificationTitle(String notificationTitle) {
        NotificationTitle = notificationTitle;
        return this;
    }

    public String getNotificationMessage() {
        return NotificationMessage;
    }

    public CloudMessage setNotificationMessage(String notificationMessage) {
        NotificationMessage = notificationMessage;
        return this;
    }

    public String getLink() {
        return Link;
    }

    public CloudMessage setLink(String link) {
        Link = link;
        return this;
    }
}
