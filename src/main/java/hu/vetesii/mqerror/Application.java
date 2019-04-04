package hu.vetesii.mqerror;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ParameterizedTypeReference;

@SpringBootApplication
public class Application implements ApplicationListener<ApplicationReadyEvent> {
	private static final Logger log = LogManager.getLogger(Application.class);

	public static void main(String[] args) {
		System.out.println("Start application...");
		logProperties();
		SpringApplication.run(Application.class, args);
	}

	private static void logProperties() {
		System.out.println("java.version=" + System.getProperty("java.version"));
		System.out.println("log System.getProperties()...");
		for (Object key : System.getProperties().keySet()) {
			System.out.println("System+prop > " + key + "=" + System.getProperties().get(key));
		}
	}

	@Autowired
	Binding hashBinding1;

	@Autowired
	Binding hashBinding2;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		log.info("run makeRequest()...");
		makeRequest();
	}
	
	public void makeRequest() {
		try {
			CompletableFuture<String> hdt1 = sendMessage("some message",
					hashBinding1.getExchange(), hashBinding1.getRoutingKey());
			CompletableFuture<String> hdt2 = sendMessage("valami uzenet",
					hashBinding2.getExchange(), hashBinding2.getRoutingKey());
			
			CompletableFuture.allOf(hdt1, hdt2).join();
			
			log.info("Resp1: {}", hdt1.get());
			log.info("Resp2: {}", hdt2.get());
		} catch (Exception e) {
			log.error("Error in makeRequest()", e);
		}
	}
	
	public CompletableFuture<String> sendMessage(String request, String exchange, String routing) {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			log.debug("send request -> exchange={}, routing={}", exchange, routing);
			
			Map<String, String> map = new HashMap<>();
			map.put("request", request);
			
			try {
				String response = rabbitTemplate.convertSendAndReceiveAsType(
						exchange, routing, map, ParameterizedTypeReference.forType(String.class));
				
				return response;
			} catch (AmqpException e) {
				throw new CompletionException(e);
			}
		});
		
		return future;
	}
}
