/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import entityModel.Food;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.jdbcHelper;

/**
 *
 * @author MINH DANG
 */
public class FoodDAO extends SysDAO<Food, Integer> {

    String SELECT_ALL_SQL = "SELECT * FROM Food";
    String INSERT_SQL = "INSERT INTO Food(name, idCategory, price) VALUES (?,?,?)";
    String UPDATE_SQL = "UPDATE Food SET name = ?, idCategory = ?, price = ? WHERE id = ?";
    String DELETE_SQL = "DELETE Food WHERE id = ?";
    String SELECT_BY_ID = "select * from Food WHERE id = ?";
    String SELECT_BY_IDCATEGORY = "SELECT * FROM food WHERE idCategory = ?";
    String SELECT_BY_NAME = "select * from Food where name like ?";
        
    @Override
    public void insert(Food entitype) {
        jdbcHelper.exeUpdate(INSERT_SQL,
                entitype.getName(),
                entitype.getIdCategory(),
                entitype.getPrice());
    }

    @Override
    public void update(Food entitype) {
        jdbcHelper.exeUpdate(UPDATE_SQL, 
                entitype.getName(),
                entitype.getIdCategory(),
                entitype.getPrice(),
                entitype.getId());
    }

    @Override
    public void delete(Integer id) {
        jdbcHelper.exeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<Food> selectAll() {
        return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public Food findById(Integer id) {
        List<Food> list = selectBySQL(SELECT_BY_ID, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Food> selectBySQL(String sql, Object... args) {
        List<Food> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.exeQuery(sql, args);
            while (rs.next()) {
                Food f = new Food();
                f.setId(rs.getInt("Id"));
                f.setName(rs.getString("name"));
                f.setIdCategory(rs.getInt("idCategory"));
                f.setPrice(rs.getFloat("price"));
                list.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Food> select_by_idcategory(int idcategory){
        return selectBySQL(SELECT_BY_IDCATEGORY, idcategory);
    }
    
    public List<Food> selectbyname(String name){
       return this.selectBySQL(SELECT_BY_NAME, "%"+name+"%");
    }
}
