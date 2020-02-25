package com.rednavis.client;

import com.rednavis.backend.ServerModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ServerModule.class})
public class MaasVaadinApplication {

  public static void main(String[] args) {
    SpringApplication.run(MaasVaadinApplication.class, args);
  }

}
