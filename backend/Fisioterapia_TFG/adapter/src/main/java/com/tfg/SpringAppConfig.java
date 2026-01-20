package com.tfg;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title= "fisioterapia API",
version = "1.0",
license = @License(name = "Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International Public License", url = "https://creativecommons.org/licenses/by-nc-sa/4.0/")))

public class SpringAppConfig {
}
