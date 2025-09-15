//package com.crediya.sqs.listener.helper;
//
//
//import com.crediya.sqs.listener.config.SQSProperties;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//import software.amazon.awssdk.services.sqs.SqsAsyncClient;
//import software.amazon.awssdk.services.sqs.model.*;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.function.Function;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class SQSListenerTest {
//
//    @Mock
//    private SqsAsyncClient sqsAsyncClient;
//
//    @Mock
//    private SQSProperties sqsProperties;
//
//    @Mock
//    private Function<Message, Mono<Void>> processor;
//
//    private SQSListener sqsListener;
//
//    private static final String QUEUE_URL = "https://sqs.us-east-1.amazonaws.com/123456789/test-queue";
//    private static final int NUMBER_OF_THREADS = 2;
//    private static final int MAX_NUMBER_OF_MESSAGES = 10;
//    private static final int WAIT_TIME_SECONDS = 20;
//    private static final int VISIBILITY_TIMEOUT_SECONDS = 30;
//
//    @BeforeEach
//    void setUp() {
//        when(sqsProperties.queueUrl()).thenReturn(QUEUE_URL);
//        when(sqsProperties.numberOfThreads()).thenReturn(NUMBER_OF_THREADS);
//        when(sqsProperties.maxNumberOfMessages()).thenReturn(MAX_NUMBER_OF_MESSAGES);
//        when(sqsProperties.waitTimeSeconds()).thenReturn(WAIT_TIME_SECONDS);
//        when(sqsProperties.visibilityTimeoutSeconds()).thenReturn(VISIBILITY_TIMEOUT_SECONDS);
//
//        sqsListener = SQSListener.builder()
//                .client(sqsAsyncClient)
//                .properties(sqsProperties)
//                .processor(processor)
//                .build();
//    }
//
//    @Test
//    void shouldStartListenerSuccessfully() {
//        // Given
//        ReceiveMessageResponse emptyResponse = ReceiveMessageResponse.builder()
//                .messages(List.of())
//                .build();
//
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(emptyResponse));
//
//        // When
//        SQSListener result = sqsListener.start();
//
//        // Then
//        assertNotNull(result);
//        assertEquals(sqsListener, result);
//
//        // Verify that receiveMessage is called (might take a moment due to async nature)
//        verify(sqsProperties).numberOfThreads();
//        verify(sqsProperties).queueUrl();
//    }
//
//    @Test
//    void shouldProcessMessageSuccessfully() {
//        // Given
//        String messageId = "test-message-id";
//        String messageBody = "test message body";
//        String receiptHandle = "test-receipt-handle";
//
//        Message message = Message.builder()
//                .messageId(messageId)
//                .body(messageBody)
//                .receiptHandle(receiptHandle)
//                .build();
//
//        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
//                .messages(List.of(message))
//                .build();
//
//        DeleteMessageResponse deleteResponse = DeleteMessageResponse.builder().build();
//
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(response));
//        when(processor.apply(any(Message.class)))
//                .thenReturn(Mono.empty());
//        when(sqsAsyncClient.deleteMessage(any(DeleteMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(deleteResponse));
//
//        // When - Test the internal listen method via reflection or create a test-friendly version
//        // For this test, we'll verify the behavior through the start method
//        sqsListener.start();
//
//        // Then - Verify interactions (with some delay for async processing)
//        verify(processor, timeout(1000)).apply(message);
//        verify(sqsAsyncClient, timeout(1000)).deleteMessage(any(DeleteMessageRequest.class));
//    }
//
//    @Test
//    void shouldHandleEmptyMessageResponse() {
//        // Given
//        ReceiveMessageResponse emptyResponse = ReceiveMessageResponse.builder()
//                .messages(List.of())
//                .build();
//
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(emptyResponse));
//
//        // When
//        sqsListener.start();
//
//        // Then
//        verify(sqsAsyncClient, timeout(1000).atLeastOnce())
//                .receiveMessage(any(ReceiveMessageRequest.class));
//        verifyNoInteractions(processor);
//    }
//
//    @Test
//    void shouldHandleMultipleMessages() {
//        // Given
//        Message message1 = Message.builder()
//                .messageId("msg-1")
//                .body("body-1")
//                .receiptHandle("receipt-1")
//                .build();
//
//        Message message2 = Message.builder()
//                .messageId("msg-2")
//                .body("body-2")
//                .receiptHandle("receipt-2")
//                .build();
//
//        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
//                .messages(List.of(message1, message2))
//                .build();
//
//        DeleteMessageResponse deleteResponse = DeleteMessageResponse.builder().build();
//
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(response));
//        when(processor.apply(any(Message.class)))
//                .thenReturn(Mono.empty());
//        when(sqsAsyncClient.deleteMessage(any(DeleteMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(deleteResponse));
//
//        // When
//        sqsListener.start();
//
//        // Then
//        verify(processor, timeout(1000)).apply(message1);
//        verify(processor, timeout(1000)).apply(message2);
//        verify(sqsAsyncClient, timeout(1000).times(2))
//                .deleteMessage(any(DeleteMessageRequest.class));
//    }
//
//    @Test
//    void shouldContinueOnProcessorError() {
//        // Given
//        Message message = Message.builder()
//                .messageId("error-msg")
//                .body("error body")
//                .receiptHandle("error-receipt")
//                .build();
//
//        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
//                .messages(List.of(message))
//                .build();
//
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(response));
//        when(processor.apply(any(Message.class)))
//                .thenReturn(Mono.error(new RuntimeException("Processing error")));
//
//        // When
//        sqsListener.start();
//
//        // Then - Should not fail the entire flow
//        verify(processor, timeout(1000)).apply(message);
//        // Message should not be deleted when processing fails
//        verify(sqsAsyncClient, timeout(1000).never())
//                .deleteMessage(any(DeleteMessageRequest.class));
//    }
//
//    @Test
//    void shouldBuildReceiveMessageRequestCorrectly() {
//        // Given
//        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
//                .messages(List.of())
//                .build();
//
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(response));
//
//        // When
//        sqsListener.start();
//
//        // Then
//        verify(sqsAsyncClient, timeout(1000)).receiveMessage(argThat(request ->
//                QUEUE_URL.equals(request.queueUrl()) &&
//                        MAX_NUMBER_OF_MESSAGES.equals(request.maxNumberOfMessages()) &&
//                        WAIT_TIME_SECONDS.equals(request.waitTimeSeconds()) &&
//                        VISIBILITY_TIMEOUT_SECONDS.equals(request.visibilityTimeout())
//        ));
//    }
//
//    @Test
//    void shouldBuildDeleteMessageRequestCorrectly() {
//        // Given
//        String receiptHandle = "test-receipt-handle";
//        Message message = Message.builder()
//                .messageId("test-id")
//                .body("test body")
//                .receiptHandle(receiptHandle)
//                .build();
//
//        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
//                .messages(List.of(message))
//                .build();
//
//        DeleteMessageResponse deleteResponse = DeleteMessageResponse.builder().build();
//
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(response));
//        when(processor.apply(any(Message.class)))
//                .thenReturn(Mono.empty());
//        when(sqsAsyncClient.deleteMessage(any(DeleteMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(deleteResponse));
//
//        // When
//        sqsListener.start();
//
//        // Then
//        verify(sqsAsyncClient, timeout(1000)).deleteMessage(argThat(request ->
//                QUEUE_URL.equals(request.queueUrl()) &&
//                        receiptHandle.equals(request.receiptHandle())
//        ));
//    }
//
//    @Test
//    void shouldHandleSQSReceiveMessageException() {
//        // Given
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("SQS error")));
//
//        // When
//        sqsListener.start();
//
//        // Then - Should continue despite the error (due to repeat() and error handling)
//        verify(sqsAsyncClient, timeout(1000).atLeastOnce())
//                .receiveMessage(any(ReceiveMessageRequest.class));
//        verifyNoInteractions(processor);
//    }
//
//    @Test
//    void shouldHandleDeleteMessageException() {
//        // Given
//        Message message = Message.builder()
//                .messageId("test-id")
//                .body("test body")
//                .receiptHandle("test-receipt")
//                .build();
//
//        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
//                .messages(List.of(message))
//                .build();
//
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(response));
//        when(processor.apply(any(Message.class)))
//                .thenReturn(Mono.empty());
//        when(sqsAsyncClient.deleteMessage(any(DeleteMessageRequest.class)))
//                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Delete failed")));
//
//        // When
//        sqsListener.start();
//
//        // Then
//        verify(processor, timeout(1000)).apply(message);
//        verify(sqsAsyncClient, timeout(1000)).deleteMessage(any(DeleteMessageRequest.class));
//    }
//
//    @Test
//    void shouldRespectNumberOfThreadsConfiguration() {
//        // Given
//        int customThreadCount = 5;
//        when(sqsProperties.numberOfThreads()).thenReturn(customThreadCount);
//
//        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
//                .messages(List.of())
//                .build();
//
//        when(sqsAsyncClient.receiveMessage(any(ReceiveMessageRequest.class)))
//                .thenReturn(CompletableFuture.completedFuture(response));
//
//        // When
//        sqsListener.start();
//
//        // Then
//        verify(sqsProperties).numberOfThreads();
//        // The actual threading verification is complex, but we can verify the configuration is used
//    }