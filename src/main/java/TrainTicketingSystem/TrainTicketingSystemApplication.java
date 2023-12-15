package TrainTicketingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TrainTicketingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainTicketingSystemApplication.class, args);
	}

}
