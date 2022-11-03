/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.shelltea.warmup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@Slf4j
public class WarmUpListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private ServerProperties serverProperties;
    @Value("${management.endpoint.warm-up.enable:true}")
    private boolean enable;
    @Value("${management.endpoint.warm-up.times:5}")
    private int times;

    private static WarmUpRequest body() {
        final WarmUpRequest warmUpRequest = new WarmUpRequest();
        warmUpRequest.setValidTrue(true);
        warmUpRequest.setValidFalse(false);
        warmUpRequest.setValidString("warm up");
        warmUpRequest.setValidNumber(15);
        warmUpRequest.setValidBigDecimal(BigDecimal.TEN);
        return warmUpRequest;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (enable) {
            AnnotationConfigServletWebServerApplicationContext context =
                    (AnnotationConfigServletWebServerApplicationContext) event.getApplicationContext();

            String contextPath = serverProperties.getServlet().getContextPath();
            int port = serverProperties.getPort() != null && serverProperties.getPort() != 0 ?
                    serverProperties.getPort() : context.getWebServer().getPort();

            if (contextPath == null) {
                contextPath = "";
            }

            final String url = "http://localhost:" + port + contextPath + "/actuator/warm-up";
            log.info("Starting warm up application. Endpoint: {}, {} times", url, times);

            RestTemplate restTemplate = new RestTemplate();

            for (int i = 0; i < times; i++) {
                ResponseEntity<String> response = restTemplate.postForEntity(url, body(), String.class);
                log.debug("Warm up response:{}", response);
            }

            log.info("Completed warm up application");
        }
    }
}
