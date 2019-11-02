package com.david.emdblog.utils;

/**
 * @Author ：程序员小冰
 * @GitHub: https://github.com/QQ986945193
 */
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 最简单的一个jdbc的封装工具类
 */
public class JdbcUtils {


	// 使用默认的配置文件，要求必须给出c3p0-config.xml
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

	/**
	 * 使用连接池返回一个connection连接对象
	 * 
	 * @throws SQLException
	 */
	public static Connection getC3P0Connection() throws SQLException {
		return dataSource.getConnection();
	}

	/**
	 * 返回连接池对象
	 */
	public static DataSource getDataSource() {
		return dataSource;
	}
}
