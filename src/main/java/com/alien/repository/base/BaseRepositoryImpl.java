package com.alien.repository.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alien.page.Pager;

/**
 * Created by konghao on 2016/12/7.
 */
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements BaseRepository<T, ID> {

	@Autowired
	private EntityManager entityManager;

	private Class<T> clazz;

	@SuppressWarnings({ "rawtypes", "unused" })
	private final JpaEntityInformation entityInformation;

	// 父类没有不带参数的构造方法，这里手动构造父类
	public BaseRepositoryImpl(JpaEntityInformation<T, ?> ef, EntityManager em) {

		super(ef, em);

		this.entityManager = em;
		this.clazz = ef.getJavaType();
		this.entityInformation = ef;

	}

	// 通过EntityManager来完成查询
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listBySQL(String sql) {
		return entityManager.createNativeQuery(sql).getResultList();
	}

	@Override
	public void updateBySql(String sql, Object... args) {
		Query query = entityManager.createNativeQuery(sql);
		int i = 0;
		for (Object arg : args) {
			query.setParameter(++i, arg);
		}
		query.executeUpdate();
	}

	@Override
	public void updateByHql(String hql, Object... args) {
		Query query = entityManager.createQuery(hql);
		int i = 0;
		for (Object arg : args) {
			System.out.println(arg);
			query.setParameter(++i, arg);
		}
		query.executeUpdate();
	}
	public void saveEntity(T t) {
		 entityManager.persist(t);
	}
	@SuppressWarnings("unchecked")
	public T findById(T t, Object id) {
		return (T) entityManager.find(t.getClass(), id);
	}

	public void delete(Object id) {
		Query query = entityManager.createQuery("delete from " + clazz.getSimpleName() + " p where p.id = ?1");
		query.setParameter(1, id);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		String hql = "select t from " + clazz.getSimpleName() + " t";
		Query query = entityManager.createQuery(hql);
		List<T> beans = query.getResultList();
		return beans;
	}

	@SuppressWarnings({ "unchecked" })
	public Pager findsqlpage(String tableName, String fields, String sqlCondition, List<String> list, Pager pager) {
		List<T> param = new ArrayList<T>();
		String sqls = null;
		if (tableName != null && fields != null) {
			sqls = "select " + fields + " from " + tableName;

			try {
				if (sqlCondition != null) {
					sqls = sqls + " " + sqlCondition;
				}

				Query query = entityManager
						.createNativeQuery(pager.getPageMySQL(sqls, pager.getPageNumber(), pager.getPageSize()), clazz);

				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						query.setParameter(i + 1, list.get(i));
					}
					param = query.getResultList();
					pager.setTotalCount(findsqltotalcount(tableName, fields, sqlCondition, list));
					pager.initPageBean(pager.getTotalCount(), pager.getPageSize());
					pager.setResult(param);
					pager.setCurrentCount(param.size());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pager;
	}

	@SuppressWarnings({ "unchecked" })
	public Pager findsqlpage(String tableName, String fields, List<String> list, Pager pager) {
		List<T> param = new ArrayList<T>();
		String sqls = null;
		if (tableName != null && fields != null) {
			sqls = "select " + fields + " from " + tableName;

			try {

				Query query = entityManager
						.createNativeQuery(pager.getPageMySQL(sqls, pager.getPageNumber(), pager.getPageSize()), clazz);

				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						query.setParameter(i + 1, list.get(i));
					}
					param = query.getResultList();
					pager.setTotalCount(findsqltotalcount(tableName, fields, list));
					pager.initPageBean(pager.getTotalCount(), pager.getPageSize());
					pager.setResult(param);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pager;
	}

	@SuppressWarnings("rawtypes")
	public int findsqltotalcount(String tableName, String fields, List<String> list) {
		int totalcount = 0;
		String sqls = "select " + fields + " from " + tableName;
		try {
			Query query = entityManager.createNativeQuery(sqls, clazz);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					query.setParameter(i + 1, list.get(i));
				}
				List lists = query.getResultList();
				totalcount = lists.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalcount;
	}

	@SuppressWarnings("rawtypes")
	public int findsqltotalcount(String tableName, String fields, String sqlCondition, List<String> list) {
		int totalcount = 0;
		String sqls = "select " + fields + " from " + tableName;
		try {

			if (sqlCondition != null) {
				sqls = sqls + " " + sqlCondition;
			}
			Query query = entityManager.createNativeQuery(sqls, clazz);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					query.setParameter(i + 1, list.get(i));
				}
				List lists = query.getResultList();
				totalcount = lists.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalcount;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int deleteEqualField(String field, Object value) {
		Query query = entityManager
				.createQuery("delete from " + clazz.getSimpleName() + " p where p." + field + " = ?1");
		query.setParameter(1, value);
		return query.executeUpdate();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void update(T bean) {
		entityManager.merge(bean);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int batchDelete(List<Object> ids) {
		StringBuffer hql = new StringBuffer("delete from " + clazz.getSimpleName() + " where id  in(:ids)");
		Query query = entityManager.createQuery(hql.toString());
		query.setParameter("ids", ids);
		return query.executeUpdate();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteLikeField(String field, String value) {
		Query query = entityManager
				.createQuery("delete from " + clazz.getSimpleName() + " p where p." + field + " like ?1");
		query.setParameter(1, value);
		return query.executeUpdate();
	}

}