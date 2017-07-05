package com.miqian.mq.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolei_wang on 2017/6/26.
 * Email: gwzheng521@163.com
 * Description: 秒钱项目基类
 *
     {
     "code":"000000",
     "message":"成功",
     "data":{
     "productCode":"MQBXSB11706281652001",
     "productName":"杨宇帮帅虎秒钱宝一期",
     "pubUserId":"2",
     "pubUserType":3,
     "startTime":"2017-06-28 16:55:00",
     "startTimeStamp":1498640100000,
     "endTime":"2017-08-01 16:50:00",
     "endTimeStamp":1501577400000,
     "productType":3,
     "status":"5",
     "channelCode":"["APP","PC","WAP","CPS"]",
     "interestAccrualMode":"BMB",
     "bidType":"XSB1",
     "maxInvestAmount":10000,
     "unitAmount":100,
     "minInvestAmount":100,
     "bidAmount":10000,
     "investedAmount":0,
     "frozenAmount":0,
     "remainAmount":10000,
     "yearRate":16,
     "addInterestRate":1,
     "investedCount":0,
     "productRate":15,
     "createTime":"2017-06-28 16:52:56",
     "investType":1,
     "investedProgress":"0",
     "currentTime":"2017-06-30 14:35:11",
     "currentTimeStamp":1498804511498
     }
     }
 */

public class ProductBaseInfo {

    /**
     * 产品编号
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 开始时间
     */
    private long startTimeStamp;

    /**
     * 结束时间
     */
    private long endTimeStamp;

    /**
     * 产品类型
     * 3 秒钱宝 1 定期项目 2 定期计划
     */
    private int productType;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 产品状态： 1 创建,2 待审核,3 待上线, 4 待开标,5 开标,6 满标,7 已结束未满标,8 满标下线,9 未认购下线
     */
    private String status;

    /**
     * 计息模式
     * MB 满标,BMB 不满标
     */
    private String interestAccrualMode;


    /**
     * 标的类型
     * PTB 普通标,QDB 渠道标,XSB1 新手标1,XSB2 新手标2,SBB 双倍收益标,SBSYK 可使用双倍收益卡标
     */
    private String bidType;

    /**
     * 最大累计投资金额
     */
    private BigDecimal maxInvestAmount;

    /**
     * 每份投资金额
     */
    private BigDecimal unitAmount;

    /**
     * 最小投资金额
     */
    private BigDecimal minInvestAmount;

    /**
     * 最大年利率
     */
    private String maxRate;


    /**
     * 标的总额
     */
    private BigDecimal bidAmount;

    /**
     * 已投资金额
     */
    private BigDecimal investedAmount;

    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 剩余金额
     */
    private BigDecimal remainAmount;

    /**
     * 所展示的年利率
     */
    private String yearRate;

    /**
     * 产品加息
     */
    private String addInterestRate;

    /**
     * 计息开始时间
     * 1 为次日计息 0 为当日计息
     */
    private int interestStartDay;

    /**
     * 投资人次
     */
    private int investedCount;


    /**
     * 产品本身的年利率
     */
    private String productRate;

    /**
     * 投资进度
     */
    private float investedProgress;

    /**
     * 是否支持转让
     * 0 不可以 1 可以
     */
    private int canTransfer;

    /**
     * 产品期限
     */
    private String productTerm;

    private ArrayList<ColumnInfo> titleColumn;

    /**
     * 标签
     */
    private ArrayList<String> productTags;

    /**
     * 活动标题
     */
    private String activityTitle;

    /**
     * 活动跳转地址
     */
    private String activityJumpUrl;


    public ArrayList<String> getProductTags() {
        return productTags;
    }

    public void setProductTags(ArrayList<String> productTags) {
        this.productTags = productTags;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityJumpUrl() {
        return activityJumpUrl;
    }

    public void setActivityJumpUrl(String activityJumpUrl) {
        this.activityJumpUrl = activityJumpUrl;
    }

    public ArrayList<ColumnInfo> getTitleColumn() {
        return titleColumn;
    }

    public void setTitleColumn(ArrayList<ColumnInfo> titleColumn) {
        this.titleColumn = titleColumn;
    }

    public class ColumnInfo {
        private String title;
        private String value;
        private String jumpUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getJumpUrl() {
            return jumpUrl;
        }

        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl;
        }
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCanTransfer() {
        return canTransfer;
    }

    public void setCanTransfer(int canTransfer) {
        this.canTransfer = canTransfer;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getInterestAccrualMode() {
        return interestAccrualMode;
    }

    public void setInterestAccrualMode(String interestAccrualMode) {
        this.interestAccrualMode = interestAccrualMode;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public BigDecimal getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(BigDecimal maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public BigDecimal getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(BigDecimal minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public String getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(String maxRate) {
        this.maxRate = maxRate;
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(BigDecimal investedAmount) {
        this.investedAmount = investedAmount;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getYearRate() {
        return yearRate;
    }

    public void setYearRate(String yearRate) {
        this.yearRate = yearRate;
    }

    public String getAddInterestRate() {
        return addInterestRate;
    }

    public void setAddInterestRate(String addInterestRate) {
        this.addInterestRate = addInterestRate;
    }

    public int getInterestStartDay() {
        return interestStartDay;
    }

    public void setInterestStartDay(int interestStartDay) {
        this.interestStartDay = interestStartDay;
    }

    public int getInvestedCount() {
        return investedCount;
    }

    public void setInvestedCount(int investedCount) {
        this.investedCount = investedCount;
    }

    public String getProductRate() {
        return productRate;
    }

    public void setProductRate(String productRate) {
        this.productRate = productRate;
    }

    public float getInvestedProgress() {
        return investedProgress;
    }

    public void setInvestedProgress(float investedProgress) {
        this.investedProgress = investedProgress;
    }

    public String getProductTerm() {
        return productTerm;
    }

    public void setProductTerm(String productTerm) {
        this.productTerm = productTerm;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int flags) {
//        parcel.writeString(productCode);
//        parcel.writeString(productName);
//        parcel.writeLong(startTimeStamp);
//        parcel.writeLong(endTimeStamp);
//        parcel.writeInt(productType);
//        parcel.writeString(description);
//        parcel.writeString(status);
//        parcel.writeString(interestAccrualMode);
//        parcel.writeSerializable(maxInvestAmount);
//        parcel.writeSerializable(unitAmount);
//        parcel.writeSerializable(minInvestAmount);
//        parcel.writeString(maxRate);
//        parcel.writeSerializable(bidAmount);
//        parcel.writeSerializable(investedAmount);
//        parcel.writeSerializable(frozenAmount);
//        parcel.writeSerializable(remainAmount);
//        parcel.writeString(yearRate);
//        parcel.writeString(addInterestRate);
//        parcel.writeInt(interestStartDay);
//        parcel.writeInt(investedCount);
//        parcel.writeString(productRate);
//        parcel.writeFloat(investedProgress);
//        parcel.writeInt(canTransfer);
//        parcel.writeString(productTerm);
//    }
//
//    public ProductBaseInfo(Parcel source) {
//        productCode = source.readString();
//        productName = source.readString();
//        startTimeStamp = source.readLong();
//        endTimeStamp = source.readLong();
//        productType = source.readInt();
//        description = source.readString();
//        status = source.readString();
//        interestAccrualMode = source.readString();
//        maxInvestAmount = (BigDecimal) source.readSerializable();
//        unitAmount = (BigDecimal) source.readSerializable();
//        minInvestAmount = (BigDecimal) source.readSerializable();
//        maxRate = source.readString();
//        bidAmount = (BigDecimal) source.readSerializable();
//        investedAmount = (BigDecimal) source.readSerializable();
//        frozenAmount = (BigDecimal) source.readSerializable();
//        remainAmount = (BigDecimal) source.readSerializable();
//        yearRate = source.readString();
//        addInterestRate = source.readString();
//        interestStartDay = source.readInt();
//        investedCount = source.readInt();
//        productRate = source.readString();
//        investedProgress = source.readFloat();
//        canTransfer = source.readInt();
//        productTerm = source.readString();
//    }
//
//    public static final Creator<ProductBaseInfo> CREATOR = new Creator<ProductBaseInfo>() {
//        @Override
//        public ProductBaseInfo createFromParcel(Parcel parcel) {
//            return null;
//        }
//
//        @Override
//        public ProductBaseInfo[] newArray(int i) {
//            return new ProductBaseInfo[i];
//        }
//    };
}
