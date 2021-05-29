package br.com.devcave.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class WebFluxJavaApplication {
	static {
		BlockHound.install();
	}

	public static void main(String[] args) {
		SpringApplication.run(WebFluxJavaApplication.class, args);
	}

}
