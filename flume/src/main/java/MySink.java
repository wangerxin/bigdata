import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySink extends AbstractSink implements Configurable {

    //创建Logger对象
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSink.class);

    private String prefix;
    private String suffix;

    public void configure(Context context) {
        prefix = context.getString("prefix", "hello:");
        suffix = context.getString("suffix");
    }

    public Status process() throws EventDeliveryException {

        //0.声明返回值状态信息
        Status status;
        //1.连接channel
        Channel ch = getChannel();
        //2.开启事物
        Transaction transaction = ch.getTransaction();
        transaction.begin();
        //3.读取事件
        Event event;
        while (true) {
            event = ch.take();
            if (event != null) {
                break;
            }
        }
        //4.处理事件
        try {
            LOG.info(prefix + new String(event.getBody()) + suffix);
            //5.提交事物
            transaction.commit();
            status = Status.READY;
        } catch (Exception e) {
            //6.失败回滚
            transaction.rollback();
            status = Status.BACKOFF;
        } finally {
            //7.关闭资源
            transaction.close();
        }
        return status;
    }
}
