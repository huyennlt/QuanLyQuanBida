/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import entityModel.Account;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.jdbcHelper;

/**
 *
 * @author MINH DANG
 */
public class AccountDAO extends SysDAO<Account, String>{

    String SELECT_ALL_SQL = "SELECT * FROM ACCOUNT";
    String INSERT_SQL = "INSERT INTO ACCOUNT VALUES (?, ?, ?, ?)";
    String DELETE_SQL = "DELETE ACCOUNT WHERE UserName = ?";
    String UPDATE_SQL = "UPDATE ACCOUNT SET DisplayName = ?, Password = ?, Type = ? WHERE UserName = ?";
    String SELECT_BY_ID = "SELECT * FROM ACCOUNT WHERE UserName = ?";
    
    @Override
    public void insert(Account entitype) {
        jdbcHelper.exeUpdate(INSERT_SQL, entitype.getUsername(), entitype.getDisplayname(), entitype.getPassword(), entitype.getType());
    }

    @Override
    public void update(Account entitype) {
        jdbcHelper.exeUpdate(UPDATE_SQL, entitype.getDisplayname(), entitype.getPassword(), entitype.getType(), entitype.getUsername());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.exeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<Account> selectAll() {
        return selectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public Account findById(String id) {
       List<Account> list = selectBySQL(SELECT_BY_ID, id);
       return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Account> selectBySQL(String sql, Object... args) {
        List<Account> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.exeQuery(sql, args);
            while (rs.next()) {                
                Account ac = new Account();
                ac.setUsername(rs.getString("Username"));
                ac.setDisplayname(rs.getString("Displayname"));
                ac.setPassword(rs.getString("PassWord"));
                ac.setType(rs.getInt("Type"));
                list.add(ac);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return list;
    }
    
    
    public List<Account> selectByName(String Username) {
        String sql = "select* from Account where Username like ? ";
        return this.selectBySQL(sql, "%" + Username + "%");
    }
    
}
