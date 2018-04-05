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
public class Output {
   private static final String TC_MESSAGE =
       "Quantidade de clientes no arquivo de entrada: ";
   private static final String TS_MESSAGE =
       "Quantidade de vendedores no arquivo de entrada: ";
   private static final String MESI_MESSAGE = "ID da venda mais cara: ";
   private static final String WSN_MESSAGE = "O pior vendedor ja encontrado: ";

   /** Output's Total of Customers. */
   private int totalOfCustomers;
   /** Output's Total of Salesmen. */
   private int totalOfSalesmen;
   /** Output's Most Expensive Sale ID. */
   private long mostExpensiveSaleId;
   /** Output's Worst Salesman Name. */
   private String worstSalesmanName;

   /**
    * @return Total of Customers Message.
    */
   public String getTotalOfCustomersMessage() {
      return TC_MESSAGE + this.totalOfCustomers;
   }

   /**
    * @return Total of Salesmen Message.
    */
   public String getTotalOfSalesmenMessage() {
      return TS_MESSAGE + this.totalOfSalesmen;
   }

   /**
    * @return Most Expensive Sale ID Message.
    */
   public String getMostExpensiveSaleIdMessage() {
      return MESI_MESSAGE + this.mostExpensiveSaleId;
   }

   /**
    * @return Worst Salesman Name Message.
    */
   public String getWorstSalesmanNameMessage() {
      return WSN_MESSAGE + this.worstSalesmanName;
   }
}
