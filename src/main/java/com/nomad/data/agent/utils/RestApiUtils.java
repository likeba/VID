package com.nomad.data.agent.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.nomad.data.agent.common.dto.AipHttpHeaders;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.utils.enums.AgentApiType;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestApiUtils {

    @Autowired
    static RestTemplate restTemplate = new RestTemplate();

    public static String makeUri(String target, String port, AgentApiType agentApiType){
        return makeUri(target, port, agentApiType.getValue().toString());
    }

    public static String makeUri(String target, String port, String agentApiType){
        String url = null;

        try {
            url = new URL("http", target, Integer.parseInt(port), agentApiType).toString();

        } catch (MalformedURLException e) {
            log.error(">>>>> make url error", e);
            throw new CustomException(ErrorCodeType.API_MAKE_URL_ERROR);
        }

        return url;
    }

    private static ResponseEntity<?> callApiResult(String httpUrl, Object req, HttpMethod httpMethod, AipHttpHeaders httpHeaders) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        if (!ObjectUtils.isEmpty(httpHeaders)){
            MediaType contentType = httpHeaders.getContentType();
            if (!ObjectUtils.isEmpty(contentType)){
                headers.setContentType(contentType);
            }

            List<MediaType> accecptType = httpHeaders.getAcceptType();
            if (!ObjectUtils.isEmpty(accecptType)){
                headers.setAccept(accecptType);
            }
        }

        HttpEntity<Object> requestEntity = null;

        if (HttpMethod.GET.equals(httpMethod)) {

            if (!ObjectUtils.isEmpty(req)) {
                Map<String, Object> queryParamMap = (Map) req;
                uriComponentsBuilder = setQueryParam(uriComponentsBuilder, queryParamMap);
            }
            requestEntity = new HttpEntity<>(headers);
        }

        if (HttpMethod.POST.equals(httpMethod)) {
            requestEntity = new HttpEntity<>(req, headers);
        }

        return restTemplate.exchange(uriComponentsBuilder.toUriString(), httpMethod, requestEntity, Object.class);
    }

    public static Object callApiAll(String httpUrl, Object req, HttpMethod httpMethod, AipHttpHeaders httpHeaders){
        ResponseEntity responseEntity = callApiResult(httpUrl, req, httpMethod, httpHeaders);

        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus.equals(HttpStatus.OK) || httpStatus.equals(HttpStatus.ACCEPTED)) {
            return responseEntity;
        } else {
            throw new CustomException(ErrorCodeType.API_CALL_ERROR_WRONG);
        }
    }

    public static Object callApi(String httpUrl, Object req, HttpMethod httpMethod, AipHttpHeaders httpHeaders){
        ResponseEntity responseEntity = callApiResult(httpUrl, req, httpMethod, httpHeaders);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        } else {
            throw new CustomException(ErrorCodeType.API_CALL_ERROR_WRONG);
        }
    }

    private static UriComponentsBuilder setQueryParam(UriComponentsBuilder uriComponentsBuilder, Map<String, Object> queryParamMap) {
        Set<String> keySet = queryParamMap.keySet();

        for (String key : keySet) {
            uriComponentsBuilder.queryParam(key, queryParamMap.get(key).toString());
            log.info(">>>>> query param :{}", queryParamMap.get(key));
        }
        return uriComponentsBuilder;
    }

    public static Future callApiRunnableThread(String httpUrl, Object req, HttpMethod httpMethod, AipHttpHeaders httpHeaders, ExecutorService executorService){
        return executorService.submit(new RunnableThread(httpUrl, req, httpMethod, httpHeaders));
    }

    private static class RunnableThread implements Runnable {
        String httpUrl;
        Object req;
        HttpMethod httpMethod;
        AipHttpHeaders httpHeaders;

        RunnableThread(String httpUrl, Object req, HttpMethod httpMethod, AipHttpHeaders httpHeaders){
            this.httpUrl = httpUrl;
            this.req = req;
            this.httpMethod = httpMethod;
            this.httpHeaders = httpHeaders;
        }

        public void run(){
            RestApiUtils.callApi(httpUrl, req, httpMethod, httpHeaders);
        }
    }
    
    public static Object callApiFalseBufferRequest(String httpUrl, Object req, HttpMethod httpMethod, AipHttpHeaders httpHeaders) {
    	
    	SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    	requestFactory.setBufferRequestBody(false);
    	restTemplate.setRequestFactory(requestFactory);
    	
    	ResponseEntity responseEntity = callApiResult(httpUrl, req, httpMethod, httpHeaders);
    	
    	requestFactory.setBufferRequestBody(true);
    	restTemplate.setRequestFactory(requestFactory);
    	
    	if(responseEntity.getStatusCode().equals(HttpStatus.OK)) {
    		return responseEntity.getBody();
    	}else {
    		throw new CustomException(ErrorCodeType.API_CALL_ERROR_WRONG);
    	}
    }
}

