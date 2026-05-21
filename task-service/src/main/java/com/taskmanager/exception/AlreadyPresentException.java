package com.taskmanager.exception;

public class AlreadyPresentException extends RuntimeException{

      public AlreadyPresentException(String message){
          super(message);
      }
}
