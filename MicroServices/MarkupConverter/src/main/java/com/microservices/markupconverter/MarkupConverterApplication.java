package com.microservices.markupconverter;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.microservices.markupconverter.*")
public class MarkupConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarkupConverterApplication.class, args);
    }

    @Bean
    public Parser parser() {

        return Parser.builder().build();
    }

    @Bean
    public HtmlRenderer htmlRenderer() {
        return HtmlRenderer.builder().build();
    }

}
