package hu.vetesii.mqerror;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class SpringConfiguration implements RabbitListenerConfigurer {

	@Value("${mq.error.mq.exchange.name}")
	private String exchangeName;

	@Value("${mq.error.mq.1.queue.name}")
	private String queueName1;

	@Value("${mq.error.mq.1.routing.name}")
	private String routingKey1;

	@Value("${mq.error.mq.2.queue.name}")
	private String queueName2;

	@Value("${mq.error.mq.2.routing.name}")
	private String routingKey2;

	@Bean
	public DirectExchange appExchange1() {
		return new DirectExchange(exchangeName);
	}

	@Bean
	public DirectExchange appExchange2() {
		return new DirectExchange(exchangeName);
	}

	@Bean
	public Queue hashQueue1() {
		return new Queue(queueName1);
	}

	@Bean
	public Queue hashQueue2() {
		return new Queue(queueName2);
	}

	@Bean("hashBinding1")
	public Binding declareBinding1() {
		return BindingBuilder.bind(hashQueue1()).to(appExchange1()).with(routingKey1);
	}

	@Bean("hashBinding2")
	public Binding declareBinding2() {
		return BindingBuilder.bind(hashQueue2()).to(appExchange2()).with(routingKey2);
	}

	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
	}

}
