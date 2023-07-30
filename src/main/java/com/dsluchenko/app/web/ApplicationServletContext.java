package com.dsluchenko.app.web;

import com.dsluchenko.app.data.dao.impl.CurrencyDaoJdbc;
import com.dsluchenko.app.data.dao.impl.ExchangeRateDaoJdbc;
import com.dsluchenko.app.service.impl.CurrencyServiceImpl;
import com.dsluchenko.app.service.impl.ExchangeRateServiceImpl;
import com.dsluchenko.app.mapper.impl.CurrencyMapperImpl;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationServletContext implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        servletContext.setAttribute(CurrencyMapperImpl.class.getSimpleName(), new CurrencyMapperImpl());

        servletContext.setAttribute(CurrencyServiceImpl.class.getSimpleName(), new CurrencyServiceImpl(new CurrencyDaoJdbc()));
        servletContext.setAttribute(ExchangeRateServiceImpl.class.getSimpleName(), new ExchangeRateServiceImpl(new ExchangeRateDaoJdbc()));

        servletContext.setAttribute(ResponseHandler.class.getSimpleName(), new ResponseHandler());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
