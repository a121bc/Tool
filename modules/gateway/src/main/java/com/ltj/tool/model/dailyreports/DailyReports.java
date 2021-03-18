package com.ltj.tool.model.dailyreports;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 考勤日报
 *
 * @author Liu Tian Jun
 * @date 2021-03-15 11:59
 */
@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "DailyReports")
public class DailyReports {

    private Integer result;
    private Integer total;
    private Integer count;
    private Integer page;
    @JacksonXmlProperty(localName = "record")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<DailyRecord> records;

    @Data
    @NoArgsConstructor
    @JacksonXmlRootElement(localName = "record")
    public static class DailyRecord {
        private String staffNo;
        private String attNo;
        private String staffName;
        private String deptName;
        private String deptNo;
        private String date;
        private String week;
        private String workType;
        private String signinTime;
        private String signoutTime;
        private String workTimeName;
        private Integer shouldAtt;
        private Integer actualAtt;
        private Integer workingTime;
        private Integer absent;
        private Integer late;
        private Integer leaveEarly;
        private Integer overtime;
        private Integer holidayot;
        private Integer restdayot;
        private Integer leavewithpay;
        private Double dayOflwp;
        private Double leavewithoutpay;
        private Double dayOflwop;
        private Double ondutyDay;
        private String leaveName;
        private Integer lackOfRecords;
        private Integer exceptions;
    }
}
