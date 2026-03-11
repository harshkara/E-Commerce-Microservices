package com.practice.authService.exception;

public class AlreadyPresentException extends RuntimeException{

      public AlreadyPresentException(String message){
          super(message);
      }
}
