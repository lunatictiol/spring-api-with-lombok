package com.sab.lombokbasics.bootstrap;


import com.sab.lombokbasics.entities.Customer;
import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.model.JuiceCSVRecord;
import com.sab.lombokbasics.model.JuiceStyle;
import com.sab.lombokbasics.repository.CustomerRepository;
import com.sab.lombokbasics.repository.JuiceRepository;
import com.sab.lombokbasics.services.JuiceCsvService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataBootstrap implements CommandLineRunner {
    private final JuiceRepository juiceRepository;
    private final CustomerRepository customerRepository;
    private final JuiceCsvService juiceCSVService;

   @Transactional
    @Override
    public void run(String... args) throws Exception {
       loadCSVData();
        loadCustomer();
        loadJuice();

    }

    private void loadCSVData() {
        if (juiceRepository.count() <10) {
            try {
                File file = ResourceUtils.getFile("classpath:csvdata/juice.csv");
                List<JuiceCSVRecord> juiceCSVRecords = juiceCSVService.convertCSV(file);
                juiceCSVRecords.forEach(
                        juiceCSVRecord -> {
                            JuiceStyle juiceStyle = switch (juiceCSVRecord.getStyle()) {
                                case "American Pale Lager" -> JuiceStyle.LAGER;
                                case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                                        JuiceStyle.ALE;
                                case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> JuiceStyle.IPA;
                                case "American Porter" -> JuiceStyle.PORTER;
                                case "Oatmeal Stout", "American Stout" -> JuiceStyle.STOUT;
                                case "Saison / Farmhouse Ale" -> JuiceStyle.SAISON;
                                case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> JuiceStyle.WHEAT;
                                case "English Pale Ale" -> JuiceStyle.PALE_ALE;
                                default -> JuiceStyle.PILSNER;
                            };

                            juiceRepository.save(Juice.builder()
                                    .juiceName(StringUtils.abbreviate(juiceCSVRecord.getName(), 50))
                                    .juiceStyle(juiceStyle)
                                    .price(BigDecimal.TEN)
                                    .upc(juiceCSVRecord.getRow().toString())
                                    .quantityOnHand(juiceCSVRecord.getCount())
                                    .build());
                        }
                );
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }




   private void loadJuice (){
       if (juiceRepository.count() == 0) {
           Juice juice1 = Juice.builder()
                   .juiceName("Galaxy Cat")
                   .upc("12356")
                   .price(new BigDecimal("12.99"))
                   .quantityOnHand(122)
                   .createdDate(LocalDateTime.now())
                   .updateDate(LocalDateTime.now())
                   .build();

           Juice juice2 = Juice.builder()
                   .juiceName("Crank")
                   .upc("12356222")
                   .price(new BigDecimal("11.99"))
                   .quantityOnHand(392)
                   .createdDate(LocalDateTime.now())
                   .updateDate(LocalDateTime.now())
                   .build();

           Juice juice3 = Juice.builder()
                   .juiceName("Sunshine City")
                   .upc("12356")
                   .price(new BigDecimal("13.99"))
                   .quantityOnHand(144)
                   .createdDate(LocalDateTime.now())
                   .updateDate(LocalDateTime.now())
                   .build();
           juiceRepository.save(juice1);
           juiceRepository.save(juice2);
           juiceRepository.save(juice3);
       }
   }
   private void loadCustomer(){
       if (customerRepository.count() == 0) {
           Customer customer1 = Customer.builder()

                   .name("Customer 1")

                   .createdDate(LocalDateTime.now())
                   .updateDate(LocalDateTime.now())
                   .build();

           Customer customer2 = Customer.builder()

                   .name("Customer 2")

                   .createdDate(LocalDateTime.now())
                   .updateDate(LocalDateTime.now())
                   .build();

           Customer customer3 = Customer.builder()

                   .name("Customer 3")

                   .createdDate(LocalDateTime.now())
                   .updateDate(LocalDateTime.now())
                   .build();
           customerRepository.save(customer1);
           customerRepository.save(customer2);
           customerRepository.save(customer3);
       }
   }

}
