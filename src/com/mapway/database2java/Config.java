package com.mapway.database2java;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * ������������Ϣ
 * 
 * @author zhangjianshe@gmail.com
 * 
 */
public class Config {
	private Options mOptions;
	private CommandLine line;

	public Config() {
		mOptions = new Options();

		init();
	}

	public boolean parse(String[] args) throws ParseException {
		CommandLineParser parser = new BasicParser();
		line = parser.parse(mOptions, args);
		return true;
	}

	public void help() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("DatabaseGen", mOptions);
	}

	private void init() {
		Option option = OptionBuilder.withArgName("driver").hasArg()
				.withDescription("驱动").create("driver");
		mOptions.addOption(option);

		option = OptionBuilder.withArgName("nutz").hasArg()
				.withDescription("只生成NUTZBEAN").create("nutz");
		mOptions.addOption(option);

		option = OptionBuilder.withArgName("jdbcurl").hasArg()
				.withDescription("JDBCURL").create("jdbcurl");
		mOptions.addOption(option);

		option = OptionBuilder.withArgName("schema").hasArg()
				.withDescription("schema").create("schema");
		mOptions.addOption(option);

		option = OptionBuilder.withArgName("username").hasArg()
				.withDescription("User Name").create("user");
		mOptions.addOption(option);

		option = OptionBuilder.withArgName("password").hasArg()
				.withDescription("Password").create("pwd");
		mOptions.addOption(option);

		option = OptionBuilder.withArgName("package").hasArg()
				.withDescription("Package ").create("package");
		mOptions.addOption(option);

		option = OptionBuilder.withArgName("srcpath").hasArg()
				.withDescription("out path").create("path");
		mOptions.addOption(option);

		option = OptionBuilder.withArgName("maxconnections").hasArg()
				.withDescription("connection count").create("maxconnections");
		mOptions.addOption(option);
	}

	/**
	 * ��ݿ������
	 * 
	 * @return
	 */
	public String getDriver() {
		return line.getOptionValue("driver", "com.mysql.jdbc.Driver");
	}

	/**
	 * ��ݿ�����URL
	 * 
	 * @return
	 */
	public String getJDBCURL() {
		return line.getOptionValue("jdbcurl", "");
	}

	public int getMaxConnections() {
		return Integer.valueOf(line.getOptionValue("maxconnections", "20"));
	}

	public String getSchema() {
		return line.getOptionValue("schema", "schema");
	}

	public String getUser() {
		return line.getOptionValue("user", "user");
	}

	public String getPassword() {
		return line.getOptionValue("pwd", "pwd");
	}

	public String getPackage() {
		return line.getOptionValue("package", "com.mapway.noapp");
	}

	public String getPath() {
		return line.getOptionValue("path", "");
	}

	public String getNutz() {
		return line.getOptionValue("nutz", "");
	}
}
