/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utils.jdbcHelper;

/**
 *
 * @author MINH DANG
 */
public class DoanhThuDAO {
    public List<Object[]> getRevenueByDay(Date startday, Date endday) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "{call GetRevenueByDay(?,?)}";
                rs = jdbcHelper.exeQuery(sql, startday, endday);
                while (rs.next()) {
                    Object[] model = {
                        rs.getDate("SaleDate"),
                        rs.getFloat("DailyRevenue")
                    };
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return list;
    }
}
