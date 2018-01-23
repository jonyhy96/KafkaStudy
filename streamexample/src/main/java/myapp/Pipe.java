package myapp;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Pipe {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,"stream-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String,String> source = builder.stream("streams-plaintext-input");
        source.to("streams-pipe-output");
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology,props);
        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook"){
            @Override
            public void run(){
                streams.close();
                latch.countDown();
            }
        });
        try{
            streams.start();
            latch.await();
        }catch(Throwable e ){
            System.exit(1);
        }
        System.exit(0);
    }
}
