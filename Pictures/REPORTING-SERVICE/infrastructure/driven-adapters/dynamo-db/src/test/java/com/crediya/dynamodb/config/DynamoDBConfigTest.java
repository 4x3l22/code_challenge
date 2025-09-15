package com.crediya.dynamodb.config;

import com.crediya.dynamodb.DynamoDBTemplateAdapter;
import com.crediya.dynamodb.ModelEntity;
import com.crediya.model.report.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TemplateAdapterOperationsTest {

    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private DynamoDbAsyncTable<ModelEntity> table;

    private DynamoDBTemplateAdapter dynamoDBTemplateAdapter;
    private ModelEntity modelEntity;
    private final String tableName = "test-table";
    private final String indexName = "metricId-updatedAt-index";
    private final String singlePk = "test-pk";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(ModelEntity.class)))
                .thenReturn(table);

        dynamoDBTemplateAdapter = new DynamoDBTemplateAdapter(
                dynamoDbAsyncClient,
                dynamoDbEnhancedAsyncClient,
                mapper,
                tableName,
                indexName,
                singlePk
        );

        modelEntity = new ModelEntity();
        modelEntity.setMetricId(singlePk);
        modelEntity.setCount(5L);
        modelEntity.setTotalAmountCents(new BigDecimal("100.50"));
        modelEntity.setUpdatedAt("2023-12-01T10:00:00Z");
    }

    @Test
    void modelEntityPropertiesMustNotBeNull() {
        ModelEntity modelEntityUnderTest = new ModelEntity();
        modelEntityUnderTest.setMetricId("test-id");
        modelEntityUnderTest.setCount(1L);
        modelEntityUnderTest.setTotalAmountCents(new BigDecimal("50.00"));
        modelEntityUnderTest.setUpdatedAt("2023-12-01T10:00:00Z");

        assertNotNull(modelEntityUnderTest.getMetricId());
        assertNotNull(modelEntityUnderTest.getCount());
        assertNotNull(modelEntityUnderTest.getTotalAmountCents());
        assertNotNull(modelEntityUnderTest.getUpdatedAt());
    }

    @Test
    void testUpdateReport_Success() {
        BigDecimal approvedAmount = new BigDecimal("25.75");
        UpdateItemResponse response = UpdateItemResponse.builder().build();

        when(dynamoDbAsyncClient.updateItem(any(UpdateItemRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        StepVerifier.create(dynamoDBTemplateAdapter.updateReport(approvedAmount))
                .expectComplete()
                .verify();
    }

    @Test
    void testUpdateReport_NullAmount_ThrowsException() {
        StepVerifier.create(dynamoDBTemplateAdapter.updateReport(null))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void testGet_Success() {
        Report expectedReport = new Report();
        // Set up report properties as needed

        when(table.getItem(any(java.util.function.Consumer.class)))
                .thenReturn(CompletableFuture.completedFuture(modelEntity));
        when(mapper.map(modelEntity, Report.class)).thenReturn(expectedReport);

        StepVerifier.create(dynamoDBTemplateAdapter.get())
                .expectNext(expectedReport)
                .verifyComplete();
    }

    @Test
    void testGet_EntityNotFound_ReturnsEmpty() {
        when(table.getItem(any(java.util.function.Consumer.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        StepVerifier.create(dynamoDBTemplateAdapter.get())
                .verifyComplete();
    }

    @Test
    void testModelEntity_GettersAndSetters() {
        ModelEntity entity = new ModelEntity();
        String metricId = "test-metric-id";
        Long count = 10L;
        BigDecimal totalAmount = new BigDecimal("150.25");
        String updatedAt = "2023-12-01T15:30:00Z";

        entity.setMetricId(metricId);
        entity.setCount(count);
        entity.setTotalAmountCents(totalAmount);
        entity.setUpdatedAt(updatedAt);

        assertEquals(metricId, entity.getMetricId());
        assertEquals(count, entity.getCount());
        assertEquals(totalAmount, entity.getTotalAmountCents());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }
}