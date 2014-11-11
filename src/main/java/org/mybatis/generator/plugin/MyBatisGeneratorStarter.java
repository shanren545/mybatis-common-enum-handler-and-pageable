package org.mybatis.generator.plugin;

import org.mybatis.generator.api.ShellRunner;

public class MyBatisGeneratorStarter {

	public static void main(String[] args) throws Exception {
		/*List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		// File configFile = new File("/generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);

		Configuration config = cp
				.parseConfiguration(MyBatisGeneratorStarter.class
						.getClassLoader().getResourceAsStream(
								"generatorConfig.xml"));
		// Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
				callback, warnings);
		myBatisGenerator.generate(null);*/
		
		
		String config = MysqlPagingPlugin.class.getClassLoader()
				.getResource("generatorConfig.xml").getFile();
		String[] arg = { "-configfile", config, "-overwrite" };
		ShellRunner.main(arg);
		System.out.println("generate success!");
	}

}
