package deusto.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.jfr.FlightRecorder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.Duration;
import java.util.*;

public class KafkaManager {

    private String ip;

    public KafkaManager() {

        this.ip = null;

    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    // Producer Funtions
    public void sendJson(String path, String topic){

        // Obejto de propiedades
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ip);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());

        // Producer
        final KafkaProducer<String, JSONObject> producer = new KafkaProducer<String, JSONObject>(prop);

        // Json Objetc
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(path));

            ProducerRecord<String, JSONObject> recod = new ProducerRecord<>(topic, "0", jsonObject);

            producer.send(recod);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close
        producer.flush();
        producer.close();

    }

    public void sendFileJson(String path, String topic){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File f:
                folder.listFiles()) {
            sendJson(f.getPath(),topic);
        }

    }

    // Consumer Functions
    public ArrayList<String> listTopics(){

        if (ip == null) return new ArrayList<String>();

        Properties properties = new Properties();
        properties.put("bootstrap.servers", ip);
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        Map<String, List<PartitionInfo>> topics = consumer.listTopics();
        consumer.close();

        return new ArrayList<String>(topics.keySet());
    }

    public ArrayList<String> reciveData(String topic){

        // Obejto de propiedades
        Properties prop = new Properties();
        prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ip);
        prop.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "java-group");
        prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(prop);
        consumer.subscribe(Arrays.asList(topic));

        ArrayList<String> res = new ArrayList<>();

        try{

            ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : records){
                res.add(record.value());
            }

        } catch (Exception e){

        } finally {
            consumer.close();
        }

        return res;
    }

    public void downloadData(String path, String topic){

        ArrayList<String> res = reciveData(topic);
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        int count = 1;
        for (String s: res) {

            JsonObject json = parser.parse(s).getAsJsonObject();
            String out = gson.toJson(json);

            try {
                FileWriter fw = new FileWriter(new File(path + "/" + count + ".json"));

                for (int i = 0; i < out.length(); i++) {
                    fw.write(out.charAt(i));
                }

                count++;
                fw.flush();
                fw.close();

            } catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    public static void main(String[] args) {
        KafkaManager k = new KafkaManager();
        k.sendFileJson("src/main/resources","jsons");
        k.downloadData("C:/Users/Asier/Desktop/accesos_directos/puta/","jsons");

    }

}
