package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.ItemBO;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.ItemDAO;
import lk.ijse.posm.dao.custom.QueryDAO;
import lk.ijse.posm.dto.ItemDTO;
import lk.ijse.posm.entity.Item_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {

    QueryDAO queryDAO= (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY_DAO);

    ItemDAO itemDAO= (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    @Override
    public int getTotalItemSalesCount() throws SQLException, ClassNotFoundException {
        return queryDAO.getTotalItemSalesCount();
    }

    @Override
    public String generateNextItemId() throws SQLException, ClassNotFoundException {
        return itemDAO.generateNewId();
    }

    @Override
    public boolean saveItemDetails(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return itemDAO.save(new Item_Entity(
                itemDTO.getItem_code(),itemDTO.getItem_type(),
                itemDTO.getQty_on_hand(),itemDTO.getUnit_price(),
                itemDTO.getImage()
        ));
    }

    @Override
    public boolean updateItemDetails(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return itemDAO.update(new Item_Entity(
                itemDTO.getItem_code(),itemDTO.getItem_type(),
                itemDTO.getQty_on_hand(),itemDTO.getUnit_price(),
                itemDTO.getImage()
        ));
    }

    @Override
    public ArrayList<ItemDTO> getALLItem() throws SQLException, ClassNotFoundException {
        ArrayList<Item_Entity> itemEntities=itemDAO.getAll();
        ArrayList<ItemDTO> itemDTOS=new ArrayList<>();
        for (Item_Entity itemEntity:itemEntities) {
            itemDTOS.add(new ItemDTO(itemEntity.getItem_code(),itemEntity.getItem_type(),
                    itemEntity.getQty_on_hand(),itemEntity.getUnit_price(),
                    itemEntity.getImage())
            );
        }
        return itemDTOS;
    }

    @Override
    public ItemDTO searchItem(String code) throws SQLException, ClassNotFoundException {
        Item_Entity itemEntity=itemDAO.search(code);
        return new ItemDTO(
                itemEntity.getItem_code(),
                itemEntity.getItem_type(),
                itemEntity.getQty_on_hand(),
                itemEntity.getUnit_price(),
                null
        );
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        return itemDAO.getAllIds();
    }

    @Override
    public int getSelectedItemQtyOnHand(String code) throws SQLException, ClassNotFoundException {

        ResultSet resultSet= SQLUtil.execute("SELECT qty_on_hand FROM Item WHERE item_code = ?",code);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }
}
