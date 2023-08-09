package com.dsluchenko.app.web;

import com.dsluchenko.app.data.dao.impl.CurrencyDaoJdbc;
import com.dsluchenko.app.data.dao.impl.ExchangeRateDaoJdbc;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.Service;
import com.dsluchenko.app.service.impl.CurrencyServiceImpl;
import com.dsluchenko.app.service.impl.ExchangeRateServiceImpl;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.flywaydb.core.Flyway;

import java.util.HashMap;
import java.util.Map;

@WebListener
public class ApplicationServletContext implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        new Flyway(Flyway.configure()
                .installedBy("/src/main/java/resources").loadDefaultConfigurationFiles())
                .migrate();

        ServletContext servletContext = sce.getServletContext();
        Map<String, Service> services = new HashMap<>();

        services.put(CurrencyService.class.getSimpleName(), new CurrencyServiceImpl(new CurrencyDaoJdbc()));
        services.put(ExchangeRateService.class.getSimpleName(), new ExchangeRateServiceImpl(new ExchangeRateDaoJdbc()));

        servletContext.setAttribute("services", services);

        servletContext.setAttribute(ResponseHandler.class.getSimpleName(), new ResponseHandler());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
