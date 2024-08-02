/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import entityModel.Customer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.jdbcHelper;

/**
 *
 * @author MINH DANG
 */
public class CustomerDAO extends SysDAO<Customer, String>{
    
    String SELECT_ALL_SQL = "SELECT * FROM Customer";
    String INSERT_SQL = "INSERT INTO Customer VALUES (?,?,?,?,?,?)";
    String UPDATE_SQL = "UPDATE Customer SET name =?, gender =?, phonenumber =?, daycheckin =?, idcategorycustomer =? where idcustomer = ?";
    String DELETE_SQL = "DELETE Customer WHERE idcustomer = ?";
    String SELECT_BY_ID = "SELECT * FROM Customer WHERE idcustomer = ?";

    @Override
    public void insert(Customer entitype) {
        jdbcHelper.exeUpdate(INSERT_SQL, 
                entitype.getIdcustomer(), 
                entitype.getName(), 
                entitype.getGender(), 
                entitype.getPhonenumber(),
                entitype.getDaycheckin(),
                entitype.getIdcategorycustomer());
    }

    @Override
    public void update(Customer entitype) {
        jdbcHelper.exeUpdate(UPDATE_SQL,
                entitype.getName(), 
                entitype.getGender(),
                entitype.getPhonenumber(), 
                entitype.getDaycheckin(), 
                entitype.getIdcategorycustomer(),
                entitype.getIdcustomer());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.exeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<Customer> selectAll() {
        return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public Customer findById(String id) {
        List<Customer> list = selectBySQL(SELECT_BY_ID, id);
        return list.size() > 0 ? list.get(0 ) : null;
    }

    @Override
    public List<Customer> selectBySQL(String sql, Object... args) {
        List<Customer> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.exeQuery(sql, args);
            while (rs.next()) {
                Customer ct = new Customer();   
                ct.setIdcustomer(rs.getString("idcustomer"));
                ct.setName(rs.getString("name"));
                ct.setGender(rs.getString("gender"));
                ct.setPhonenumber(rs.getString("phonenumber"));
                ct.setDaycheckin(rs.getString("daycheckin"));
                ct.setIdcategorycustomer(rs.getString("idcategorycustomer"));
                
            }
        }catch(Exception e){
            throw new RuntimeException();
        }
         return list;
    }
}
