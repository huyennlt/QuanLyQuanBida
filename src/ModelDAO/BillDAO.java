/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import entityModel.Bill;
import entityModel.Billinfo;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import utils.jdbcHelper;

/**
 *
 * @author MINH DANG
 */
public class BillDAO extends SysDAO<Bill, Integer>{
    
    String SELECT_ALL_SQL = "SELECT * FROM Bill";
    String INSERT_SQL = "INSERT INTO Bill(DateCheckIn, idTable, status) VALUES (?, ?, ?)";
    String UPDATE_SQL = "UPDATE Bill SET DateCheckOut = ?, idTable = ?, status = ?, totalPrice = ? WHERE id = ?";
    String DELETE_SQL = "DELETE Bill WHERE id = ?";
    String SELECT_BY_ID = "SELECT * FROM Bill WHERE id = ?";
    String SELECT_ID_BY_IDTABLE = "select id from bill where idTable = ?";
    String SELECT_DATECHECKIN_BY_IDTABLE = "select DateCheckIn from Bill where idTable = ?";

    
    @Override
    public void insert(Bill entitype) {
        jdbcHelper.exeUpdate(INSERT_SQL, entitype.getDatecheckin(),entitype.getIdtable(), entitype.getStatus());
    }

    @Override
    public void update(Bill entitype) {
        jdbcHelper.exeUpdate(UPDATE_SQL,
                entitype.getDatecheckout(), 
                entitype.getIdtable(), 
                entitype.getStatus(),
                entitype.getTotalPrice(),
                entitype.getId());
    }

    @Override
    public void delete(Integer id) {
        jdbcHelper.exeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<Bill> selectAll() {
       return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public Bill findById(Integer id) {
        List<Bill> list = selectBySQL(SELECT_BY_ID, id);
        return list.size() > 0 ? list.get(0) : null; 
    }

    @Override
    public List<Bill> selectBySQL(String sql, Object... args) {
        List<Bill> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.exeQuery(sql, args);
            while (rs.next()) {                
                Bill b = new Bill();
                b.setId(rs.getInt("Id"));
                b.setDatecheckin(rs.getDate("DateCheckIn"));
                b.setDatecheckout(rs.getDate("DateCheckOut"));
                b.setIdtable(rs.getInt("idTable"));
                b.setStatus(rs.getInt("status"));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    public Integer findbyIdBill(int idtable){
        try {
            ResultSet rs = jdbcHelper.exeQuery(SELECT_ID_BY_IDTABLE, idtable);
            while (rs.next()) {                
                return rs.getInt("Id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Timestamp findDatebyIdtable(int idtable){
        try {
            ResultSet rs = jdbcHelper.exeQuery(SELECT_DATECHECKIN_BY_IDTABLE, idtable);
            while (rs.next()) {                
                return rs.getTimestamp("DateCheckIn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
