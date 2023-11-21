package com.example.projecthelper.util;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(List.class)
public class BooleanListArrayTypeHandler extends BaseTypeHandler<List<Boolean>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Boolean> parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = ps.getConnection();
        Boolean[] boolArray = parameter.toArray(new Boolean[0]);
        Array array = conn.createArrayOf("boolean", boolArray);
        ps.setArray(i, array);
    }

    @Override
    public List<Boolean> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return arrayToList(rs.getArray(columnName));
    }

    @Override
    public List<Boolean> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return arrayToList(rs.getArray(columnIndex));
    }

    @Override
    public List<Boolean> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return arrayToList(cs.getArray(columnIndex));
    }

    private List<Boolean> arrayToList(Array array) throws SQLException {
        if (array == null) {
            return null;
        }
        Boolean[] boolArray = (Boolean[]) array.getArray();
        return Arrays.asList(boolArray);
    }
}

