import java.util.List;

public class OrderList {
    private List<Orders> ordersList;
    private PageInfoFromResponse pageInfoFromResponse;
    private List<AvailableStationsFromResponse> availableStationsFromResponseList;

    public List<Orders> getOrdersFromResponseList() {
        return ordersList;
    }

    public void setOrdersFromResponseList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public PageInfoFromResponse getPageInfoFromResponse() {
        return pageInfoFromResponse;
    }

    public void setPageInfoFromResponse(PageInfoFromResponse pageInfoFromResponse) {
        this.pageInfoFromResponse = pageInfoFromResponse;
    }

    public List<AvailableStationsFromResponse> getAvailableStationsFromResponseList() {
        return availableStationsFromResponseList;
    }

    public void setAvailableStationsFromResponseList(List<AvailableStationsFromResponse> availableStationsFromResponseList) {
        this.availableStationsFromResponseList = availableStationsFromResponseList;
    }




}
