package br.com.acmattos.salesprocessor.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Entity that holds sale data.
 *
 * @author acmattos
 * @since 04/04/2018.
 */
@ToString(exclude = {"salesman"})
@Getter
@Builder
@AllArgsConstructor
class Sale implements Comparable<Sale>{
   private static final String OPEN = "[";
   private static final String CLOSE = "]";
   private static final String EMPTY = "";
   private static final String COMMA = ",";
   private static final String DASH = "-";

   /** Sale's ID. */
   private Long id;
   /** Sale's Cost. */
   private BigDecimal cost;
   /** Sale's salesman. */
   private Salesman salesman;

   /**
    * Creates Sale parser.
    * @param parsedEntityData Raw data captured from input file.
    * @return An instance of this parser.
    */
   static Sale createFrom(String[] parsedEntityData, Salesman salesman){
      if(null != parsedEntityData) {
         Sale sale = Sale.builder()
             .id(Long.parseLong(parsedEntityData[1]))
             .cost(getCost(parsedEntityData[2]))
             .salesman(salesman)
             .build();
         salesman.setSale(sale);
         return sale;
      }
      return null;
   }

   /**
    * Compare another parser to this based on sale's COST.
    * @param sale Another instance of this parser.
    * @return -1, 0, or 1 as it is numerically less than, equal to, or greater
    *         than each other.
    */
   @Override
   public int compareTo(Sale sale) {
      return sale.getCost().compareTo(this.getCost());
   }

   /**
    * Process raw sale itens data in order to find sales cost.
    * @param entityItens Raw sale itens data.
    * @return Sale's cost.
    */
   private static BigDecimal getCost(String entityItens){
      BigDecimal value = BigDecimal.ZERO;
      String[] parsedEntityItens =
          entityItens.replace(OPEN, EMPTY).replace(CLOSE, EMPTY).split(COMMA);

      if (parsedEntityItens.length > 0){
         for(String parsedEntityItem : parsedEntityItens){
            String[] parts = parsedEntityItem.split(DASH);
            if (parts.length == 3){
               value = value.add(new BigDecimal(parts[1])
                   .multiply(new BigDecimal(parts[2])));
            }
         }
      }
      return value;
   }
}
