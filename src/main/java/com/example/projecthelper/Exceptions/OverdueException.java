package com.example.projecthelper.Exceptions;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
public class OverdueException extends RuntimeException {

    private final LocalDateTime due;
    private final LocalDateTime executionTime;


    public OverdueException(LocalDateTime due, LocalDateTime executionTime) {
        super();
        this.due = due;
        this.executionTime = executionTime;
    }

    public OverdueException(String message, LocalDateTime due, LocalDateTime executionTime) {
        super(message);
        this.due = due;
        this.executionTime = executionTime;
    }

    public OverdueException(String message, Throwable cause, LocalDateTime due, LocalDateTime executionTime) {
        super(message, cause);
        this.due = due;
        this.executionTime = executionTime;
    }

    public OverdueException(Throwable cause, LocalDateTime due, LocalDateTime executionTime) {
        super(cause);
        this.due = due;
        this.executionTime = executionTime;
    }

    @Override
    public String toString(){
        return "message: "+getMessage()+"\n"+"due: "+due+"\n"+"executionTime: "+executionTime+"\n";
    }
}
