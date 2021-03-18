package com.ltj.tool.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ltj.tool.config.properties.KaoQinTongProperties;
import com.ltj.tool.model.dailyreports.DailyReports;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * 融商云考勤
 *
 * @author Liu Tian Jun
 * @date 2021-03-15 11:08
 */
@Slf4j
@Component
public class KaoQinTongHandler {

    private final static String DAILY_REPORTS_API = "http://www.kaoqintong.net/api/dailyReports.action";

    private final KaoQinTongProperties kaoQinTongProperties;

    private final RestTemplate httpsRestTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    public KaoQinTongHandler(RestTemplate httpsRestTemplate, KaoQinTongProperties kaoQinTongProperties) {
        this.httpsRestTemplate = httpsRestTemplate;
        this.kaoQinTongProperties = kaoQinTongProperties;
        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(mapper);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));

        this.httpsRestTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
    }

    public DailyReports lookupMonthlyReports() {
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("id_key", kaoQinTongProperties.getIdKey());
        body.add("s_key", kaoQinTongProperties.getSKey());
        body.add("v", "1.0");
        body.add("from", "2020-02-01");
        body.add("to", "2020-02-29");
        body.add("att_no", "4709");
        body.add("page", "0");
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<DailyReports> kaoQinResponseEntity = httpsRestTemplate.postForEntity(DAILY_REPORTS_API, httpEntity, DailyReports.class);
        return kaoQinResponseEntity.getBody();

    }

}
