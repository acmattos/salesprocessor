package br.com.acmattos.salesprocessor.file;

import br.com.acmattos.salesprocessor.parser.Output;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * Input path Listener, responsible for input file's digestion.
 *
 * @author acmattos
 * @since 03/04/2018.
 */
public class InputDirectoryListener {
   private WatchService watchService;
   private Environment environment;
   private FileDigester fileDigester;

   /**
    * Creates a WatchService and registers input path for listen to it.
    * @param environment Enviroment data holder.
    * @param fileDigester Input file digester.
    * @throws IOException In case of error accessing input file resources.
    */
   public InputDirectoryListener(Environment environment,
                                 FileDigester fileDigester) throws IOException{
      this.environment = environment;
      this.fileDigester = fileDigester;
      this.watchService = FileSystems.getDefault().newWatchService();
      this.environment.getInputPath().register(watchService, ENTRY_CREATE);
   }

   /**
    * Process all ENTRY_CREATE events inside input path.
    */
   public void listenToEvents() {
      for (;;) {
         WatchKey key = getWatchKey();

         for (WatchEvent<?> event: key.pollEvents()) {
            if (OVERFLOW == event.kind()) {
               continue;
            }
            digestFile(getInputFile((WatchEvent<Path>) event));
         }

         if (!key.reset()) {
            break;
         }
      }
   }

   /**
    * A token representing the registration of a watchable object with a
    * WatchService.
    * @return WatchKey.
    */
   private WatchKey getWatchKey(){
      WatchKey key = null;
      try {
         System.out.println("Listen to file events...");
         key = watchService.take();
         Thread.sleep(1000);
      } catch (InterruptedException ie) {
         // ignored
      }
      return key;
   }

   /**
    * Digests the file created.
    * @param inputFile input file to be digested.
    */
   private void digestFile(Path inputFile) {
      System.out.format("Processing: %s\n", inputFile);

      Output output = this.fileDigester.extract(inputFile);
      this.fileDigester.compile(
          this.environment.getOutputPath(), inputFile, output);
      this.fileDigester.dispose(this.environment.getProcessedPath(), inputFile);
   }

   /**
    * Gets input file.
    * @param event ENTRY_CREATE event that holds input file.
    * @return input file full path.
    */
   private Path getInputFile(WatchEvent<Path> event){
      Path inputFileName = event.context();
      return environment.getInputPath().resolve(inputFileName);
   }
}
