package br.com.acmattos.salesprocessor.file;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Enviroment data holder: process enviroment setup, making all resources
 * available for file processing.
 *
 * @author acmattos
 * @since 03/04/2018.
 */
public class Environment {
   
   private static final String HOMEPATH = "user.home";
   private static final String DATA_IN = "/data/in";
   private static final String DATA_OUT = "/data/out";
   private static final String DATA_PROCESSED = "/data/processed";
   
   private String base;
   private Path inputPath;
   private Path outputPath;
   private Path processedPath;
   
   /**
    * Creates a new instance of Environment used inside InputDirectoryListener
    * instances.
    */
   public Environment(){
      this.init();
   }

   /**
    * Gets input path.
    * @return input path.
    */
   Path getInputPath() {
      return inputPath;
   }

   /**
    * Gets output path.
    * @return output path.
    */
   Path getOutputPath() {
      return outputPath;
   }

   /**
    * Gets processed path.
    * @return processed path.
    */
   Path getProcessedPath() {
      return processedPath;
   }
   
   /**
    * Initializes the following directories:
    * - INPUT
    * - OUTPUT
    * - PROCESSED
    */
   private void init(){
      try {
         this.initInputPath();
         this.initOutputPath();
         this.initProcessedPath();
      } catch (Exception e) {
         exit(e.getMessage());
      }
   }
   
   /**
    * Initializes INPUT directory.
    */
   private void initInputPath(){
      this.base = System.getProperty(HOMEPATH);
      if (null == this.base) {
         exit("Undefined environment variable 'user.home'");
      }
      String in = this.base + DATA_IN;
      this.inputPath = Paths.get(in);
   
      if (!this.inputPath.toFile().exists()) {
         exit("Does not exist: " + in);
      } else if (!this.inputPath.toFile().isDirectory()) {
         exit("Not a directory: " + in);
      } else if (!this.inputPath.toFile().canRead()) {
         exit("Could not read: " + in);
      }
      System.out.println("Input Path: " + this.inputPath);
   }
   
   /**
    * Initializes OUTPUT directory.
    */
   private void initOutputPath(){
      String out = this.base + DATA_OUT;
      this.outputPath = createDirectory(out);
      System.out.println("Output Path: " + this.outputPath);
   }
   
   /**
    * Initializes PROCESSED directory.
    */
   private void initProcessedPath(){
      String processed = this.base + DATA_PROCESSED;
      this.processedPath = createDirectory(processed);
      System.out.println("Processed Path: " + this.processedPath);
   }
   
   /**
    * Creates a directory for a given path.
    *
    * @param path Path that will create a directory.
    * @return A directory that corresponds to a given path.
    */
   private Path createDirectory(String path) {
      Path directory = Paths.get(path);
      if (!directory.toFile().exists()) {
         if (!directory.toFile().mkdirs()) {
            throw new RuntimeException("Could not create: " + path);
         }
      } else if (!directory.toFile().isDirectory()) {
         throw new RuntimeException("Not a directory: " + path);
      }
      return directory;
   }

   /**
    * Log message before exit
    * @param message Error message.
    */
   private void exit(String message) {
      System.err.println(message);
      System.exit(1);
   }
}
