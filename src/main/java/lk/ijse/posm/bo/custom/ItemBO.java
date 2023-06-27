package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ItemBO extends SuperBO {
    int getTotalItemSalesCount() throws SQLException, ClassNotFoundException;

    String generateNextItemId() throws SQLException, ClassNotFoundException;

    boolean saveItemDetails(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    boolean updateItemDetails(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getALLItem() throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException;

    List<String> getAllIds() throws SQLException, ClassNotFoundException;

    int getSelectedItemQtyOnHand(String code) throws SQLException, ClassNotFoundException;
}
