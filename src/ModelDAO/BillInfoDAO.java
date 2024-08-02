/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import entityModel.Billinfo;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.jdbcHelper;

/**
 *
 * @author MINH DANG
 */
public class BillInfoDAO extends SysDAO<Billinfo, Integer> {

    String SELECT_ALL_SQL = "SELECT * FROM BillInfo";
    String INSERT_SQL = "INSERT INTO BillInfo(idBill, idFood, count) VALUES (?, ?, ?)";
    String UPDATE_SQL = "UPDATE BillInfo SET idBill = ?, idFood = ?, count = ? WHERE id = ?";
    String DELETE_SQL = "DELETE BillInfo WHERE id = ?";
    String SELECT_BY_ID = "SELECT * FROM BillInfo WHERE id = ?";
    String SELECT_ID_BY_IDTABLE = "SELECT id FROM BillInfo WHERE idTable = ?";
    
    @Override
    public void insert(Billinfo entitype) {
        jdbcHelper.exeUpdate(INSERT_SQL, 
                entitype.getIdbill(),
                entitype.getIdfood(),
                entitype.getCount());
    }

    @Override
    public void update(Billinfo entitype) {
        jdbcHelper.exeUpdate(UPDATE_SQL, 
                entitype.getIdbill(), 
                entitype.getIdfood(), 
                entitype.getCount(),
                entitype.getId());
    }

    @Override
    public void delete(Integer id) {
        jdbcHelper.exeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<Billinfo> selectAll() {
        return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public Billinfo findById(Integer id) {
        List<Billinfo> list = selectBySQL(SELECT_BY_ID, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Billinfo> selectBySQL(String sql, Object... args) {
        List<Billinfo> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.exeQuery(sql, args);
            while (rs.next()) {
                Billinfo tb = new Billinfo();
                tb.setId(rs.getInt("id"));
                tb.setIdbill(rs.getInt("idBill"));
                tb.setIdfood(rs.getInt("idFood"));
                tb.setCount(rs.getInt("count"));
                list.add(tb);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return list;
    }

    
    public Billinfo findbyIdTable(int idtable){
        List<Billinfo> list = selectBySQL(SELECT_ID_BY_IDTABLE, idtable);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    
    //Xóa và lưu trữ thông tin bill
    public List<Object[]> deleteBillInfo(){
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "{call dbo.usp_LogAndShowDeletedBills}";
                rs = jdbcHelper.exeQuery(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                rs.getStatement().getConnection().close();  
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return list;
    }
}
