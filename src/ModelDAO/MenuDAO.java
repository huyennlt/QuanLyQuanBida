/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import entityModel.Bill;
import entityModel.Menu;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.jdbcHelper;

/**
 *
 * @author MINH DANG
 */
public class MenuDAO extends SysDAO<Menu, Integer>{

    String SELECT_BY_IDTABLE = "SELECT name, count, price, count*price as totalPrice FROM Bill inner join BillInfo on Bill.id = BillInfo.idBill inner join Food on BillInfo.idFood = Food.id WHERE idTable = ?";
    
    @Override
    public void insert(Menu entitype) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Menu entitype) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Menu> selectAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Menu findById(Integer id) {
        List<Menu> list = selectBySQL(SELECT_BY_IDTABLE, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Menu> selectBySQL(String sql, Object... args) {
        List<Menu> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.exeQuery(sql, args);
            while (rs.next()) {                
                Menu menu = new Menu();
                menu.setFoodname(rs.getString("name"));
                menu.setCount(rs.getInt("count"));
                menu.setPrice(rs.getFloat("price")); 
                menu.setTotalPrice(rs.getFloat("totalPrice"));
                list.add(menu);
            }
        } catch (Exception e) {
        }
        return list;
    } 
    
    public List<Menu> selectAllMenuByIdTable(int idtable){
        return selectBySQL(SELECT_BY_IDTABLE, idtable);
    }
}
