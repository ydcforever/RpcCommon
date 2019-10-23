import com.rpc.kafka.producer.YKafkaProducer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by T440 on 2019/3/4.
 */
public class ProducerTest {

    public static void main(String[] args) {
        YKafkaProducer<String> producer = new YKafkaProducer<String>("ydc").build();
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("222");
        producer.send(list);
    }
}
