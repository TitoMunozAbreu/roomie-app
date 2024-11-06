package es.roomie.household;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HouseholdApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseholdApplication.class, args);
	}

}
