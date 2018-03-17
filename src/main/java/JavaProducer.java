import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;

public class JavaProducer {

    public static void main(String[] args) throws InterruptedException {

        ArrayList<String> employees = new ArrayList<String>();
        ArrayList<String> description = new ArrayList<String>();
        ArrayList<String> expenses = new ArrayList<String>();

        // Randoms for picking value out of ArrayLists
        Random random_employee = new Random();
        Random random_description = new Random();

        // Max value of random generated number
        int maxexpensevalue = 1000;

        // Topic to produce to
        String topic = "ipt-spesen";

        // Time to sleep in ms
        int timetosleep = 5000;

        // Create new property
        Properties properties = new Properties();

        // kafka bootstrap server
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        // producer acks
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "3");
        properties.setProperty("linger.ms", "1");

        Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);

        // Fill ArrayLists with values
        Collections.addAll(employees, "RKO","DAL","PGR","DAL","SHU","TIN","LKE","TSC","ASH","FHE");
        Collections.addAll(description, "Mittagessen","Abendessen","Training","Bahn","Geschäftsauto","Wochenunterkunft","Flug","Hotel","BYO");

        // Do every x milliseconds
        while (true){
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<String, String>(
                            topic,
                            null,
                            null,

                            // pick random employee
                            employees.get(random_employee.nextInt(employees.size())) + " , "

                                    // pick random description
                                    + description.get(random_employee.nextInt(description.size())) + " , "

                                    // generate random number
                                    + (int)(Math.random() * maxexpensevalue * 100));

            producer.send(producerRecord);
            Thread.sleep(timetosleep);

        }
    }
}
