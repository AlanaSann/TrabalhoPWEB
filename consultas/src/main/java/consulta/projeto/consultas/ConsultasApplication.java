package consulta.projeto.consultas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ConsultasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsultasApplication.class, args);
	}

}
