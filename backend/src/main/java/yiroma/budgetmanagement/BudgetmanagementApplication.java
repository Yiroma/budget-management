package yiroma.budgetmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@RestController
public class BudgetmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetmanagementApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello World!";
	}

}
