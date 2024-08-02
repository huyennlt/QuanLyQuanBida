/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import entityModel.Bill;
import entityModel.FoodCategory;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.jdbcHelper;

/**
 *
 * @author ACER
 */
public class FoodCategoryDAO extends SysDAO<FoodCategory, Integer> {

    String SELECT_ALL_SQL = "SELECT * FROM FoodCategory";
    String INSERT_SQL = "INSERT INTO FoodCategory (name) VALUES (?)";
    String UPDATE_SQL = "UPDATE FoodCategory SET name = ? WHERE id = ?";
    String DELETE_SQL = "DELETE FoodCategory WHERE id = ?";
    String SELECT_BY_ID = "SELECT * FROM FoodCategory WHERE ID = ?";

    @Override
    public void insert(FoodCategory entitype) {
        jdbcHelper.exeUpdate(INSERT_SQL, entitype.getName());
    }

    @Override
    public void update(FoodCategory entitype) {
        jdbcHelper.exeUpdate(UPDATE_SQL, entitype.getName(), entitype.getId());
    }

    @Override
    public void delete(Integer id) {
        jdbcHelper.exeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<FoodCategory> selectAll() {
        return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public FoodCategory findById(Integer id) {
        List<FoodCategory> list = selectBySQL(SELECT_BY_ID, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<FoodCategory> selectBySQL(String sql, Object... args) {
       List<FoodCategory> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.exeQuery(sql, args);
            while (rs.next()) {                
                FoodCategory f = new FoodCategory();
                f.setId(rs.getInt("Id"));
                f.setName(rs.getString("name"));
                list.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

      public List<FoodCategory> selectById(String name) {
        String sql = "select* from FoodCategory where name like ? ";
        return this.selectBySQL(sql, "%" + name + "%");
    }
    
}
