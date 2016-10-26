package com.miqian.mq.entity;

import java.math.BigDecimal;

/**
 * @author wangduo
 * @description: ${todo}
 * @email: cswangduo@163.com
 * @date: 16/10/19
 */
public class CurrentProjectInfo {

    private String productCode; // 产品编号
    private String subjectId; // 产品编号
    private String subjectName; // 产品名称
    private int canTransfer; // 是否支持转让 0:否 1:是
    private String limit; // 项目期限	服务器端这里是默认返回“活存活取”
    private BigDecimal fromInvestmentAmount; // 最小投资金额
    private BigDecimal subjectMaxBuy; // 最大累计投资金额
    private BigDecimal residueAmt; // 剩余可认购金额
    private int subjectStatus; // 产品状态 1:创建 2:待审核 3:待上线 4:待开标 5:开标 6:满标 7:下架
    private BigDecimal subjectTotalPrice; // 标的总额
    private String yearInterest; //	年利率
    private String prodId; // 产品类型 1:定期赚 2:定期计划 3:活期 99:其他
    private String riskCheck; // 风险审核
    private String securityGuarantee; // 安全保障 该字段暂没有用
    private String personTime; // 认购人数
    private String description; // 产品描述
    private String unitAmount; // 每份投资金额
    private String nextInvestAmount; // 续投金额 这个是自动续投时用到的
    private String addInterestRate; // 加息产品 后台有做乘以100操作
    private long startTimestamp; // 投标开始时间 时间戳
    private long endTimestamp; // 投标结束时间 时间戳
    private long fullTimestamp; // 满标时间 时间戳
    private String interestAccrualMode; // 计息模式 MB:满标 BMB:不满标
    private String interestStartDay; // 计息开始时间 0:为当日计息 1:为次日计息
    private String subjectType; // 标的类型 00：标准标的 01：新手专属 02：众人拾财专属 03：老财主专享回馈 04：定向大额投资人

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCanTransfer() {
        return canTransfer;
    }

    public void setCanTransfer(int canTransfer) {
        this.canTransfer = canTransfer;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public BigDecimal getFromInvestmentAmount() {
        return fromInvestmentAmount;
    }

    public void setFromInvestmentAmount(BigDecimal fromInvestmentAmount) {
        this.fromInvestmentAmount = fromInvestmentAmount;
    }

    public BigDecimal getSubjectMaxBuy() {
        return subjectMaxBuy;
    }

    public void setSubjectMaxBuy(BigDecimal subjectMaxBuy) {
        this.subjectMaxBuy = subjectMaxBuy;
    }

    public BigDecimal getResidueAmt() {
        return residueAmt;
    }

    public void setResidueAmt(BigDecimal residueAmt) {
        this.residueAmt = residueAmt;
    }

    public int getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(int subjectStatus) {
        this.subjectStatus = subjectStatus;
    }

    public BigDecimal getSubjectTotalPrice() {
        return subjectTotalPrice;
    }

    public void setSubjectTotalPrice(BigDecimal subjectTotalPrice) {
        this.subjectTotalPrice = subjectTotalPrice;
    }

    public String getYearInterest() {
        return yearInterest;
    }

    public void setYearInterest(String yearInterest) {
        this.yearInterest = yearInterest;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getRiskCheck() {
        return riskCheck;
    }

    public void setRiskCheck(String riskCheck) {
        this.riskCheck = riskCheck;
    }

    public String getSecurityGuarantee() {
        return securityGuarantee;
    }

    public void setSecurityGuarantee(String securityGuarantee) {
        this.securityGuarantee = securityGuarantee;
    }

    public String getPersonTime() {
        return personTime;
    }

    public void setPersonTime(String personTime) {
        this.personTime = personTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(String unitAmount) {
        this.unitAmount = unitAmount;
    }

    public String getNextInvestAmount() {
        return nextInvestAmount;
    }

    public void setNextInvestAmount(String nextInvestAmount) {
        this.nextInvestAmount = nextInvestAmount;
    }

    public String getAddInterestRate() {
        return addInterestRate;
    }

    public void setAddInterestRate(String addInterestRate) {
        this.addInterestRate = addInterestRate;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public long getFullTimestamp() {
        return fullTimestamp;
    }

    public void setFullTimestamp(long fullTimestamp) {
        this.fullTimestamp = fullTimestamp;
    }

    public String getInterestAccrualMode() {
        return interestAccrualMode;
    }

    public void setInterestAccrualMode(String interestAccrualMode) {
        this.interestAccrualMode = interestAccrualMode;
    }

    public String getInterestStartDay() {
        return interestStartDay;
    }

    public void setInterestStartDay(String interestStartDay) {
        this.interestStartDay = interestStartDay;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
}
