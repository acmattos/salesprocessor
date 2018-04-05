package br.com.acmattos.salesprocessor.file;

import br.com.acmattos.salesprocessor.parser.EntityParser;
import br.com.acmattos.salesprocessor.parser.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Input file digester: extracts and parses data from input file and disposes it
 * finishing the operation.
 *
 * @author acmattos
 * @since 03/04/2018.
 */
public class FileDigester {

   private static final String DAT_EXTENSION = ".dat";
   private static final String DONE_DAT_EXTENSION = ".done.dat";
   private static final int FIRST_INDEX = 0;
   private static final String FILE_SEPARATOR =
       FileSystems.getDefault().getSeparator();

   /**
    * Creates a new instance of FileDigester used inside InputDirectoryListener
    * instances.
    */
   public FileDigester(){
   }

   /**
    * Extracts data from input file
    * @param inputFile input file with data to be extracted.
    * @return Output raw data.
    */
   Output extract(Path inputFile){
      Output output = null;
      try {
         if (null != inputFile  && inputFile.toString().endsWith(".dat")) {
            System.out.println("Extracting data from: " + inputFile);
            List<String> lines = Files.readAllLines(inputFile);
            EntityParser entityParser = new EntityParser();
            for (String line: lines) {
               entityParser.parse(line);
            }
            output = entityParser.buildOutput();
         } else {
            System.err.format("Aborting extraction: '%s' is not a .DAT file\n",  inputFile);
         }
      } catch (IOException ioe) {
         ioe.printStackTrace();
      }
      return output;
   }

   /**
    * Compiles output raw data to an output file.
    * @param outputPath Output path directory.
    * @param inputFile Input file.
    * @param output Output raw data.
    */
   void compile(Path outputPath, Path inputFile, Output output){
      if(null != outputPath && null != inputFile && null != output){
         System.out.println("Compiling output: " + output);
         File outputFile = createDoneFile(outputPath, inputFile);
         try(PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println(output.getTotalOfCustomersMessage());
            writer.println(output.getTotalOfSalesmenMessage());
            writer.println(output.getMostExpensiveSaleIdMessage());
            writer.println(output.getWorstSalesmanNameMessage());
         } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe.getMessage());
         }
      }
   }

   /**
    * Dispose input file to PROCESSED path.
    * @param processedPath Processed path.
    * @param inputFile Input file.
    */
   void dispose(Path processedPath, Path inputFile){
      try {
         if(null != inputFile) {
            File oldInputFile = inputFile.toFile();
            File newFile = createFile(
                processedPath, inputFile.getFileName().toString());
            inputFile.toFile().renameTo(newFile);
            Files.deleteIfExists(oldInputFile.toPath());
            System.out.println("Disposing file: " + newFile);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * Create done file: the output response from file extraction.
    * @param outputPath Output path directory.
    * @param inputFile Input file.
    * @return Done file.
    */
   private File createDoneFile(Path outputPath, Path inputFile){
      String outputFileName = inputFile.getFileName().toString();
      int lastIndex = outputFileName.lastIndexOf(DAT_EXTENSION);

      StringBuilder fileName =
          new StringBuilder(outputFileName.substring(FIRST_INDEX, lastIndex))
          .append(DONE_DAT_EXTENSION);
      return createFile(outputPath, fileName.toString());
   }

   /**
    * Create file denoted by a destination path and a given name.
    * @param destinationPath Destination path.
    * @param fileName File name
    * @return A new file.
    */
   private File createFile(Path destinationPath, String fileName){
      StringBuilder fullFileName =
          new StringBuilder(destinationPath.toAbsolutePath().toString())
              .append(FILE_SEPARATOR)
              .append(fileName);
      System.out.println("New file created: " + fullFileName);
      return new File(fullFileName.toString());
   }
}
