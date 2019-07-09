package com.sf.daogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * 存储音乐相关
 */
public class SongRecordDaoGenerator {

  public static void main(String args[]) throws Exception {
    Schema schema = new Schema(1, "com.sf.sofarmusic.db.song");

    Entity entity = schema.addEntity("SongRecord");
    entity.addLongProperty("id").primaryKey().autoincrement();
    entity.addStringProperty("songId");
    entity.addStringProperty("content");

    new DaoGenerator().generateAll(schema, "./packages/app/src/main/java");
  }

}
