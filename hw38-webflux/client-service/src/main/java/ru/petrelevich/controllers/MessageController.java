package ru.petrelevich.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.petrelevich.domain.Message;

import static java.lang.Long.parseLong;
import static ru.petrelevich.enums.Room.AGGREGATE_ROOM;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private static final String TOPIC_TEMPLATE = "/topic/response.";

    private final WebClient datastoreClient;
    private final SimpMessagingTemplate template;

    public MessageController(WebClient datastoreClient, SimpMessagingTemplate template) {
        this.datastoreClient = datastoreClient;
        this.template = template;
    }

    @MessageMapping("/message.{roomId}")
    public void getMessage(@DestinationVariable String roomId, Message message) {

        if(AGGREGATE_ROOM.getCriterion().equals(roomId)){
            logger.error("Can not save message for room {}. Operation is forbidden.", roomId);
            return;
        }

        logger.info("get message:{}, roomId:{}", message, roomId);
        saveMessage(roomId, message).subscribe(msgId -> logger.info("message send id:{}", msgId));

        convertAndSend(roomId, message);
        convertAndSend(AGGREGATE_ROOM.getCriterion(), message);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        var genericMessage = (GenericMessage<byte[]>) event.getMessage();
        var simpDestination = (String) genericMessage.getHeaders().get("simpDestination");
        if (simpDestination == null) {
            logger.error("Can not get simpDestination header, headers:{}", genericMessage.getHeaders());
            throw new ChatException("Can not get simpDestination header");
        }
        if (!simpDestination.startsWith(template.getUserDestinationPrefix())) {
            return;
        }
        var roomId = parseRoomId(simpDestination);
        logger.info("subscription for:{}, roomId:{}", simpDestination, roomId);
        /*
        /user/3c3416b8-9b24-4c75-b38f-7c96953381d1/topic/response.1
         */

        var messages = parseLong(AGGREGATE_ROOM.getCriterion()) == roomId
                ? getMessagesAll()
                : getMessagesByRoomId(roomId);

        messages
                .doOnError(ex -> logger.error("getting messages for roomId:{} failed", roomId, ex))
                .subscribe(message -> template.convertAndSend(simpDestination, message));
    }

    private void convertAndSend(String roomId, Message message){
        template.convertAndSend(
                String.format("%s%s", TOPIC_TEMPLATE, roomId), new Message(HtmlUtils.htmlEscape(message.messageStr())));
    }

    private long parseRoomId(String simpDestination) {
        try {
            var idxRoom = simpDestination.lastIndexOf(TOPIC_TEMPLATE);
            return parseLong(simpDestination.substring(idxRoom).replace(TOPIC_TEMPLATE, ""));
        } catch (Exception ex) {
            logger.error("Can not get roomId", ex);
            throw new ChatException("Can not get roomId");
        }
    }

    private Mono<Long> saveMessage(String roomId, Message message) {
        return datastoreClient
                .post()
                .uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchangeToMono(response -> response.bodyToMono(Long.class));
    }

    private Flux<Message> getMessagesByRoomId(long roomId) {
        return datastoreClient
                .get()
                .uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(this::responseMessage);
    }

    private Flux<Message> getMessagesAll() {
        return datastoreClient
                .get()
                .uri("/msg")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(this::responseMessage);
    }

    private Flux<Message> responseMessage(ClientResponse response){
        if (response.statusCode().equals(HttpStatus.OK)) {
            return response.bodyToFlux(Message.class);
        } else {
            return response.createException().flatMapMany(Mono::error);
        }
    }
}
