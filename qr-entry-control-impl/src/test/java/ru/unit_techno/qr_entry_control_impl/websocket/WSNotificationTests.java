package ru.unit_techno.qr_entry_control_impl.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import ru.unit_techno.qr_entry_control_impl.base.BaseTestClass;
import ru.unit_techno.qr_entry_control_impl.dto.websocket.CardReturnEvent;
import ru.unit_techno.qr_entry_control_impl.dto.websocket.QrScanEvent;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;

public class WSNotificationTests extends BaseTestClass {

    @Value("${local.server.port}")
    private int port;
    @Autowired
    private WSNotificationService notificationService;
    @Autowired
    private ObjectMapper objectMapper;

    BlockingQueue<String> blockingQueue;
    WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    @AfterEach
    public void clearQueue() {
        blockingQueue.clear();
    }

    @Test
    @DisplayName("Проверка, что из вебсокета получено оповещение о том, что QR код не найден в системе")
    public void checkQrDoesntExistNotification() throws Exception {
        StompSession session = stompClient
                .connect("ws://localhost:" + port + "/gs-guide-websocket", new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);
        session.subscribe("/qr-entry-control/events", new DefaultStompFrameHandler());

        notificationService.sendQrErrorScan("А777АА77", 0L);

        String receivedMessage = blockingQueue.poll(10, SECONDS);
        QrScanEvent result = objectMapper.readValue(receivedMessage, QrScanEvent.class);
        Assertions.assertEquals(result.getGovernmentNumber(), "Номер автомобиля: А777АА77");
        Assertions.assertEquals(result.getMessage(), "Данный QR код не найден в системе");
    }

    @Test
    @DisplayName("Проверка, что из вебсокета получено оповещение о том, что произошла ошибка при возврате карточки")
    public void checkCardReturningErrorNotification() throws Exception {
        StompSession session = stompClient
                .connect("ws://localhost:" + port + "/gs-guide-websocket", new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);
        session.subscribe("/qr-entry-control/events", new DefaultStompFrameHandler());

        notificationService.sendCardNotReturned("А777АА77", 0L);

        String receivedMessage = blockingQueue.poll(1, SECONDS);
        CardReturnEvent result = objectMapper.readValue(receivedMessage, CardReturnEvent.class);
        Assertions.assertEquals(result.getGovernmentNumber(), "Номер автомобиля: А777АА77");
        Assertions.assertEquals(result.getMessage(), "Не удалось зафиксировать возврат карточки");
    }


    class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQueue.offer(new String((byte[]) o));
        }
    }
}
