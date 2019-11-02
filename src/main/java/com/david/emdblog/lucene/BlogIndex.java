package com.david.emdblog.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.david.emdblog.constant.Constants;
import com.david.emdblog.entity.Blog;
import com.david.emdblog.service.BlogService;
import com.david.emdblog.service.impl.BloggerServiceImpl;
import com.david.emdblog.utils.DateUtil;
import com.david.emdblog.utils.UtilFuns;

/**
 * 文章索引类
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
public class BlogIndex {

	/**
	 * 更新文章文章索引
	 */
	public void updateIndex(Blog blog) throws Exception {
		IndexWriter writer = getWriter();
		Document document = new Document();
		document.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
		// document.add(new StringField("releaseDate", DateUtil.formatDate(new Date(),
		// "yyyy-MM-dd"), Field.Store.YES));
		document.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"), Field.Store.YES));
		document.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
		writer.updateDocument(new Term("id", String.valueOf(blog.getId())), document);
		if (writer != null) {
			writer.close();
			writer = null;
			if (indexWriter != null) {
				indexWriter.close();
				indexWriter = null;
			}
		}
	}

	/**
	 * 删除指定文章文章的索引
	 */
	public void deleteIndex(String blogId) throws Exception {
		IndexWriter writer = getWriter();
		writer.deleteDocuments(new Term("id", blogId));
		writer.forceMergeDeletes();// 强制删除
		writer.commit();
		if (writer != null) {
			writer.close();
			writer = null;
			if (indexWriter != null) {
				indexWriter.close();
				indexWriter = null;
			}
		}
	}

	/**
	 * 添加文章索引
	 */
	public void addIndex(Blog blog) throws Exception {
		IndexWriter writer = getWriter();
		// System.out.println(blog);
		Document document = new Document();
		document.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
		document.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"), Field.Store.YES));
		document.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
		writer.addDocument(document);
		if (writer != null) {
			writer.close();
			writer = null;
			if (indexWriter != null) {
				indexWriter.close();
				indexWriter = null;
			}
		}
	}

	/**
	 * 添加文章索引
	 */
	public void addIndexNotClosed(Blog blog) throws Exception {
		IndexWriter writer = getWriter();
		// System.out.println("data:"+ DateUtil.formatDate(blog.getReleaseDate(),
		// "yyyy-MM-dd"));
		// System.out.println("data:"+blog.getReleaseDate());
		Document document = new Document();
		document.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
		document.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"), Field.Store.YES));
		document.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
		writer.addDocument(document);
		if (writer != null) {
			writer.close();
			writer = null;
			if (indexWriter != null) {
				indexWriter.close();
				indexWriter = null;
			}
		}
		// System.out.println("添加索引成功");
	}

	/**
	 * 删除全部索引
	 */
	public void delAllIndex() throws Exception {
		// 指定索引和文档存储的目录
		IndexWriter writer = getWriter();
		// 删除所有
		writer.deleteAll();
		// 提交
		writer.commit();
		// 关闭
		if (writer != null) {
			writer.close();
			writer = null;
			if (indexWriter != null) {
				indexWriter.close();
				indexWriter = null;
			}
		}
	}

	private static Directory directory = null;
	public static IndexWriter indexWriter = null;

	/**
	 * 获取IndexWriter实例
	 */

	public static IndexWriter getWriter() throws Exception {
		if (indexWriter == null) {
			directory = FSDirectory.open(Paths.get(Constants.constant_LUCENE));
			SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
		}
		return indexWriter;
	}

	/**
	 * 查询文章信息
	 * 
	 * @param q
	 *            查询关键字
	 * @return
	 * @throws Exception
	 */
	public List<Blog> searchBlog(String q) throws Exception {
		directory = FSDirectory.open(Paths.get(Constants.constant_LUCENE));

		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher is = new IndexSearcher(reader);
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		QueryParser parser = new QueryParser("title", analyzer);
		Query query = parser.parse(q);
		QueryParser parser2 = new QueryParser("content", analyzer);
		Query query2 = parser2.parse(q);
		booleanQuery.add(query, BooleanClause.Occur.SHOULD);
		booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
		TopDocs hits = is.search(booleanQuery.build(), 100);
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);
		List<Blog> blogList = new LinkedList<Blog>();
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = is.doc(scoreDoc.doc);
			Blog blog = new Blog();
			blog.setId(Integer.parseInt(doc.get(("id"))));
			blog.setReleaseDateStr(doc.get(("releaseDate")));
			String title = doc.get("title");
			String content = doc.get("content");
			if (title != null) {
				TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
				String hTitle = highlighter.getBestFragment(tokenStream, title);
				if (UtilFuns.isEmpty(hTitle)) {
					blog.setTitle(title);
				} else {
					blog.setTitle(hTitle);
				}
			}
			if (content != null) {
				TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
				String hContent = highlighter.getBestFragment(tokenStream, content);
				if (UtilFuns.isEmpty(hContent)) {
					if (content.length() <= 200) {
						blog.setContent(content);
					} else {
						blog.setContent(content.substring(0, 200));
					}
				} else {
					blog.setContent(hContent);
				}
			}
			blogList.add(blog);
		}
		return blogList;
	}

	public static void main(String[] args) throws Exception {
		BlogIndex blogIndex = new BlogIndex();
		java.sql.Connection connection = blogIndex.getConnection();
		List<Blog> blogList = blogIndex.findAllBlog(connection);
		System.out.println(blogList.size());
		for (Blog blog : blogList) {
			blogIndex.addIndex(blog);
		}
	}

	/**
	 * 查询所有文章
	 */
	public List<Blog> findAllBlog(Connection connection) throws Exception {
		List<Blog> lists = new ArrayList<Blog>();
		if (connection != null) {
			// 不等于空，则说明连接成功 Statement是用来向数据库发送要执行的SQL语句的！
			/*
			 * 二、对数据库做增、删、改 1. 通过Connection对象创建Statement >
			 * Statement语句的发送器，它的功能就是向数据库发送sql语句！ 2. 调用它的int executeUpdate(String
			 * sql)，它可以发送DML、DDL
			 */
			Statement createStatement = connection.createStatement();
			/**
			 * 发送查询的sql语句
			 */
			String sqlSelctString = "select * from t_blog";
			// 得到查询结果，然后将查询结果的内容读取出来
			ResultSet executeQuery = createStatement.executeQuery(sqlSelctString);
			while (executeQuery.next()) {
				Blog blog = new Blog();
				blog.setId(executeQuery.getInt("id"));
				blog.setTitle(executeQuery.getString("title"));
				blog.setReleaseDate(executeQuery.getDate("releaseDate"));
				blog.setContentNoTag(executeQuery.getString("content"));
				lists.add(blog);
			}

			executeQuery.close();
			createStatement.close();
			connection.close();
		}

		return lists;

	}

	/**
	 * 加载驱动并进行获取连接 /** ClassNotFoundException找不到类 可能没导入驱动包
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 *             检查是否开启了mysql服务器以及用户名密码是否正确
	 */
	private java.sql.Connection getConnection() throws ClassNotFoundException, SQLException {
		/*
		 * jdbc四大配置参数： > driverClassName：com.mysql.j dbc.Driver >
		 * url：jdbc:mysql://localhost:3306/db3 > username：root > password：123
		 */
		/*
		 * 所有的java.sql.Driver实现类，都提供了static块，块内的代码就是把自己注册到 DriverManager中！
		 */
		/*
		 * jdbc4.0之后，每个驱动jar包中，在META-INF/services目录下提供了一个名为java.sql.Driver的文件。
		 * 文件的内容就是该接口的实现类名称！
		 */
		Class.forName("com.mysql.jdbc.Driver");
		// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
		// DriverManager.registerDriver(driver);
		// 使用url、username、password，得到连接对象
		java.sql.Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/db_blog3?useUnicode=true&amp;characterEncoding=UTF-8", "root", "1234");
		System.out.println(connection);
		return connection;
	}

}
