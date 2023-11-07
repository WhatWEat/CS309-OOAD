package com.example.projecthelper.util;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(List.class)
public class StringListArrayTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws
        SQLException {
        Connection conn = ps.getConnection();
        String[] strArray = parameter.toArray(new String[0]);
        Array array = conn.createArrayOf("varchar", strArray);
        ps.setArray(i, array);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return arrayToList(rs.getArray(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return arrayToList(rs.getArray(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return arrayToList(cs.getArray(columnIndex));
    }

    private List<String> arrayToList(Array array) throws SQLException {
        if (array == null) {
            return null;
        }
        String[] strArray = (String[]) array.getArray();
        return Arrays.asList(strArray);
    }
}
