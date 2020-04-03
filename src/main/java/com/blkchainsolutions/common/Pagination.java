package com.blkchainsolutions.common;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Pagination {

    public static final int NUMBERS_PER_PAGE = 10;
    private int totalPages; // 总页数
    private int page;   // 当前页码
    private List resultList;    // 结果集存放List
    private int total;




    public Pagination(String sql, int currentPage, int numPerPage,JdbcTemplate jdbcTemplat) {
      if (sql == null || sql.equals("")) {
            throw new IllegalArgumentException(
                    "com.blkchainsolutions.commo.sql is empty,please initial it first. ");
        }
        String countSQL = getSQLCount(sql);
        setPage(currentPage);
        int total =this.totalRows(countSQL,jdbcTemplat);
        setTotal(total);
        setTotalPages(numPerPage,total );
        int startIndex = (currentPage - 1) * numPerPage;    //数据读取起始index

        StringBuffer paginationSQL = new StringBuffer(" ");
        paginationSQL.append(sql);
        paginationSQL.append(" limit "+ startIndex+","+numPerPage);
        setResultList(jdbcTemplat.queryForList(paginationSQL.toString()));
    }

    public String getSQLCount(String sql){
        String sqlBak = sql.toLowerCase();
        String searchValue = " from ";
        String sqlCount = "select count(*) from "+ sql.substring(sqlBak.indexOf(searchValue)+searchValue.length(), sqlBak.length());
        return sqlCount;
    }

    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public List getResultList() {
        return resultList;
    }
    public void setResultList(List resultList) {
        this.resultList = resultList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    // 计算总页数
    public void setTotalPages(int numPerPage,int totalRows) {
        if (totalRows % numPerPage == 0) {
            this.totalPages = totalRows / numPerPage;
        } else {
            this.totalPages = (totalRows / numPerPage) + 1;
        }
    }

    private  int totalRows(String countSQL,
     JdbcTemplate jdbcTemplate) {
        int rs = jdbcTemplate.execute(countSQL, new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int i = rs.getInt(1);

                    return i;

                }
                return 0;
            }
        });
        return rs;
    }

}
