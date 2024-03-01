package org.helpdesk.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"org.helpdesk.service", "org.helpdesk.dao","org.helpdesk.models"})
public class RootConfig {



}
