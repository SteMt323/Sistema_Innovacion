package ni.edu.uam.innovacion;

import ni.edu.uam.innovacion.modules.auth.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class InnovacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(InnovacionApplication.class, args);
	}

}
