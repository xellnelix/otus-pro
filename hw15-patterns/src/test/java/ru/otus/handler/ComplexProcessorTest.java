package ru.otus.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.DateTimeProvider;
import ru.otus.processor.homework.EvenSecondException;
import ru.otus.processor.homework.ProcessorEvenSecondError;
import ru.otus.processor.homework.ProcessorSwapFields;

class ComplexProcessorTest {

    private static void accept(Exception ex) {}

    @Test
    @DisplayName("Тестируем вызовы процессоров")
    void handleProcessorsTest() {
        // given
        var message = new Message.Builder(1L).field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenReturn(message);

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});

        // when
        var result = complexProcessor.handle(message);

        // then
        verify(processor1).process(message);
        verify(processor2).process(message);
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        // given
        var message = new Message.Builder(1L).field8("field8").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenThrow(new RuntimeException("Test Exception"));

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        // when
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));

        // then
        verify(processor1, times(1)).process(message);
        verify(processor2, never()).process(message);
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        // given
        var message = new Message.Builder(1L).field9("field9").build();

        var listener = mock(Listener.class);

        var complexProcessor = new ComplexProcessor(new ArrayList<>(), (ex) -> {});

        complexProcessor.addListener(listener);

        // when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        // then
        verify(listener, times(1)).onUpdated(message);
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }

    @Test
    @DisplayName("Тестируем ProcessorSwapFields")
    void handleProcessorChangeFieldsTest() {
        var message =
                new Message.Builder(1L).field11("field11").field12("field12").build();

        Processor processor = new ProcessorSwapFields();

        var processors = List.of(processor);

        var complexProcessor = new ComplexProcessor(processors, ComplexProcessorTest::accept);

        var result = complexProcessor.handle(message);

        var expected = message.toBuilder().field11("field12").field12("field11").build();

        assertThat(result.getField11()).isEqualTo(expected.getField11());
        assertThat(result.getField12()).isEqualTo(expected.getField12());
    }

    @Test
    @DisplayName("Тестируем ProcessorThrowException")
    void handleProcessorThrowExceptionIfNonEvesSecondTest() {
        DateTimeProvider dateTimeProvider = Mockito.mock(DateTimeProvider.class);
        Mockito.when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.now().withSecond(2));

        var message = new Message.Builder(1L).field11("field1").build();

        Processor processor = new ProcessorEvenSecondError(dateTimeProvider);

        var processors = List.of(processor);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});

        var result = complexProcessor.handle(message);

        assertThat(result).isEqualTo(message);

        Exception exception = assertThrows(EvenSecondException.class, () -> processor.process(message));

        assertThat(exception.getMessage()).isEqualTo("Even second call");
    }
}
