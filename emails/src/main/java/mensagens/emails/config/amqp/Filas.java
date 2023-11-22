package mensagens.emails.config.amqp;

import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class Filas {
    @Bean
    public Queue pacientQueue(){
         return QueueBuilder.durable("pacientQueue").build();
    }
}
