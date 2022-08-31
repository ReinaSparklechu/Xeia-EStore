package Xeia.Web;

import Xeia.Items.Item;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);
        registry.addFormatter(new Formatter<Item>() {
            @Override
            public Item parse(String text, Locale locale) throws ParseException {
                System.out.println("In parse: " + text);
                return null;
            }

            @Override
            public String print(Item object, Locale locale) {
                System.out.println(object);
                return null;
            }
        });
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("admin");
    }

}
