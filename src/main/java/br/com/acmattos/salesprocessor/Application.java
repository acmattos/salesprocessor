package br.com.acmattos.salesprocessor;

import br.com.acmattos.salesprocessor.file.Environment;
import br.com.acmattos.salesprocessor.file.FileDigester;
import br.com.acmattos.salesprocessor.file.InputDirectoryListener;

import java.io.IOException;

/**
 * Bootstraps the application.
 *
 * @author acmattos
 * @since 03/04/2018.
 */
public class Application {
   /**
    * Bootstrap the application.
    * @param args not used
    * @throws IOException In case of error accessing input file resources.
    */
   public static void main(String[] args) throws IOException {
      InputDirectoryListener inputDirectoryListener =
         new InputDirectoryListener(new Environment(), new FileDigester());
      inputDirectoryListener.listenToEvents();
   }
}
