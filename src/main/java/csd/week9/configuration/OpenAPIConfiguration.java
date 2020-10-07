package csd.week9.configuration;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
  info = @Info(
  title = "Book Store API",
  description = "" +
    "A basic web api for an online bookstore...",
  contact = @Contact(
    name = "DT", 
    url = "localhost", 
    email = "dt@gmail.com"
  ),
  license = @License(
    name = "My Licence", 
    url = "NA"))
  //servers = @Server(url = "http://localhost:8080/")
)
@SecurityScheme(
  name = "api", 
  scheme = "basic",
  type = SecuritySchemeType.HTTP,
  in = SecuritySchemeIn.HEADER)
@Configuration
public class OpenAPIConfiguration {
    
}