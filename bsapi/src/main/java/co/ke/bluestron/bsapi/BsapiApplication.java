package co.ke.bluestron.bsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import co.ke.bluestron.bsapi.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class BsapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsapiApplication.class, args);
	}

}
