package net.hyntech.common.model.entity;


import net.hyntech.baselib.http.BaseResponse;

import java.io.Serializable;
import java.util.List;

public class NoticeListEntity extends BaseResponse<NoticeListEntity> {


    /**
     * alarmInfoList : [{"addr":"开封市尉氏县洧川乡鲁湾村东","alarmId":"2020HORa7mNWKV","createTime":"2021-01-03 10:01:13","ebikeId":"0331af41b8e04f95b66818791bf88aaf","ebikeNo":"WS068921","lat":34.31637,"lng":113.947738,"messageId":"ae29515fdd6f4281b0212a4f8a0972ea","messageType":"move","remark":"18749850659发生位移变化","ruleType":0,"state":0},{"addr":"尉氏","alarmId":"2020LeKnGTFuUd","createTime":"2020-12-31 10:39:07","ebikeId":"55d2c5a1e5424a44ae4cd29dc6a4ff1c","ebikeNo":"WS118201","lat":0,"lng":0,"messageId":"2020LeKnGTFuUd","messageType":"alarm","remark":"15938535013报警。","ruleType":0,"state":1},{"addr":"开封市杞县阳堌镇常寺村西头村口","alarmId":"2019BRUegnCnU2","createTime":"2020-12-29 00:08:50","ebikeId":"8271030b3a99418bb9882608da24d0e8","ebikeNo":"WS067188","lat":34.683543,"lng":114.782753,"messageId":"6a55569fe116463ba416ebf7932afe97","messageType":"move","remark":"15937817809发生位移变化","ruleType":0,"state":1},{"addr":"开封市尉氏县洧川乡老庄王村北","alarmId":"2020HORa7mNWKV","createTime":"2020-12-27 16:00:15","ebikeId":"0331af41b8e04f95b66818791bf88aaf","ebikeNo":"WS068921","lat":34.32099,"lng":113.949025,"messageId":"723219cefd3f4b608fb84f0b4fd59532","messageType":"move","remark":"18749850659发生位移变化","ruleType":0,"state":0},{"addr":"开封市尉州大道阮籍路交叉口","alarmId":"2020LXTiPb6juZ","createTime":"2020-12-27 14:57:40","ebikeId":"3f137c70e5c34f2ab16a3d7875341505","ebikeNo":"WS122339","lat":34.425408,"lng":114.206897,"messageId":"d4fe7fd40cba40a2baa7d5a19fe0bbbf","messageType":"move","remark":"19137892989发生位移变化","ruleType":0,"state":0},{"addr":"尉氏县三贤路一中东路口","alarmId":"2020LXTiPb6juZ","createTime":"2020-12-27 13:52:32","ebikeId":"3f137c70e5c34f2ab16a3d7875341505","ebikeNo":"WS122339","lat":34.422907,"lng":114.207265,"messageId":"098aaeaf3ce048168379d72286095e7b","messageType":"move","remark":"19137892989发生位移变化","ruleType":0,"state":0},{"addr":"尉氏县三贤路与粮店街交叉口","alarmId":"2020LXTiPb6juZ","createTime":"2020-12-27 13:51:13","ebikeId":"3f137c70e5c34f2ab16a3d7875341505","ebikeNo":"WS122339","lat":34.42341,"lng":114.20053,"messageId":"97f2b9c3c0614d84af529152194a17cf","messageType":"move","remark":"19137892989发生位移变化","ruleType":0,"state":0},{"addr":"尉氏县三贤路北街交叉口","alarmId":"2020LXTiPb6juZ","createTime":"2020-12-27 13:50:44","ebikeId":"3f137c70e5c34f2ab16a3d7875341505","ebikeNo":"WS122339","lat":34.422884,"lng":114.202711,"messageId":"f59ada27e4424b39a33bbda2123a9a28","messageType":"move","remark":"19137892989发生位移变化","ruleType":0,"state":0},{"addr":"开封市尉氏县建设路北二环交叉口","alarmId":"2020LXTiPb6juZ","createTime":"2020-12-26 15:00:09","ebikeId":"3f137c70e5c34f2ab16a3d7875341505","ebikeNo":"WS122339","lat":34.432718,"lng":114.207553,"messageId":"6bce22631d7d43cda0293c0bc4640f67","messageType":"move","remark":"19137892989发生位移变化","ruleType":0,"state":0},{"addr":"尉氏县北二环与尉缭路交叉口","alarmId":"2020LXTiPb6juZ","createTime":"2020-12-26 14:59:58","ebikeId":"3f137c70e5c34f2ab16a3d7875341505","ebikeNo":"WS122339","lat":34.433116,"lng":114.203349,"messageId":"3c7e2b56af8845ff8a1795a8c7288c86","messageType":"move","remark":"19137892989发生位移变化","ruleType":0,"state":0}]
     * page : {"firstPage":true,"lastPage":false,"list":[],"nextPage":2,"pageNo":1,"pageSize":10,"prePage":1,"totalCount":664,"totalPage":67}
     */

    private PageBean page;
    private List<AlarmInfoListBean> alarmInfoList;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<AlarmInfoListBean> getAlarmInfoList() {
        return alarmInfoList;
    }

    public void setAlarmInfoList(List<AlarmInfoListBean> alarmInfoList) {
        this.alarmInfoList = alarmInfoList;
    }

    public static class PageBean {
        /**
         * firstPage : true
         * lastPage : false
         * list : []
         * nextPage : 2
         * pageNo : 1
         * pageSize : 10
         * prePage : 1
         * totalCount : 664
         * totalPage : 67
         */

        private boolean firstPage;
        private boolean lastPage;
        private int nextPage;
        private int pageNo;
        private int pageSize;
        private int prePage;
        private int totalCount;
        private int totalPage;
        private List<?> list;

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<?> getList() {
            return list;
        }

        public void setList(List<?> list) {
            this.list = list;
        }
    }

    public static class AlarmInfoListBean implements Serializable {
        /**
         * addr : 开封市尉氏县洧川乡鲁湾村东
         * alarmId : 2020HORa7mNWKV
         * createTime : 2021-01-03 10:01:13
         * ebikeId : 0331af41b8e04f95b66818791bf88aaf
         * ebikeNo : WS068921
         * lat : 34.31637
         * lng : 113.947738
         * messageId : ae29515fdd6f4281b0212a4f8a0972ea
         * messageType : move
         * remark : 18749850659发生位移变化
         * ruleType : 0
         * state : 0
         */

        private String addr;
        private String alarmId;
        private String createTime;
        private String ebikeId;
        private String ebikeNo;
        private double lat;
        private double lng;
        private String messageId;
        private String messageType;
        private String remark;
        private int ruleType;
        private int state;

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEbikeId() {
            return ebikeId;
        }

        public void setEbikeId(String ebikeId) {
            this.ebikeId = ebikeId;
        }

        public String getEbikeNo() {
            return ebikeNo;
        }

        public void setEbikeNo(String ebikeNo) {
            this.ebikeNo = ebikeNo;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getRuleType() {
            return ruleType;
        }

        public void setRuleType(int ruleType) {
            this.ruleType = ruleType;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
