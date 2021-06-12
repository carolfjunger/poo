package models;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(JunitTestSuite.class);
      int testesFalhos = result.getFailureCount();
      if(result.wasSuccessful()) {
    	  System.out.println("Todos os testes passaram!! ");
      } else {
    	  System.out.println("Total de testes que falharam: " + Integer.toString(testesFalhos));
      }
      
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }

   }
} 