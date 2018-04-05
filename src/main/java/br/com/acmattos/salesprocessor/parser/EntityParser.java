package br.com.acmattos.salesprocessor.parser;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Entity that holds customer data.
 *
 * @author acmattos
 * @since 04/04/2018.
 */
public class EntityParser {
   
   private static final String DELIMITER = "\\u00E7";
   private static final String SALESPERSON_DATA = "001";
   private static final String CUSTOMER_DATA = "002";
   private static final String SALES_DATA = "003";
   
   private Set<Customer> customers;
   private Set<Salesman> salesmen;
   private Set<Sale> sales;

   /**
    * Creates an instance of parser raw data parser.
    */
   public EntityParser(){
      this.customers = new HashSet<>();
      this.salesmen = new HashSet<>();
      this.sales = new TreeSet<>();
   }

   /**
    * Parses parser raw data.
    * @param entityData Entity raw data
    */
   public void parse(String entityData){
      String[] parsedEntityData = entityData.split(DELIMITER);
      if (parsedEntityData.length > 0 && parsedEntityData.length == 4) {
         parseEntityBasedOnItsType(parsedEntityData);
         System.out.println("Line parsed: " + entityData);
      } else {
         System.err.println("Invalid parser data: " + entityData);
      }
   }

   /**
    * Builds output data for a given set of parse operations.
    * @return Output raw data.
    */
   public Output buildOutput(){
      Output output = null;
      if(!this.customers.isEmpty())
         output = Output.builder()
             .totalOfCustomers(this.customers.size())
             .totalOfSalesmen(this.salesmen.size())
             .mostExpensiveSaleId(getMostExpensiveSaleId())
             .worstSalesmanName(getWorstSalesmanName())
             .build();
      this.clearData();
      return output;
   }

   /**
    * Detects parser type data and proceds with proper parser.
    * @param parsedEntityData Parsed parser data.
    */
   private void parseEntityBasedOnItsType(String[] parsedEntityData) {
      if (SALESPERSON_DATA.equals(parsedEntityData[0])) {
         Salesman salesman = Salesman.createFrom(parsedEntityData);
         this.salesmen.add(salesman);
      } else if (CUSTOMER_DATA.equals(parsedEntityData[0])) {
         Customer customer = Customer.createFrom(parsedEntityData);
         this.customers.add(customer);
      } else if (SALES_DATA.equals(parsedEntityData[0])) {
         Sale sale = Sale.createFrom(parsedEntityData,
             getSalesmanByName(parsedEntityData[3]));
         this.sales.add(sale);
      }
   }

   /**
    * Gets a salesman by his name.
    * @param name Salesman's name.
    * @return The proper salesmen or null if none was found.
    */
   private Salesman getSalesmanByName(String name){
      for (Salesman salesman : this.salesmen){
         if (salesman.getName().equals(name)){
            return salesman;
         }
      }
      return null;
   }

   /**
    * @return The most expensive sale ID.
    */
   private Long getMostExpensiveSaleId(){
      Long mostExpensiveSaleId = null;
      if (this.sales.size() > 0){
         Sale mostExpensiveSale = ((TreeSet<Sale>)this.sales).first();
         mostExpensiveSaleId = mostExpensiveSale.getId();
      }
      return mostExpensiveSaleId;
   }

   /**
    * @return The worst salesman name.
    */
   private String getWorstSalesmanName(){
      String worstSalesmanName = null;
      if (this.salesmen.size() > 0){
         TreeSet<Salesman> sortedSalesmen = new TreeSet<>(this.salesmen);
         Salesman worstSalesman = sortedSalesmen.first();
         worstSalesmanName = worstSalesman.getName();
         sortedSalesmen.clear();
      }
      return worstSalesmanName;
   }

   /**
    * Clear all parser set.
    */
   private void clearData(){
      this.customers.clear();
      this.salesmen.clear();
      this.sales.clear();
   }
}
