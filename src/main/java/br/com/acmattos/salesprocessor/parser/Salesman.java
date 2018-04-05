package br.com.acmattos.salesprocessor.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Entity that holds salesman data.
 *
 * @author acmattos
 * @since 04/04/2018.
 */
@ToString
@Getter
@Builder
@AllArgsConstructor
class Salesman implements Comparable<Salesman>{
   /** Salesman's CPF. */
   private long cpf;
   /** Salesman's Name. */
   private String name;
   /** Salesman's Salary. */
   private BigDecimal salary;
   /** Salesman's Sale. */
   private Sale sale;

   /**
    * Creates Salesman parser.
    * @param parsedEntityData Raw data captured from input file.
    * @return An instance of this parser.
    */
   static Salesman createFrom(String[] parsedEntityData){
      if(null != parsedEntityData) {
         return Salesman.builder()
            .cpf(Long.parseLong(parsedEntityData[1]))
            .name(parsedEntityData[2])
            .salary(new BigDecimal(parsedEntityData[3]))
            .build();
      }
      return null;
   }

   /**
    * Sets salesman's sale information.
    * @param sale Sale.
    */
   void setSale(Sale sale) {
      this.sale = sale;
   }

   /**
    * Compare this parser to another based on sale's COST.
    * @param salesman Another instance of this parser.
    * @return -1, 0, or 1 as it is numerically less than, equal to, or greater
    *         than each other.
    */
   @Override
   public int compareTo(Salesman salesman) {
      return this.getSale().getCost().compareTo(
         salesman.getSale().getCost());
   }
}

