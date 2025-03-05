package com.awstest.examples_labs.lambda;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaConfig {
    
    @Bean(name = "saludar")
    public Supplier<String> print() {
        return () -> { return "Hello world"; };
    }

    @Bean
    public Function<String, String> printName() {
        return (name) -> { return name.toUpperCase(); };
    }

    @Bean
    public Consumer<String> procesado() {
        return ( estado ) -> { System.out.println("El estado del proceso " + estado); };
    }
}
