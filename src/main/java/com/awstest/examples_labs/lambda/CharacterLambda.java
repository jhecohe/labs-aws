package com.awstest.examples_labs.lambda;

import com.awstest.examples_labs.lambda.domain.Character;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CharacterLambda {

    // Funciona con GET
    @Bean
    public Supplier<Map<String, Object>> createCharacter() {
        return () -> {
            Map<String, Object> character = new HashMap<>();
            character.put("nombre", "Goku");
            character.put("healthPoints", 100);
            character.put("skills", "Kame hame ha");

            return character;
        };
    }

    // Funciona con POST
    @Bean
    public Function<Map<String, Object>, String> receiveObject() {
        StringBuffer character = new StringBuffer();

        return mapa -> {
            mapa.forEach((key, value) -> {
                character.append(value.toString() + " - ");
            });
            return character.toString();
        };
    }

    // Post
    // Recibimos un json y automaticamente serializa al objeto retornamos el objeto
    @Bean
    public Function<Character, Character> receiveCharacter() {
        return character -> {
            character.setHealthPoints(character.getHealthPoints() + 10);
            return character;
        };
    }

    //Recibimos un json y retornamos un json
    @Bean
    public Function<
        Map<String, Object>,
        Map<String, Object>
    > processCharacter() {
        return requestJson -> {
            StringBuffer character = new StringBuffer();
            requestJson.forEach((key, value) -> {
                character.append(value.toString() + " - ");
            });

            System.out.println(character.toString());

            Map<String, Object> newJson = new HashMap<>();
            newJson.put("nombre", "Vegeta");
            newJson.put("healthPoints", 100);
            newJson.put(
                "skills",
                new String[] { "Bola de poder", "Kame hame ha", "Super saiyan" }
            );
            return newJson;
        };
    }
}
