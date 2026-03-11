package com.practice.taskmanager.exception;

public class AlreadyPresentException extends RuntimeException{

      public AlreadyPresentException(String message){
          super(message);
      }
}
