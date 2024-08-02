/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import entityModel.Food;
import entityModel.TableBida;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.jdbcHelper;

/**
 *
 * @author MINH DANG
 */
public class TableBidaDAO extends SysDAO<TableBida, Integer>{
    String SELECT_ALL_SQL = "SELECT * FROM TableBida";
    String INSERT_SQL = "INSERT INTO TableBida(name) VALUES (?)";
    String UPDATE_SQL = "UPDATE TableBida SET name = ?, status = ? where id = ?";
    String DELETE_SQL = "DELETE TableBida WHERE id = ?";
    String SELECT_BY_ID = "SELECT * FROM TableBida where id = ?";
    String SELECT_BY_NAME = "select * from TableBida where name like ?";
    String SWITCH_TABLE_BY_IDTABLE = "UPDATE Bill set idTable = ? where idTable = ?";
    
    @Override
    public void insert(TableBida entitype) {
        jdbcHelper.exeUpdate(INSERT_SQL, entitype.getName());
    }

    @Override
    public void update(TableBida entitype) {
        jdbcHelper.exeUpdate(UPDATE_SQL, entitype.getName(), entitype.getStatus(), entitype.getId());
    }

    @Override
    public void delete(Integer id) {
        jdbcHelper.exeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<TableBida> selectAll() {
        return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public TableBida findById(Integer id) {
        List<TableBida> list = selectBySQL(SELECT_BY_ID, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<TableBida> selectBySQL(String sql, Object... args) {
        List<TableBida> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.exeQuery(sql, args);
            while (rs.next()) {                
                TableBida tb = new TableBida();
                tb.setId(rs.getInt("id"));
                tb.setName(rs.getString("name"));
                tb.setStatus(rs.getString("status"));
                list.add(tb);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return list;
    } 
    
    //chuyển bànDAO
    public void switchTable(int idtable1, int idtable2) throws SQLException{
        try {
            try {
                String sql = "{call USP_SwitchTabel(?,?)}";
                jdbcHelper.exeUpdate(sql, idtable1, idtable2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    
    //Tìm kiếm bàn theo tên
    public List<TableBida> selectbyname(String name){
       return this.selectBySQL(SELECT_BY_NAME, "%"+name+"%");
    }
    
    //Chuyển bàn theo idtable
    public void switchtable(int tbnew, int tbold){     
        jdbcHelper.exeUpdate(SWITCH_TABLE_BY_IDTABLE, tbnew, tbold);       
    }
}
