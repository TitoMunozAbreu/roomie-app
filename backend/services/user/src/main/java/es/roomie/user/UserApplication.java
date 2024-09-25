package es.roomie.user;

import es.roomie.user.model.TaskHistory;
import es.roomie.user.model.User;
import es.roomie.user.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository userRepository) {
		return args -> {
			boolean isUser = userRepository
					.findById("f3f42941-99e2-43a7-a80e-871339a57f13")
					.isPresent();

			if(!isUser){
				User newUser = User.builder()
						.id("f3f42941-99e2-43a7-a80e-871339a57f13")
						.build();
				userRepository.insert(newUser);
			}
		};
	}
}
