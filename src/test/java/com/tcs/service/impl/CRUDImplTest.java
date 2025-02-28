package com.tcs.service.impl;

import com.tcs.repository.IGenericRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CRUDImplTest {

    private static class TestEntity {
        private Long id;

        public TestEntity(Long id) {
            this.id = id;
        }
    }

    @Mock
    private IGenericRepository<TestEntity, Long> mockRepository;

    private CRUDImpl<TestEntity, Long> service;

    private final TestEntity mockEntity = new TestEntity(1L);

    @BeforeEach
    void setUp() {
        service = new CRUDImpl<TestEntity, Long>() {
            @Override
            protected IGenericRepository<TestEntity, Long> getRepository() {
                return mockRepository;
            }
        };
    }

    @Test
    void givenSave_ThenSuccessfullySaveTheEntity_WhenTheEntityHasData() {
        when(mockRepository.save(mockEntity)).thenReturn(Mono.just(mockEntity));
        Mono<TestEntity> result = service.save(mockEntity);
        StepVerifier.create(result)
                .expectNextMatches(saveClient -> saveClient.id.equals(1L))
                .verifyComplete();
        verify(mockRepository, times(1)).save(mockEntity);
    }

    @Test
    void givenFindAll_ThenReturnAllEntities() {
        when(mockRepository.findAll()).thenReturn(Flux.just(mockEntity));

        Flux<TestEntity> result = service.findAll();
        StepVerifier.create(result)
                .expectNext(mockEntity)
                .verifyComplete();
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    void givenFindById_WhenEntityExists_ThenReturnEntity() {
        when(mockRepository.findById(mockEntity.id)).thenReturn(Mono.just(mockEntity));

        Mono<TestEntity> result = service.findById(mockEntity.id);
        StepVerifier.create(result)
                .expectNext(mockEntity)
                .verifyComplete();
        verify(mockRepository, times(1)).findById(anyLong());
    }

    @Test
    void givenDelete_WhenEntityExists_ThenDeleteEntity() {
        when(mockRepository.findById(mockEntity.id)).thenReturn(Mono.just(mockEntity));
        when(mockRepository.deleteById(mockEntity.id)).thenReturn(Mono.empty());

        Mono<Boolean> result = service.deleteById(mockEntity.id);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
        verify(mockRepository, times(1)).findById(anyLong());
        verify(mockRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void givenDelete_WhenEntityDoesNotExist_ThenReturnFalse() {
        when(mockRepository.findById(mockEntity.id)).thenReturn(Mono.empty());

        Mono<Boolean> result = service.deleteById(1L);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(mockRepository, times(1)).findById(mockEntity.id);
        verify(mockRepository, never()).deleteById(anyLong());
    }
}