package com.alien.repository.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alien.page.Pager;

/**
 * 
 */
@NoRepositoryBean
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

	public List<Object[]> listBySQL(String sql);

	@Transactional
	public void updateBySql(String sql, Object... args);

	@Transactional
	public void updateByHql(String hql, Object... args);
	
	void saveEntity(T t);

	/**
	 * 
	 * @param t  实体对象
	 * @param id 主键
	 * @return 实体
	 */
	T findById(T t, Object id);

	/**
	 * 
	 * @param t 实体对象
	 */
	void update(T t);
	
	

	/**
	 * @param id 主键
	 */
	void delete(Object id);
	/**
	   * 批量删除
	 * @param ids
	 * @return
	 */
	public int batchDelete(List<Object> ids);
	/**
	 * like匹配删除
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	int deleteLikeField(String field, String value);
	/**
	 * 查询所有数据
	 */
	public List<T> findAll();

	/**
	 * 
	 * @param tableName    表名
	 * @param fields       查询的字段 eg:id,name……
	 * @param sqlCondition 查询条件eg:where id=?
	 * @param list         条件参数集合 list.add()
	 * @param pager        分页实体
	 * @return
	 */
	public Pager findsqlpage(String tableName, String fields, String sqlCondition, List<String> list, Pager pager);

	/**
	 * 
	 * @param tableName 表名
	 * @param fields    查询的字段 eg:id,name……
	 * @param list      条件参数集合 list.add()
	 * @param pager     分页实体
	 * @return
	 */
	public Pager findsqlpage(String tableName, String fields, List<String> list, Pager pager);
	
	/**
	 * 
	 * @param tableName 表名
	 * @param fields 查询的字段 eg:id,name……
	 * @param sqlCondition 查询条件eg:where id=?
	 * @param list 条件参数集合 list.add()
	 * @return
	 */
	public int findsqltotalcount(String tableName, String fields, String sqlCondition, List<String> list);
	
	/**
	 * 
	 * @param tableName 表名
	 * @param fields 查询的字段 eg:id,name……
	 * @param sqlCondition 查询条件eg:where id=?
	 * @param list 条件参数集合 list.add()
	 * @return
	 */
	public int findsqltotalcount(String tableName, String fields, List<String> list);

	/**
	 * 根据字段删除数据
	 * 
	 * @param field 字段
	 * @param value 值
	 * @return
	 */
	int deleteEqualField(String field, Object value);

}