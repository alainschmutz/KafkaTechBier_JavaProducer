# Exercise 3

This java producer will create a topic called `ipt-spesen` and produce data every 5 seconds.

It should simulate the "expenses-flow" within ipt. For this it will randomly choose a employee, description of the expense and a random value between 0-1000 CHF.

The produced data of this topic will be used in the future examples using Kafka Streams and Kafka Connect.

## How to

1. Open up IntelliJ

1. Select _KafkaTechBier_JavaProducer_ to open it up

1. In the right bottom corner in IntelliJ -> Click _Git: xxx_ -> _Local Branch_ -> _master_ -> _Checkout_

1. In the toolbar -> _VCS_ -> _Git_ -> _Pull_ -> press _Pull_

1. Click _Maven Projects_ (which is located on the right) -> Lifecycle -> Run/doubleclick _compile_

1. Check the code and try to understand what is been coded

1. Run the code: In the toolbar -> _Run_ -> _Run_

1. Use either the Kafka UI or the consumer console to see that data has been produced

1. ... be happy that you've just created the first Java Kafka Producer :-)

# Exercise 4

We will change the producer to produce Avro instead of String.


1. Add the Avro dependencies to the pom.xml, under dependencies.

            <dependency>
                <groupId>org.apache.avro</groupId>
                <artifactId>avro</artifactId>
                <version>1.8.2</version>
            </dependency>
    
            <dependency>
                <groupId>io.confluent</groupId>
                <artifactId>kafka-avro-serializer</artifactId>
                <version>4.0.0</version>
            </dependency>

2. Add the code generator to the pom.xml, under build / plugins.

            <plugin>
                <groupId>org.apache.avro</groupId>
                <artifactId>avro-maven-plugin</artifactId>
                <version>1.8.2</version>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>schema</goal>
                        </goals>
                        <configuration>
                            <sourceDirectory>src/main/resources/avro</sourceDirectory>
                            <outputDirectory>${project.build.directory}/generated-sources</outputDirectory>
                            <stringType>String</stringType>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
    
3. The Avro schema is already at src/main/resources/avro/Expense.avsc. Have a look at it, but please don't change anything, because we will use it in other exercises. 

4. Run mvn compile (in IntelliJ in the Maven Projects tool button: Producer, Lifecycle, compile). This will generate code for the data model under target/generated-sources. 

5. The following changes are to be made in the JavaProducer.java.
   * Change the name of the topic we will produce into to ipt-spesen-avro. 

   * Add the schema registry URL which is where Avro schemas are stored: 

          properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        
   * Change the value serializer
 
          properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
          
   * Change the generic types of the producer and producerRecord variables to <String, Expense>
   * Now produce an Expense instead of a String. Hint: Use the generated Expense.newBuilder().

6. To check that you are producing good Avro, use the AVRO console consumer:

       kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic ipt-spesen-avro --from-beginning
    
Final solution is in branch "avro".

# Exercise 5

Schema Evolution. We noticed that a currency field is missing from the Expense object. How stupid of us. But hey, with Avro it's easy to make a change while keeping old clients compatible.

1. Add the following to Expense.avsc:

    {
      "name": "currency",
      "doc": "Three-letter (ISO 4217) code.",
      "type": "string"
    }

2. Perform Maven compile

3. Run JavaProducer and watch it fail. The error is due to us trying to produce a new schema which is not backwards compatible. 

4. Let's make it backwards-compatible by adding the following after "type":

       "default": "CHF"
       
5. Perform Maven compile

6. Run JavaProducer and watch it succeed. Results as usual can be seen using kafka-avro-console-consumer.

# Next exercise

Kafka Streams: https://github.com/fabiohecht/techbierkafkastreamsexercise


