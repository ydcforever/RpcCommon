import com.rpc.kafka.consumer.ConsumerHandler;
import com.rpc.kafka.consumer.YKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by T440 on 2019/3/4.
 */
public class ConsumerTest {

    public static void main(String[] args) {

        YKafkaConsumer<String, String> kafkaConsumer = new YKafkaConsumer<>("ydc", new ConsumerHandler<String, String>() {
            @Override
            public void handler(ConsumerRecord<String, String> record) {
                System.out.println(record.value());
            }
        }).build();
        kafkaConsumer.start();
    }
}
