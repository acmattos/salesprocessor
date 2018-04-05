package br.com.acmattos.salesprocessor.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

/**
 * Entity that holds customer data.
 *
 * @author acmattos
 * @since 04/04/2018.
 */
@ToString
@Builder
@AllArgsConstructor
class Customer {
   /** Customer's CNPJ. */
   private long cnpj;
   /** Customer's Name. */
   private String name;
   /** Customer's Business Area. */
   private String businessArea;
    
   /**
    * Creates Customer parser.
    * @param parsedEntityData Raw data captured from input file.
    * @return An instance of this parser.
    */
   static Customer createFrom(String[] parsedEntityData){
      if(null != parsedEntityData) {
         return Customer.builder()
            .cnpj(Long.parseLong(parsedEntityData[1]))
            .name(parsedEntityData[2])
            .businessArea(parsedEntityData[3])
            .build();
      }
      return null;
   }
}
