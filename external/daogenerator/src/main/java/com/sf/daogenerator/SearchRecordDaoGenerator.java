package com.sf.daogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * 存储搜索历史
 */
public class SearchRecordDaoGenerator {

  public static void main(String args[]) throws Exception {
    Schema schema = new Schema(1, "com.sf.sofarmusic.db.search");

    Entity entity = schema.addEntity("SearchRecord");
    entity.addLongProperty("id").primaryKey().autoincrement();
    entity.addStringProperty("word");
    entity.addIntProperty("linkType");
    entity.addStringProperty("linkUrl");

    new DaoGenerator().generateAll(schema, "./packages/app/src/main/java");
  }

}
