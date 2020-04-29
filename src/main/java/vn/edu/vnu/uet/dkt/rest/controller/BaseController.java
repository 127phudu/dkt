package vn.edu.vnu.uet.dkt.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import vn.edu.vnu.uet.dkt.rest.model.ApiDataResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseController {
    @Autowired
    protected ExecutorService executorService;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(5);
    }
}
