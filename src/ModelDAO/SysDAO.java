/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelDAO;

import java.util.List;

/**
 *
 * @author MINH DANG
 */
public abstract class SysDAO<EntityType, KeyType> {
    public abstract void insert(EntityType entitype);    
    public abstract void update(EntityType entitype);    
    public abstract void delete(KeyType id);    
    public abstract List<EntityType> selectAll();    
    public abstract EntityType findById(KeyType id);
    public abstract List<EntityType> selectBySQL(String sql, Object ... args);
}
