package com.miqian.mq.entity;

import java.util.List;

/**
 * Created by guolei_wang on 2017/6/30.
 * Email: gwzheng521@163.com
 * Description: 秒钱项目详情类

     "productTags": [
     "不可转让",
     "100元起投",
     "次日计息"
     ],
     "activityTitle": "提示文案提示文案提示文案提示文案",
     "activityJumpUrl": "www.baidu.com",
     "titleColumn": [
     {
     "title": "风险审核",
     "value": "本息盾(厦门)金融信息服务技术有限公司"
     },
     {
     "title": "项目匹配",
     "jumpUrl":"www.baidu.com"
     },
     {
     "title": "认购记录",
     "jumpUrl":"www.baidu.com"
     },
     {
     "title": "更多信息",
     "jumpUrl":"www.baidu.com"
     }
     ]
 */

public class CurrentDetailsInfo extends ProductBaseInfo {

    /**
     * 标签
     */
    private List<String> productTags;

    /**
     * 活动标题
     */
    private String activityTitle;

    /**
     * 活动跳转地址
     */
    private String activityJumpUrl;

    private List<ColumnInfo> titleColumn;

    public List<String> getProductTags() {
        return productTags;
    }

    public void setProductTags(List<String> productTags) {
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

    public List<ColumnInfo> getTitleColumn() {
        return titleColumn;
    }

    public void setTitleColumn(List<ColumnInfo> titleColumn) {
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
}
