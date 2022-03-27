package cuckoofilter;

import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.grpc.ManagedChannel;
import io.grpc.Channel;

public class CuckooFilterClient {
    private static final Logger logger = Logger.getLogger(CuckooFilterClient.class.getName());

    private final CuckooFilterGrpc.CuckooFilterBlockingStub blockingStub;

    public CuckooFilterClient(Channel channel) {
        blockingStub = CuckooFilterGrpc.newBlockingStub(channel);
    }

    public static void main(String[] args) {
        logger.info("Start running CuckooFilter client...");
        String address = "127.0.0.1"; // change to your own server ip
        ManagedChannel channel = ManagedChannelBuilder.forAddress(address, 50051).usePlaintext().build();
        try {
            CuckooFilterClient client = new CuckooFilterClient(channel);
            String filterName = "f1";
            client.createFilter(filterName, 100000000);

            String[] elements = new String[100];
            for (int i = 0; i < 100; i++) {
                elements[i] = String.valueOf(i);
            }
            client.insertElements(filterName, elements);

            client.insertElement(filterName, "100");

            client.countElements(filterName);

            client.lookupElement(filterName, "100");

            client.deleteElement(filterName, "100");

            client.countElements(filterName);

            client.lookupElements(filterName, new String[] { "1", "2", "3" });

            client.resetFilter(filterName);
            client.countElements(filterName);

            client.createFilter("f2", 100000000);
            client.listFilters();

            client.deleteFilter("f2");
            client.listFilters();
        } finally {
            channel.shutdown();
        }
    }

    public void createFilter(String filterName, long capacity) {
        CreateFilterRequest req = CreateFilterRequest.newBuilder().setFilterName(filterName).setCapacity(capacity)
                .build();
        CreateFilterResponse resp;
        try {
            resp = blockingStub.createFilter(req);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("创建成功？%s", resp.getStatus().getMsg()));
    }

    public void insertElement(String filterName, String element) {
        InsertElementRequest req = InsertElementRequest.newBuilder().setFilterName(filterName).setElement(element)
                .build();
        InsertElementResponse resp;
        try {
            resp = blockingStub.insertElement(req);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("插入%s成功？%s", element, resp.getStatus().getMsg()));
    }

    public void insertElements(String filterName, String[] elements) {
        InsertElementsRequest req = InsertElementsRequest.newBuilder().setFilterName(filterName)
                .addAllElements(Arrays.asList(elements))
                .build();
        InsertElementsResponse resp;
        try {
            resp = blockingStub.insertElements(req);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("插入失败的元素：%s", resp.getFailedElementsList()));
    }

    public void countElements(String filterName) {
        CountElementsRequest req = CountElementsRequest.newBuilder().setFilterName(filterName).build();
        CountElementsResponse resp;
        try {
            resp = blockingStub.countElements(req);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("当前元素个数：%s", resp.getLen()));
    }

    public void lookupElement(String filterName, String element) {
        LookupElementRequest req = LookupElementRequest.newBuilder().setFilterName(filterName).setElement(element)
                .build();
        LookupElementResponse resp;
        try {
            resp = blockingStub.lookupElement(req);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("查到了吗？%s", resp.getStatus().getMsg()));
    }

    public void deleteElement(String filterName, String element) {
        DeleteElementRequest req = DeleteElementRequest.newBuilder().setFilterName(filterName).setElement(element)
                .build();
        DeleteElementResponse resp;
        try {
            resp = blockingStub.deleteElement(req);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("删除成功？%s", resp.getStatus().getMsg()));
    }

    public void lookupElements(String filterName, String[] elements) {
        LookupElementsRequest req = LookupElementsRequest.newBuilder().setFilterName(filterName)
                .addAllElements(Arrays.asList(elements)).build();
        LookupElementsResponse resp;
        try {
            resp = blockingStub.lookupElements(req);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("存在的元素: %s", resp.getMatchedElementsList())); // 会误判，判断为存在的，可能不存在
        logger.info(String.format("不存在的元素: %s", resp.getUnmatchedElementsList())); // 可靠的，判断为不存在的，一定不存在
    }

    public void resetFilter(String filterName) {
        ResetFilterRequest req = ResetFilterRequest.newBuilder().setFilterName(filterName).build();
        ResetFilterResponse resp;
        try {
            resp = blockingStub.resetFilter(req);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("清空成功？%s", resp.getStatus().getMsg()));
    }

    public void listFilters() {
        ListFiltersResponse resp;
        try {
            resp = blockingStub.listFilters(null);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("显示所有filter：%s", resp.getFiltersList()));
    }

    public void deleteFilter(String filterName) {
        DeleteFilterRequest req = DeleteFilterRequest.newBuilder().setFilterName(filterName).build();
        DeleteFilterResponse resp;
        try {
            resp = blockingStub.deleteFilter(req);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info(String.format("删除filter：%s", resp.getStatus().getMsg()));
    }
}