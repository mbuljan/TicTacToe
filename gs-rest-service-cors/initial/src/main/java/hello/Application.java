package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
				
		private static void startup() {
			
		}		

    public static void main(String[] args) {
				startup();
        SpringApplication.run(Application.class, args);
    }
}


