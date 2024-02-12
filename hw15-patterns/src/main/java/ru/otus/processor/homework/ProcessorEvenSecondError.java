package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorEvenSecondError implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorEvenSecondError(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 != 0) {
            return message;
        }
        throw new EvenSecondException("Even second call");
    }
}
