package com.jingwei;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.jingwei.db.domain.MyEnum;

/**
 * @author xianwen.tan 枚举与数据库tinyint的映射类处理器
 */
public class EnumValueTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

	private Class<E> type;
	private final E[] enums;
	
	public EnumValueTypeHandler(Class<E> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		if (!IEnum.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException(type.getName()
					+ " must implement the interface "
					+ IEnum.class.getName());
		}

		this.type = type;
		this.enums = type.getEnumConstants();
		if (this.enums == null) {
			throw new IllegalArgumentException(type.getSimpleName()
					+ " does not represent an enum type.");
		}
		
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter,
			JdbcType jdbcType) throws SQLException {
		ps.setInt(i, ((IEnum) parameter).getValue());
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		int i = rs.getInt(columnName);
		if (rs.wasNull()) {
			return null;
		} else {
			return convert(i);
		}
	}

	private E convert(int value) {
		for (E e : enums) {
			if (value == ((IEnum) e).getValue()) {
				return e;
			}
		}
		throw new IllegalArgumentException("Cannot convert " + value + " to "
				+ type.getSimpleName() + " by value.");
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		int i = rs.getInt(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			return convert(i);
		}
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		int i = cs.getInt(columnIndex);
		if (cs.wasNull()) {
			return null;
		} else {
			return convert(i);
		}
	}

}
